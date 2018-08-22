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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.InvokerPortlet;
import com.liferay.portal.kernel.portlet.LiferayResourceRequest;
import com.liferay.portal.kernel.servlet.PortletServlet;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletAsyncContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderParameters;
import javax.portlet.ResourceParameters;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Neil Griffin
 */
public class ResourceRequestImpl
	extends ClientDataRequestImpl implements LiferayResourceRequest {

	@Override
	public String getCacheability() {
		return _cacheablity;
	}

	@Override
	public DispatcherType getDispatcherType() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();

		return httpServletRequest.getDispatcherType();
	}

	@Override
	public String getETag() {
		return null;
	}

	@Override
	public String getLifecycle() {
		return PortletRequest.RESOURCE_PHASE;
	}

	@Override
	public PortletAsyncContext getPortletAsyncContext() {
		if (!isAsyncSupported() || !isAsyncStarted()) {
			throw new IllegalStateException();
		}

		return _portletAsyncContextImpl;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 * 	           RenderState#getRenderParameters()}
	 */
	@Deprecated
	@Override
	public Map<String, String[]> getPrivateRenderParameterMap() {
		Map<String, String[]> privateRenderParameters = new HashMap<>();

		RenderParameters renderParameters = getRenderParameters();

		Set<String> renderParameterNames = renderParameters.getNames();

		for (String renderParameterName : renderParameterNames) {
			if (!renderParameters.isPublic(renderParameterName)) {
				privateRenderParameters.put(
					renderParameterName,
					renderParameters.getValues(renderParameterName));
			}
		}

		if (privateRenderParameters.isEmpty()) {
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(privateRenderParameters);
	}

	@Override
	public String getResourceID() {
		return _resourceID;
	}

	@Override
	public ResourceParameters getResourceParameters() {
		return _resourceParameters;
	}

	@Override
	public void init(
		HttpServletRequest request, Portlet portlet,
		InvokerPortlet invokerPortlet, PortletContext portletContext,
		WindowState windowState, PortletMode portletMode,
		PortletPreferences preferences, long plid) {

		if (Validator.isNull(windowState.toString())) {
			windowState = WindowState.NORMAL;
		}

		if (Validator.isNull(portletMode.toString())) {
			portletMode = PortletMode.VIEW;
		}

		super.init(
			request, portlet, invokerPortlet, portletContext, windowState,
			portletMode, preferences, plid);

		_cacheablity = ParamUtil.getString(
			request, "p_p_cacheability", ResourceURL.PAGE);

		_resourceID = request.getParameter("p_p_resource_id");

		if (!PortalUtil.isValidResourceId(_resourceID)) {
			_resourceID = StringPool.BLANK;
		}

		String portletNamespace = PortalUtil.getPortletNamespace(
			getPortletName());

		_resourceParameters = new ResourceParametersImpl(
			getPortletParameterMap(request, portletNamespace),
			portletNamespace);
	}

	@Override
	public boolean isAsyncStarted() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();

		return httpServletRequest.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		Portlet portlet = getPortlet();

		return portlet.isAsyncSupported();
	}

	@Override
	public PortletAsyncContext startPortletAsync()
		throws IllegalStateException {

		ResourceResponse resourceResponse = (ResourceResponse)getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);

		return startPortletAsync(this, resourceResponse);
	}

	@Override
	public PortletAsyncContext startPortletAsync(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IllegalStateException {

		if (!isAsyncSupported()) {
			throw new IllegalStateException();
		}

		if (_portletAsyncContextImpl != null) {
			return _portletAsyncContextImpl;
		}

		HttpServletRequest httpServletRequest =
			(HttpServletRequest)getAttribute(
				PortletServlet.PORTLET_SERVLET_REQUEST);

		HttpServletResponse httpServletResponse =
			(HttpServletResponse)getAttribute(
				PortletServlet.PORTLET_SERVLET_RESPONSE);

		AsyncContext asyncContext = httpServletRequest.startAsync(
			httpServletRequest, httpServletResponse);

		boolean hasOriginalRequestAndResponse = false;

		if ((resourceRequest == this) &&
			(resourceResponse == getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE))) {

			hasOriginalRequestAndResponse = true;
		}

		_portletAsyncContextImpl = new PortletAsyncContextImpl(
			resourceRequest, resourceResponse, asyncContext,
			hasOriginalRequestAndResponse);

		//The portletConfig is already set by PortletRequestImpl.defineObjects

		setAttribute(JavaConstants.JAVAX_PORTLET_REQUEST, resourceRequest);
		setAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE, resourceResponse);

		return _portletAsyncContextImpl;
	}

	private String _cacheablity;
	private PortletAsyncContextImpl _portletAsyncContextImpl;
	private String _resourceID;
	private ResourceParameters _resourceParameters;

}