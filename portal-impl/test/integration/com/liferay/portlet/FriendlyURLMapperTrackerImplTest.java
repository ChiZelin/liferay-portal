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

import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.FriendlyURLMapperTracker;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.bundle.friendlyurlmappertrackerimpl.TestFriendlyURLMapper;
import com.liferay.portlet.internal.FriendlyURLMapperTrackerImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class FriendlyURLMapperTrackerImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			FriendlyURLMapper.class, new TestFriendlyURLMapper(),
			new HashMap<String, Object>() {
				{
					put(
						"javax.portlet.name",
						"FriendlyURLMapperTrackerImplTest");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetFriendlyURLMapper() throws Exception {
		Portlet portlet = new PortletImpl();

		portlet.setPortletClass(MVCPortlet.class.getName());
		portlet.setPortletId("FriendlyURLMapperTrackerImplTest");

		FriendlyURLMapperTracker friendlyURLMapperTracker =
			new FriendlyURLMapperTrackerImpl(portlet);

		FriendlyURLMapper friendlyURLMapper =
			friendlyURLMapperTracker.getFriendlyURLMapper();

		Class<?> clazz = friendlyURLMapper.getClass();

		Assert.assertEquals(
			TestFriendlyURLMapper.class.getName(), clazz.getName());
	}

	private static ServiceRegistration<FriendlyURLMapper> _serviceRegistration;

}