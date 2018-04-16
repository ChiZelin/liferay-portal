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

import javax.portlet.PortletAsyncContext;
import javax.portlet.PortletAsyncEvent;
import javax.portlet.PortletAsyncListener;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

/**
 * @author Dante Wang
 */
public class AsyncContextListenerAdapter implements AsyncListener {

	public AsyncContextListenerAdapter(
		PortletAsyncContext portletAsyncContext,
		PortletAsyncListener portletAsyncListener,
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		_portletAsyncContext = portletAsyncContext;
		_portletAsyncListener = portletAsyncListener;
		_resourceRequest = resourceRequest;
		_resourceResponse = resourceResponse;
	}

	@Override
	public void onComplete(AsyncEvent asyncEvent) throws IOException {
		_portletAsyncListener.onComplete(
			new PortletAsyncEvent(
				_portletAsyncContext, _resourceRequest, _resourceResponse));
	}

	@Override
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {
		_portletAsyncListener.onTimeout(
			new PortletAsyncEvent(_portletAsyncContext));

		try {
			_portletAsyncContext.complete();
		}
		catch (Throwable t) {
		}
	}

	@Override
	public void onError(AsyncEvent asyncEvent) throws IOException {
		_portletAsyncListener.onError(
			new PortletAsyncEvent(
				_portletAsyncContext, asyncEvent.getThrowable()));

		try {
			_portletAsyncContext.complete();
		}
		catch (Throwable t) {
		}
	}

	@Override
	public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
		_portletAsyncListener.onStartAsync(
			new PortletAsyncEvent(
				_portletAsyncContext, _resourceRequest, _resourceResponse));
	}

	private final PortletAsyncContext _portletAsyncContext;
	private final PortletAsyncListener _portletAsyncListener;
	private final ResourceRequest _resourceRequest;
	private final ResourceResponse _resourceResponse;

}
