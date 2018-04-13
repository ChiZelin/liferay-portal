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

import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.portlet.PortletAsyncContext;
import javax.portlet.PortletAsyncEvent;
import javax.portlet.PortletAsyncListener;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ResourceRequestWrapper;
import javax.portlet.filter.ResourceResponseWrapper;

/**
 * @author Leon Chi
 */
public class PortletAsyncContextImpl implements PortletAsyncContext {

	public PortletAsyncContextImpl(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		_resourceRequest = resourceRequest;
		_resourceResponse = resourceResponse;

		_startTimeMillis = System.currentTimeMillis();
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

		_portletAsyncListeners.add(
			new Entry(portletAsyncListener, resourceRequest, resourceResponse));
	}

	public void callPortletAsyncListener(
		EventSource eventSource, Throwable throwable) {

		for (Entry entry : _portletAsyncListeners) {
			PortletAsyncListener portletAsyncListener =
				entry.portletAsyncListener;

			PortletAsyncEvent portletAsyncEvent = new PortletAsyncEvent(
				this, entry.resourceRequest, entry.resourceResponse, throwable);

			try {
				if (eventSource == EventSource.COMPLETE) {
					portletAsyncListener.onComplete(portletAsyncEvent);
				}
				else if (eventSource == EventSource.ERROR) {
					portletAsyncListener.onError(portletAsyncEvent);
				}
				else if (eventSource == EventSource.STARTASYNC) {
					portletAsyncListener.onStartAsync(portletAsyncEvent);
				}
				else {
					portletAsyncListener.onTimeout(portletAsyncEvent);
				}
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void checkTimeOut() {
		long delay = _timeout - (System.currentTimeMillis() - _startTimeMillis);

		if (_timeoutFuture != null) {
			_timeoutFuture.cancel(true);
		}

		_timeoutFuture = _scheduledExecutorService.schedule(
			() -> {
				if (!isCompleted() && !isDispatched()) {
					callPortletAsyncListener(
						PortletAsyncContextImpl.EventSource.TIMEOUT, null);
				}
			},
			delay, TimeUnit.MILLISECONDS);
	}

	public void clearPortletAsyncListener() {
		_portletAsyncListeners.clear();
	}

	@Override
	public void complete() throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _completed || _dispatched) {
			throw new IllegalStateException();
		}

		callPortletAsyncListener(EventSource.COMPLETE, null);

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
		dispatch(null);
	}

	@Override
	public void dispatch(String s) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _completed) {
			throw new IllegalStateException();
		}

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
		return _timeout;
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
		_timeout = timeout;

		checkTimeOut();
	}

	@Override
	public void start(Runnable runnable) throws IllegalStateException {
		if (!_resourceRequest.isAsyncStarted() || _completed || _dispatched) {
			throw new IllegalStateException();
		}

		ExecutorService executorService =
			_portalExecutorManager.getPortalExecutor(
				PortletAsyncContextImpl.class.getName());

		executorService.execute(
			() -> {
				try {
					runnable.run();
				}
				catch (Throwable t) {
					callPortletAsyncListener(EventSource.ERROR, t);
				}
		});
	}

	public enum EventSource {

		COMPLETE, ERROR, STARTASYNC, TIMEOUT

	}

	private static volatile PortalExecutorManager _portalExecutorManager =
		ServiceProxyFactory.newServiceTrackedInstance(
			PortalExecutorManager.class, PortletAsyncContextImpl.class,
			"_portalExecutorManager", true);
	private static volatile ScheduledExecutorService _scheduledExecutorService =
		Executors.newScheduledThreadPool(1);

	private boolean _completed;
	private boolean _dispatched;
	private final List<Entry> _portletAsyncListeners =
		new CopyOnWriteArrayList<>();
	private final ResourceRequest _resourceRequest;
	private final ResourceResponse _resourceResponse;
	private final long _startTimeMillis;
	private long _timeout = 30000;
	private ScheduledFuture _timeoutFuture;

	private class Entry {

		public Entry(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) {

			this.portletAsyncListener = portletAsyncListener;
			this.resourceRequest = resourceRequest;
			this.resourceResponse = resourceResponse;
		}

		public PortletAsyncListener portletAsyncListener;
		public ResourceRequest resourceRequest;
		public ResourceResponse resourceResponse;

	}

}