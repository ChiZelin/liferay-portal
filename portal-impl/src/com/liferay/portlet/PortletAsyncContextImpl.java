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

import java.util.concurrent.ScheduledFuture;

import javax.portlet.PortletAsyncContext;
import javax.portlet.PortletAsyncListener;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ResourceRequestWrapper;
import javax.portlet.filter.ResourceResponseWrapper;

import javax.servlet.AsyncContext;

/**
 * @author Leon Chi
 * @author Dante Wang
 */
public class PortletAsyncContextImpl implements PortletAsyncContext {

	public PortletAsyncContextImpl(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		AsyncContext asyncContext) {

		_resourceRequest = resourceRequest;
		_resourceResponse = resourceResponse;
		_asyncContext = asyncContext;
	}

	@Override
	public void addListener(PortletAsyncListener portletAsyncListener)
		throws IllegalStateException {

		addListener(portletAsyncListener, _resourceRequest, _resourceResponse);
	}

	@Override
	public void addListener(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IllegalStateException {

		_asyncContext.addListener(
			new AsyncContextListenerAdapter(
				this, portletAsyncListener, resourceRequest, resourceResponse));
	}

	@Override
	public void complete() throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _completed || _dispatched) {
			throw new IllegalStateException();
		}

		_asyncContext.complete();

		_completed = true;
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
		if (!_resourceRequest.isAsyncStarted() || _completed) {
			throw new IllegalStateException();
		}

		//_asyncContext.dispatch();

		_dispatched = true;
	}

	@Override
	public void dispatch(String path) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _completed) {
			throw new IllegalStateException();
		}

		//_asyncContext.dispatch(path);

		_dispatched = true;
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

	public boolean isCompleted() {
		return _completed;
	}

	public boolean isDispatched() {
		return _dispatched;
	}

	@Override
	public void setTimeout(long timeout) {
		_asyncContext.setTimeout(timeout);
	}

	@Override
	public void start(Runnable runnable) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _completed || _dispatched) {
			throw new IllegalStateException();
		}

		_pendingRunnable = () -> {
			try {
				runnable.run();
			}
			catch (Throwable t) {
			}
		};
	}

	protected void doStart() {
		if (_pendingRunnable == null) {
			return;
		}

		_asyncContext.start(_pendingRunnable);
	}

	private AsyncContext _asyncContext;
	private boolean _completed;
	private boolean _dispatched;
	private Runnable _pendingRunnable;
	private final ResourceRequest _resourceRequest;
	private final ResourceResponse _resourceResponse;
	private ScheduledFuture _timeoutFuture;

}