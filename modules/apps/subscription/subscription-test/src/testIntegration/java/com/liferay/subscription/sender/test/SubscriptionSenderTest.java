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

package com.liferay.subscription.sender.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.subscription.model.Subscription;
import com.liferay.subscription.service.SubscriptionLocalServiceUtil;
import com.liferay.subscription.service.persistence.SubscriptionPersistence;
import com.liferay.subscription.service.persistence.SubscriptionUtil;
import com.liferay.subscription.util.SubscriptionSender;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Leon Chi
 */
@RunWith(Arquillian.class)
public class SubscriptionSenderTest {

	@Test
	public void testHasSubscriptionsReturnsFalseWhenNoSubscriptionsInDB() {
		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender.addPersistedSubscribers(
			Group.class.getName(), RandomTestUtil.randomInt());

		Assert.assertFalse(subscriptionSender.hasSubscribers());
	}

	@Test
	public void testHasSubscriptionsReturnsTrueWhenSubscriptionsInDB()
		throws PortalException {

		int companyId = RandomTestUtil.randomInt();
		int classPK = RandomTestUtil.randomInt();

		SubscriptionPersistence persistence = SubscriptionUtil.getPersistence();

		Subscription subscription = persistence.create(
			RandomTestUtil.nextLong());

		subscription.setCompanyId(companyId);

		subscription.setClassName(Group.class.getName());

		subscription.setClassPK(classPK);

		SubscriptionLocalServiceUtil.addSubscription(subscription);

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender.setCompanyId(companyId);

		subscriptionSender.addPersistedSubscribers(
			Group.class.getName(), classPK);

		try {
			Assert.assertTrue(subscriptionSender.hasSubscribers());
		}
		finally {
			SubscriptionLocalServiceUtil.deleteSubscription(subscription);
		}
	}

}