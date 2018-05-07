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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dante Wang
 */
public class PortletAsyncListenerAdapter implements AsyncListener {

	public PortletAsyncListenerAdapter(
		PortletAsyncContext portletAsyncContext) {

		_portletAsyncContext = portletAsyncContext;
	}

	public void addListener(PortletAsyncListener portletAsyncListener)
		throws IllegalStateException {

		addListener(portletAsyncListener, null, null);
	}

	public void addListener(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IllegalStateException {

		try {
			if (firedOnComplete) {
				portletAsyncListener.onComplete(
					new PortletAsyncEvent(
						_portletAsyncContext, resourceRequest,
						resourceResponse));
			}

			if (firedOnError) {
				portletAsyncListener.onError(
					new PortletAsyncEvent(
						_portletAsyncContext, resourceRequest,
						resourceResponse));
			}

			if (firedOnTimeout) {
				portletAsyncListener.onTimeout(
					new PortletAsyncEvent(
						_portletAsyncContext, resourceRequest,
						resourceResponse));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		_portletAsyncListenerAdapterEntries.add(
			new PortletAsyncListenerAdapterEntry(
				portletAsyncListener, resourceRequest, resourceResponse));
	}

	@Override
	public void onComplete(AsyncEvent asyncEvent) throws IOException {
		for (PortletAsyncListenerAdapterEntry entry :
			 _portletAsyncListenerAdapterEntries) {

			PortletAsyncListener portletAsyncListener =
				entry._portletAsyncListener;

			portletAsyncListener.onComplete(
				new PortletAsyncEvent(
					_portletAsyncContext, entry._resourceRequest,
					entry._resourceResponse));
		}

		firedOnComplete = true;
	}

	@Override
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {
		for (PortletAsyncListenerAdapterEntry entry :
			_portletAsyncListenerAdapterEntries) {

			PortletAsyncListener portletAsyncListener =
				entry._portletAsyncListener;

			portletAsyncListener.onTimeout(
				new PortletAsyncEvent(
					_portletAsyncContext, entry._resourceRequest,
					entry._resourceResponse));
		}

		firedOnTimeout = true;

		try {
			_portletAsyncContext.complete();
		}
		catch (Throwable t) {
		}
	}

	@Override
	public void onError(AsyncEvent asyncEvent) throws IOException {
		for (PortletAsyncListenerAdapterEntry entry :
			_portletAsyncListenerAdapterEntries) {

			PortletAsyncListener portletAsyncListener =
				entry._portletAsyncListener;

			portletAsyncListener.onError(
				new PortletAsyncEvent(
					_portletAsyncContext, entry._resourceRequest,
					entry._resourceResponse, asyncEvent.getThrowable()));
		}

		firedOnError = true;

		try {
			_portletAsyncContext.complete();
		}
		catch (Throwable t) {
		}
	}

	@Override
	public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
		List<PortletAsyncListenerAdapterEntry>
			portletAsyncListenerAdapterEntriesCopy = new ArrayList<>();

		portletAsyncListenerAdapterEntriesCopy.addAll(
			_portletAsyncListenerAdapterEntries);

		_portletAsyncListenerAdapterEntries.clear();

		for (PortletAsyncListenerAdapterEntry entry :
			portletAsyncListenerAdapterEntriesCopy) {

			PortletAsyncListener portletAsyncListener =
				entry._portletAsyncListener;

			portletAsyncListener.onStartAsync(
				new PortletAsyncEvent(
					_portletAsyncContext, entry._resourceRequest,
					entry._resourceResponse));
		}

		firedOnComplete = false;
		firedOnError = false;
		firedOnTimeout = false;
	}

	private final List<PortletAsyncListenerAdapterEntry>
		_portletAsyncListenerAdapterEntries = new ArrayList<>();
	private final PortletAsyncContext _portletAsyncContext;

	private class PortletAsyncListenerAdapterEntry {

		public PortletAsyncListenerAdapterEntry(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) {

			_portletAsyncListener = portletAsyncListener;
			_resourceRequest = resourceRequest;
			_resourceResponse = resourceResponse;
		}

		private final PortletAsyncListener _portletAsyncListener;
		private final ResourceRequest _resourceRequest;
		private final ResourceResponse _resourceResponse;

	}
	
	private boolean firedOnComplete;
	private boolean firedOnTimeout;
	private boolean firedOnError;

}