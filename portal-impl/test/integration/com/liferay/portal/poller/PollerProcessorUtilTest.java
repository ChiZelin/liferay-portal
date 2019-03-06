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

package com.liferay.portal.poller;

import com.liferay.portal.kernel.poller.PollerProcessor;
import com.liferay.portal.kernel.poller.PollerRequest;
import com.liferay.portal.kernel.poller.PollerResponse;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class PollerProcessorUtilTest {

	@BeforeClass
	public static void setUpClass() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			PollerProcessor.class, new TestPollerProcessor(),
			new HashMap<String, Object>() {
				{
					put("javax.portlet.name", "PollerProcessorUtilTest");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetPollerProcessor() {
		PollerProcessor pollerProcessor =
			PollerProcessorUtil.getPollerProcessor("PollerProcessorUtilTest");

		Assert.assertNotNull(pollerProcessor);

		Class<?> clazz = pollerProcessor.getClass();

		Assert.assertEquals(
			TestPollerProcessor.class.getName(), clazz.getName());
	}

	private static ServiceRegistration<PollerProcessor> _serviceRegistration;

	private static class TestPollerProcessor implements PollerProcessor {

		@Override
		public PollerResponse receive(PollerRequest pollerRequest) {
			return null;
		}

		@Override
		public void send(PollerRequest pollerRequest) {
		}

	}

}