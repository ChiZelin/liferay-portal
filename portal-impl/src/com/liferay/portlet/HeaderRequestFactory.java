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

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.InvokerPortlet;
import com.liferay.portlet.internal.HeaderRequestImpl;

import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Neil Griffin
 */
@ProviderType
public class HeaderRequestFactory {

	public static HeaderRequestImpl create(
			HttpServletRequest request, Portlet portlet,
			InvokerPortlet invokerPortlet, PortletContext portletContext,
			WindowState windowState, PortletMode portletMode,
			PortletPreferences preferences)
		throws Exception {

		return create(
			request, portlet, invokerPortlet, portletContext, windowState,
			portletMode, preferences, 0);
	}

	public static HeaderRequestImpl create(
			HttpServletRequest request, Portlet portlet,
			InvokerPortlet invokerPortlet, PortletContext portletContext,
			WindowState windowState, PortletMode portletMode,
			PortletPreferences preferences, long plid)
		throws Exception {

		HeaderRequestImpl headerRequestImpl = new HeaderRequestImpl();

		headerRequestImpl.init(
			request, portlet, invokerPortlet, portletContext, windowState,
			portletMode, preferences, plid);

		return headerRequestImpl;
	}

}