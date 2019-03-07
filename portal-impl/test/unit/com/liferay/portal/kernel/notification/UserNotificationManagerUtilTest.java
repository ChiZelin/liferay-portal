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

package com.liferay.portal.kernel.notification;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.UserNotificationFeedEntry;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationManagerUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.model.impl.UserNotificationEventImpl;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class UserNotificationManagerUtilTest {

	@BeforeClass
	public static void setUpClass() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			UserNotificationHandler.class, new TestUserNotificationHandler(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetUserNotificationHandlers() {
		Map<String, Map<String, UserNotificationHandler>>
			userNotificationHandlersMap =
				UserNotificationManagerUtil.getUserNotificationHandlers();

		Assert.assertNotNull(userNotificationHandlersMap);

		Map<String, UserNotificationHandler> userNotificationHandlers =
			userNotificationHandlersMap.get(
				TestUserNotificationHandler.SELECTOR);

		Assert.assertNotNull(userNotificationHandlers);

		UserNotificationHandler userNotificationHandler =
			userNotificationHandlers.get(
				TestUserNotificationHandler.PORTLET_ID);

		Assert.assertNotNull(userNotificationHandler);

		Class<? extends UserNotificationHandler> clazz =
			userNotificationHandler.getClass();

		Assert.assertEquals(
			TestUserNotificationHandler.class.getName(), clazz.getName());
	}

	@Test
	public void testInterpret() throws PortalException {
		UserNotificationEvent userNotificationEvent =
			new UserNotificationEventImpl();

		userNotificationEvent.setType(TestUserNotificationHandler.PORTLET_ID);

		UserNotificationFeedEntry userNotificationFeedEntry =
			UserNotificationManagerUtil.interpret(
				TestUserNotificationHandler.SELECTOR, userNotificationEvent,
				null);

		Assert.assertEquals(
			TestUserNotificationHandler.LINK,
			userNotificationFeedEntry.getLink());
	}

	@Test
	public void testIsDeliver() {
		try {
			boolean deliver = UserNotificationManagerUtil.isDeliver(
				1, TestUserNotificationHandler.SELECTOR,
				TestUserNotificationHandler.PORTLET_ID, 1, 1, 1, null);

			Assert.assertTrue(deliver);
		}
		catch (Exception e) {
			throw new Error(e);
		}
	}

	private static ServiceRegistration<UserNotificationHandler>
		_serviceRegistration;

	private static class TestUserNotificationHandler
		implements UserNotificationHandler {

		public static final String LINK = "http://www.liferay.com";

		public static final String PORTLET_ID = "PORTLET_ID";

		public static final String SELECTOR = "SELECTOR";

		@Override
		public String getPortletId() {
			return PORTLET_ID;
		}

		@Override
		public String getSelector() {
			return SELECTOR;
		}

		@Override
		public UserNotificationFeedEntry interpret(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext) {

			boolean applicable = isApplicable(
				userNotificationEvent, serviceContext);

			return new UserNotificationFeedEntry(
				false, "body", LINK, applicable);
		}

		@Override
		public boolean isDeliver(
			long userId, long classNameId, int notificationType,
			int deliveryType, ServiceContext serviceContext) {

			if (userId == 1) {
				return true;
			}

			return false;
		}

		@Override
		public boolean isOpenDialog() {
			return false;
		}

	}

}