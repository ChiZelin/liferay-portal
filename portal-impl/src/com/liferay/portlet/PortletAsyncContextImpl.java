/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.executor.CopyThreadLocalCallable;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.portlet.LiferayPortletAsyncContext;
import com.liferay.portal.kernel.util.DefaultThreadLocalBinder;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.servlet.DynamicServletRequestUtil;

import javax.portlet.PortletAsyncListener;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ResourceRequestWrapper;
import javax.portlet.filter.ResourceResponseWrapper;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Leon Chi
 * @author Dante Wang
 */
public class PortletAsyncContextImpl implements LiferayPortletAsyncContext {

	public PortletAsyncContextImpl(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		AsyncContext asyncContext) {

		_resourceRequest = resourceRequest;
		_resourceResponse = resourceResponse;
		_asyncContext = asyncContext;

		_portletAsyncListenerAdapter =
			new PortletAsyncListenerAdapter(this);

		_asyncContext.addListener(_portletAsyncListenerAdapter);

		_asyncPortletServletRequest =
			(AsyncPortletServletRequest)_asyncContext.getRequest();
	}

	@Override
	public void addListener(PortletAsyncListener portletAsyncListener)
		throws IllegalStateException {

		addListener(portletAsyncListener, null, null);
	}

	@Override
	public void addListener(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IllegalStateException {

		_portletAsyncListenerAdapter.addListener(
			portletAsyncListener, resourceRequest, resourceResponse);
	}

	@Override
	public void complete() throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _calledComplete || _calledDispatch) {
			throw new IllegalStateException();
		}

		_calledComplete = true;

		_asyncContext.complete();
	}

	@Override
	public <T extends PortletAsyncListener> T createPortletAsyncListener(
			Class<T> aClass)
		throws PortletException {

		T portletAsyncListener = null;

		try {
			portletAsyncListener = aClass.newInstance();
		}
		catch (Throwable e) {
			throw new PortletException(e);
		}

		return (T)portletAsyncListener;
	}

	@Override
	public void dispatch() throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _calledComplete) {
			throw new IllegalStateException();
		}

		_calledDispatch = true;

		//_asyncContext.dispatch();
	}

	@Override
	public void dispatch(String path) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _calledComplete) {
			throw new IllegalStateException();
		}

		_calledDispatch = true;

		// TODO: Dispatcher Handling
		//
		// Problem here is Tomcat's internal logic of dispatcher object obtained
		// from the servlet context. The servlet request here is wrapped by
		// Equinox and its servlet context will return a Equinox request
		// dispatcher, which is not "AsyncDispatcher", and Tomcat will not
		// dispatch it.
		//
		// However, if we supply the original Tomcat request's servlet context,
		// the path will be incorrect.
		//
		// The idea is to prepare the full request uri here, and wrap the
		// ServletContext with proxy to create the request dispatcher with the
		// full uri.
		//
		// No it doesn't work.

		ServletRequest originalRequest = _asyncPortletServletRequest;

		while (originalRequest instanceof ServletRequestWrapper) {
			originalRequest =
				((ServletRequestWrapper)originalRequest).getRequest();
		}

		ServletContext servletContext = originalRequest.getServletContext();

		ServletContext proxyServletContext =
			(ServletContext)ProxyUtil.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class[]{ServletContext.class},
				(proxy, method, args) -> {
					if ("getRequestDispatcher".equals(method.getName())) {
						return servletContext.getRequestDispatcher(
							_getFullPath(path));
					}

					try {
						return method.invoke(servletContext, args);
					}
					catch (InvocationTargetException ite) {
						throw ite.getCause();
					}
				}
			);

		_updateDispatchInfo(path);

		_asyncContext.dispatch(proxyServletContext, path);
	}

	@Override
	public ResourceRequest getResourceRequest() throws IllegalStateException {
		return _resourceRequest;
	}

	@Override
	public ResourceResponse getResourceResponse() throws IllegalStateException {
		return _resourceResponse;
	}

	@Override
	public long getTimeout() {
		return _asyncContext.getTimeout();
	}

	@Override
	public boolean hasOriginalRequestAndResponse() {
		if (_resourceRequest instanceof ResourceRequestWrapper ||
			_resourceResponse instanceof ResourceResponseWrapper) {

			return false;
		}

		return true;
	}

	public boolean isCalledComplete() {
		return _calledComplete;
	}

	public boolean isCalledDispatch() {
		return _calledDispatch;
	}

	@Override
	public void setTimeout(long timeout) {
		_asyncContext.setTimeout(timeout);
	}

	@Override
	public void start(Runnable runnable) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _calledComplete || _calledDispatch) {
			throw new IllegalStateException();
		}

		_pendingRunnable = new PortletAsyncRunnableWrapper(runnable);
	}

	@Override
	public void doStart() {
		if (_pendingRunnable == null) {
			return;
		}

		_asyncContext.start(_pendingRunnable);

		_pendingRunnable = null;
	}

	// TODO: Copied from PortletRequestDispatcherImpl
	protected HttpServletRequest createDynamicServletRequest(
		HttpServletRequest httpServletRequest,
		PortletRequestImpl portletRequestImpl,
		Map<String, String[]> parameterMap) {

		return DynamicServletRequestUtil.createDynamicServletRequest(
			httpServletRequest, portletRequestImpl.getPortlet(), parameterMap,
			true);
	}

	protected Map<String, String[]> toParameterMap(String queryString) {
		Map<String, String[]> parameterMap = new HashMap<>();

		for (String parameter :
			StringUtil.split(queryString, CharPool.AMPERSAND)) {

			String[] parameterArray = StringUtil.split(
				parameter, CharPool.EQUAL);

			String name = parameterArray[0];

			String value = StringPool.BLANK;

			if (parameterArray.length == 2) {
				value = parameterArray[1];
			}

			String[] values = parameterMap.get(name);

			if (values == null) {
				parameterMap.put(name, new String[] {value});
			}
			else {
				String[] newValues = new String[values.length + 1];

				System.arraycopy(values, 0, newValues, 0, values.length);

				newValues[newValues.length - 1] = value;

				parameterMap.put(name, newValues);
			}
		}

		return parameterMap;
	}

	private String _getFullPath(String path) {
		return _resourceRequest.getContextPath().concat(path);
	}

	private void _updateDispatchInfo(String path) {
		PortletRequestImpl portletRequestImpl =
			PortletRequestImpl.getPortletRequestImpl(_resourceRequest);

		String pathInfo = null;
		String queryString = null;
		String requestURI = null;
		String servletPath = null;

		if (path != null) {
			String pathNoQueryString = path;

			int pos = path.indexOf(CharPool.QUESTION);

			if (pos != -1) {
				pathNoQueryString = path.substring(0, pos);
				queryString = path.substring(pos + 1);
			}

			Portlet portlet = portletRequestImpl.getPortlet();

			PortletApp portletApp = portlet.getPortletApp();

			Set<String> servletURLPatterns = portletApp.getServletURLPatterns();

			for (String urlPattern : servletURLPatterns) {
				if (urlPattern.endsWith("/*")) {
					int length = urlPattern.length() - 2;

					if ((pathNoQueryString.length() > length) &&
						pathNoQueryString.regionMatches(
							0, urlPattern, 0, length) &&
						(pathNoQueryString.charAt(length) == CharPool.SLASH)) {

						pathInfo = pathNoQueryString.substring(length);
						servletPath = urlPattern.substring(0, length);

						break;
					}
				}
			}

			if (servletPath == null) {
				servletPath = pathNoQueryString;
			}

			String contextPath = _resourceRequest.getContextPath();

			if (contextPath.equals(StringPool.SLASH)) {
				requestURI = pathNoQueryString;
			}
			else {
				requestURI = contextPath + pathNoQueryString;
			}
		}

		_asyncPortletServletRequest.setPathInfo(pathInfo);
		_asyncPortletServletRequest.setQueryString(queryString);
		_asyncPortletServletRequest.setRequestURI(requestURI);
		_asyncPortletServletRequest.setServletPath(servletPath);
	}

	private AsyncPortletServletRequest _asyncPortletServletRequest;
	private final PortletAsyncListenerAdapter _portletAsyncListenerAdapter;
	private AsyncContext _asyncContext;
	private boolean _calledComplete;
	private boolean _calledDispatch;
	private Runnable _pendingRunnable;
	private final ResourceRequest _resourceRequest;
	private final ResourceResponse _resourceResponse;

	private class PortletAsyncRunnableWrapper
		extends CopyThreadLocalCallable<Object> implements Runnable {

		public PortletAsyncRunnableWrapper(Runnable runnable) {
			super(new DefaultThreadLocalBinder(), false, true);

			_runnable = runnable;
		}

		@Override
		public Object doCall() throws Exception {
			_runnable.run();

			return null;
		}

		@Override
		public void run() {
			try {
				call();
			}
			catch (Throwable t) {

				// Tomcat doesn't invoke onError

				try {
					_portletAsyncListenerAdapter.onError(
						new AsyncEvent(_asyncContext, t));
				}
				catch (IOException e) {
				}
			}
		}

		private final Runnable _runnable;

	}

}