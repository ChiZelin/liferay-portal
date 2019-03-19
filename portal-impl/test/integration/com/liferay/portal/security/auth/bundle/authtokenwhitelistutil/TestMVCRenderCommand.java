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

package com.liferay.portal.security.auth.bundle.authtokenwhitelistutil;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.PortletKeys;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Tomas Polesovsky
 */
public class TestMVCRenderCommand implements MVCRenderCommand {

	public static final String TEST_MVC_COMMAND_NAME =
		"TEST_MVC_RENDER_COMMAND_NAME";

	public static final String TEST_PORTLET_ID = PortletKeys.PORTAL;

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		return null;
	}

}