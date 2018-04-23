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

import com.liferay.portal.kernel.executor.CopyThreadLocalCallable;
import com.liferay.portal.kernel.portlet.LiferayPortletAsyncContext;
import com.liferay.portal.kernel.util.DefaultThreadLocalBinder;
import com.liferay.portal.kernel.util.ProxyUtil;

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

		ServletRequest servletRequest = _asyncContext.getRequest();

		ServletRequest originalRequest = servletRequest;

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
							_getFullPath(
								(HttpServletRequest)servletRequest, path));
					}

					try {
						return method.invoke(servletContext, args);
					}
					catch (InvocationTargetException ite) {
						throw ite.getCause();
					}
				}
			);

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

	private String _getFullPath(
		HttpServletRequest httpServletRequest, String path) {

		return httpServletRequest.getContextPath().concat(path);
	}

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