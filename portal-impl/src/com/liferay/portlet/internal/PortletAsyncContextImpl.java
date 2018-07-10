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

package com.liferay.portlet.internal;

import com.liferay.portal.kernel.executor.CopyThreadLocalCallable;
import com.liferay.portal.kernel.portlet.LiferayPortletAsyncContext;
import com.liferay.portal.kernel.util.DefaultThreadLocalBinder;
import com.liferay.portlet.PortletAsyncListenerAdapter;

import java.io.IOException;

import javax.portlet.PortletAsyncListener;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ResourceRequestWrapper;
import javax.portlet.filter.ResourceResponseWrapper;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

/**
 * @author Neil Griffin
 * @author Dante Wang
 * @author Leon Chi
 */
public class PortletAsyncContextImpl implements LiferayPortletAsyncContext {

	public PortletAsyncContextImpl(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		AsyncContext asyncContext) {

		_resourceRequest = resourceRequest;
		_resourceResponse = resourceResponse;
		_asyncContext = asyncContext;

		_portletAsyncListenerAdapter = new PortletAsyncListenerAdapter(this);

		_asyncContext.addListener(_portletAsyncListenerAdapter);
	}

	@Override
	public void addListener(AsyncListener asyncListener) {

		// TODO

		throw new UnsupportedOperationException();
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

		if (!_resourceRequest.isAsyncStarted() || _calledDispatch) {
			throw new IllegalStateException();
		}

		_portletAsyncListenerAdapter.addListener(
			portletAsyncListener, resourceRequest, resourceResponse);
	}

	@Override
	public void complete() throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _calledComplete ||
			_calledDispatch) {

			throw new IllegalStateException();
		}

		_calledComplete = true;

		_asyncContext.complete();
	}

	@Override
	public <T extends PortletAsyncListener> T createPortletAsyncListener(
			Class<T> listenerClass)
		throws PortletException {

		try {
			return listenerClass.newInstance();
		}
		catch (Throwable e) {
			throw new PortletException(e);
		}
	}

	@Override
	public void dispatch() throws IllegalStateException {
		_calledDispatch = true;

		throw new UnsupportedOperationException();

		// TODO

	}

	@Override
	public void dispatch(String path) throws IllegalStateException {
		_calledDispatch = true;

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void doStart() {
		if (_pendingRunnable == null) {
			return;
		}

		_asyncContext.start(_pendingRunnable);

		_pendingRunnable = null;
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

	@Override
	public boolean isCalledDispatch() {
		return _calledDispatch;
	}

	@Override
	public void removeListener(AsyncListener asyncListener) {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void reset(AsyncContext asyncContext) {
		_calledDispatch = false;
		_calledComplete = false;

		_asyncContext = asyncContext;

		_asyncContext.addListener(_portletAsyncListenerAdapter);
	}

	@Override
	public void setTimeout(long timeout) {
		_asyncContext.setTimeout(timeout);
	}

	@Override
	public void start(Runnable runnable) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _calledComplete ||
			_calledDispatch) {

			throw new IllegalStateException();
		}

		_pendingRunnable = new PortletAsyncRunnableWrapper(runnable);
	}

	private AsyncContext _asyncContext;
	private boolean _calledComplete;
	private boolean _calledDispatch;
	private Runnable _pendingRunnable;
	private final PortletAsyncListenerAdapter _portletAsyncListenerAdapter;
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
				catch (IOException ioe) {
				}
			}
		}

		private final Runnable _runnable;

	}

}