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

package com.liferay.portal.security.membershippolicy;

import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicyFactory;
import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicyFactoryUtil;
import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicyUtil;
import com.liferay.portal.security.membershippolicy.bundle.usergroupmembershippolicyfactoryimpl.TestUserGroupMembershipPolicy;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class UserGroupMembershipPolicyFactoryImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		TestUserGroupMembershipPolicy testUserGroupMembershipPolicy =
			new TestUserGroupMembershipPolicy();

		testUserGroupMembershipPolicy.setAtomicBoolean(_atomicBoolean);

		_serviceRegistration = registry.registerService(
			UserGroupMembershipPolicy.class, testUserGroupMembershipPolicy,
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

	@Before
	public void setUp() {
		_atomicBoolean.set(Boolean.FALSE);
	}

	@Test
	public void testCheckMembership() throws Exception {
		long[] array = {1, 2, 3};

		UserGroupMembershipPolicyUtil.checkMembership(array, array, array);

		Assert.assertTrue(_atomicBoolean.get());
	}

	@Test
	public void testGetUserGroupMembershipPolicy() {
		UserGroupMembershipPolicy userGroupMembershipPolicy =
			UserGroupMembershipPolicyFactoryUtil.getUserGroupMembershipPolicy();

		Class<?> clazz = userGroupMembershipPolicy.getClass();

		Assert.assertEquals(
			TestUserGroupMembershipPolicy.class.getName(), clazz.getName());
	}

	@Test
	public void testGetUserGroupMembershipPolicyFactory() {
		UserGroupMembershipPolicyFactory userGroupMembershipPolicyFactory =
			UserGroupMembershipPolicyFactoryUtil.
				getUserGroupMembershipPolicyFactory();

		UserGroupMembershipPolicy userGroupMembershipPolicy =
			userGroupMembershipPolicyFactory.getUserGroupMembershipPolicy();

		Class<?> clazz = userGroupMembershipPolicy.getClass();

		Assert.assertEquals(
			TestUserGroupMembershipPolicy.class.getName(), clazz.getName());
	}

	@Test
	public void testIsMembershipAllowed() throws Exception {
		Assert.assertTrue(
			UserGroupMembershipPolicyUtil.isMembershipAllowed(1, 1));
		Assert.assertFalse(
			UserGroupMembershipPolicyUtil.isMembershipAllowed(2, 2));
	}

	@Test
	public void testIsMembershipRequired() throws Exception {
		Assert.assertTrue(
			UserGroupMembershipPolicyUtil.isMembershipRequired(1, 1));
		Assert.assertFalse(
			UserGroupMembershipPolicyUtil.isMembershipRequired(2, 2));
	}

	@Test
	public void testPropagateMembership() throws Exception {
		long[] array = {1, 2, 3};

		UserGroupMembershipPolicyUtil.propagateMembership(array, array, array);

		Assert.assertTrue(_atomicBoolean.get());
	}

	@Test
	public void testVerifyPolicy1() throws Exception {
		UserGroupMembershipPolicyUtil.verifyPolicy();

		Assert.assertTrue(_atomicBoolean.get());
	}

	@Test
	public void testVerifyPolicy2() throws Exception {
		UserGroupMembershipPolicyUtil.verifyPolicy(null);

		Assert.assertTrue(_atomicBoolean.get());
	}

	@Test
	public void testVerifyPolicy3() throws Exception {
		UserGroupMembershipPolicyUtil.verifyPolicy(null, null, null);

		Assert.assertTrue(_atomicBoolean.get());
	}

	private static final AtomicBoolean _atomicBoolean = new AtomicBoolean();
	private static ServiceRegistration<UserGroupMembershipPolicy>
		_serviceRegistration;

}