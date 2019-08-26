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

package com.liferay.dynamic.data.mapping.storage;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Bruno Basto
 */
public class FieldRendererFactory {

	public static FieldRenderer getFieldRenderer(String dataType) {
		FieldRenderer fieldRenderer = _fieldRenderers.get(dataType);

		if (fieldRenderer == null) {
			fieldRenderer = _fieldRenderers.get(FieldConstants.STRING);
		}

		return fieldRenderer;
	}

	private static void _initailFieldRendererMap(
		ServiceTracker<BaseFieldRenderer, BaseFieldRenderer> serviceTracker) {

		ServiceReference<BaseFieldRenderer>[] serviceReferences =
			serviceTracker.getServiceReferences();

		_fieldRenderers = new HashMap<>();

		for (ServiceReference serviceReference : serviceReferences) {
			String key = (String)serviceReference.getProperty("key");

			if (key != null) {
				_fieldRenderers.put(
					key, serviceTracker.getService(serviceReference));
			}
		}
	}

	private static Map<String, FieldRenderer> _fieldRenderers;

	static {
		Bundle bundle = FrameworkUtil.getBundle(BaseFieldRenderer.class);

		ServiceTracker<BaseFieldRenderer, BaseFieldRenderer> serviceTracker =
			new ServiceTracker<>(
				bundle.getBundleContext(), BaseFieldRenderer.class, null);

		serviceTracker.open();

		_initailFieldRendererMap(serviceTracker);
	}

}