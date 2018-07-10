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

package com.liferay.portal.servlet.filters.etag;

import com.liferay.portal.kernel.portlet.LiferayPortletAsyncContext;
import com.liferay.portal.kernel.servlet.RestrictedByteBufferCacheServletResponse;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.internal.ResourceRequestImpl;

import java.io.IOException;

import java.nio.ByteBuffer;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eduardo Lundgren
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Shuyang Zhou
 */
public class ETagFilter extends BasePortalFilter {

	public static final String SKIP_FILTER =
		ETagFilter.class.getName() + "#SKIP_FILTER";

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		if (ParamUtil.getBoolean(request, _ETAG, true) &&
			!isAlreadyFiltered(request)) {

			return true;
		}
		else {
			return false;
		}
	}

	protected boolean isAlreadyFiltered(HttpServletRequest request) {
		if (request.getAttribute(SKIP_FILTER) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	protected boolean isEligibleForETag(int status) {
		if ((status >= HttpServletResponse.SC_OK) &&
			(status < HttpServletResponse.SC_MULTIPLE_CHOICES)) {

			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		request.setAttribute(SKIP_FILTER, Boolean.TRUE);

		RestrictedByteBufferCacheServletResponse
			restrictedByteBufferCacheServletResponse =
				new RestrictedByteBufferCacheServletResponse(
					response, PropsValues.ETAG_RESPONSE_SIZE_MAX);

		processFilter(
			ETagFilter.class.getName(), request,
			restrictedByteBufferCacheServletResponse, filterChain);

		if (!request.isAsyncSupported() || !request.isAsyncStarted()) {
			_postProcessETag(
				request, response, restrictedByteBufferCacheServletResponse);
		}
		else {
			ResourceRequestImpl resourceRequestImpl =
				(ResourceRequestImpl)request.getAttribute(
					JavaConstants.JAVAX_PORTLET_REQUEST);

			LiferayPortletAsyncContext portletAsyncContext =
				(LiferayPortletAsyncContext)
					resourceRequestImpl.getPortletAsyncContext();

			AsyncListener postProcessETagAsyncListener = new AsyncListener() {

				@Override
				public void onComplete(AsyncEvent asyncEvent)
					throws IOException {

					_cleanUp();
				}

				@Override
				public void onError(AsyncEvent asyncEvent) throws IOException {
					_cleanUp();
				}

				@Override
				public void onStartAsync(AsyncEvent asyncEvent)
					throws IOException {
				}

				@Override
				public void onTimeout(AsyncEvent asyncEvent)
					throws IOException {

					_cleanUp();
				}

				private void _cleanUp() throws IOException {
					_postProcessETag(
						request, response,
						restrictedByteBufferCacheServletResponse);

					portletAsyncContext.removeListener(this);
				}

			};

			portletAsyncContext.addListener(postProcessETagAsyncListener);

			AsyncContext asyncContext = request.getAsyncContext();

			asyncContext.addListener(postProcessETagAsyncListener);
		}
	}

	private void _postProcessETag(
			HttpServletRequest request, HttpServletResponse response,
			RestrictedByteBufferCacheServletResponse
				restrictedByteBufferCacheServletResponse)
		throws IOException {

		if (!restrictedByteBufferCacheServletResponse.isOverflowed()) {
			ByteBuffer byteBuffer =
				restrictedByteBufferCacheServletResponse.getByteBuffer();

			if (!isEligibleForETag(
					restrictedByteBufferCacheServletResponse.getStatus()) ||
				!ETagUtil.processETag(request, response, byteBuffer)) {

				restrictedByteBufferCacheServletResponse.flushCache();
			}
		}
	}

	private static final String _ETAG = "etag";

}