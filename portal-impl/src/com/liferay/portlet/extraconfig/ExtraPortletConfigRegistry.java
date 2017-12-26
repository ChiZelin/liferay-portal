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

package com.liferay.portlet.extraconfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Dante Wang
 */
public class ExtraPortletConfigRegistry {

	public static ExtraPortletConfig getExtraPortletConfig(
		String rootPortletId) {

		return null;
	}

	public static void registerExtraPortletConfig(
		String rootPortletId, ExtraPortletConfig extraPortletConfig) {

		_extraPortletConfigs.put(rootPortletId, extraPortletConfig);
	}

	public static void unregisterExtraPortletConfig(String rootPortletId) {
		_extraPortletConfigs.remove(rootPortletId);
	}

	private static final ConcurrentMap<String, ExtraPortletConfig>
		_extraPortletConfigs = new ConcurrentHashMap<>();

}