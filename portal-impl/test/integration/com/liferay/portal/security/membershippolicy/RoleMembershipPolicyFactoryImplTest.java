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

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyFactory;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyFactoryUtil;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyUtil;
import com.liferay.portal.model.impl.RoleImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.AtomicState;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class RoleMembershipPolicyFactoryImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			RoleMembershipPolicy.class, new TestRoleMembershipPolicy(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();

		_serviceRegistration.unregister();
	}

	@Test
	public void testCheckRoles() throws Exception {
		_atomicState.reset();

		long[] array = {1, 2, 3};

		RoleMembershipPolicyUtil.checkRoles(array, array, array);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testGetRoleMembershipPolicy() {
		RoleMembershipPolicy roleMembershipPolicy =
			RoleMembershipPolicyFactoryUtil.getRoleMembershipPolicy();

		Class<?> clazz = roleMembershipPolicy.getClass();

		Assert.assertEquals(
			TestRoleMembershipPolicy.class.getName(), clazz.getName());
	}

	@Test
	public void testGetRoleMembershipPolicyFactory() {
		RoleMembershipPolicyFactory roleMembershipPolicyFactory =
			RoleMembershipPolicyFactoryUtil.getRoleMembershipPolicyFactory();

		RoleMembershipPolicy roleMembershipPolicy =
			roleMembershipPolicyFactory.getRoleMembershipPolicy();

		Class<?> clazz = roleMembershipPolicy.getClass();

		Assert.assertEquals(
			TestRoleMembershipPolicy.class.getName(), clazz.getName());
	}

	@Test
	public void testIsRoleAllowed() throws Exception {
		Assert.assertTrue(RoleMembershipPolicyUtil.isRoleAllowed(1, 1));
		Assert.assertFalse(RoleMembershipPolicyUtil.isRoleAllowed(2, 2));
	}

	@Test
	public void testIsRoleRequired() throws Exception {
		Assert.assertTrue(RoleMembershipPolicyUtil.isRoleRequired(1, 1));
		Assert.assertFalse(RoleMembershipPolicyUtil.isRoleRequired(2, 2));
	}

	@Test
	public void testPropagateRoles() throws Exception {
		_atomicState.reset();

		long[] array = {1, 2, 3};

		RoleMembershipPolicyUtil.propagateRoles(array, array, array);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testVerifyPolicy1() throws Exception {
		_atomicState.reset();

		RoleMembershipPolicyUtil.verifyPolicy();

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testVerifyPolicy2() throws Exception {
		_atomicState.reset();

		RoleMembershipPolicyUtil.verifyPolicy(new RoleImpl());

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testVerifyPolicy3() throws Exception {
		_atomicState.reset();

		RoleMembershipPolicyUtil.verifyPolicy(
			new RoleImpl(), new RoleImpl(),
			new HashMap<String, Serializable>());

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;
	private static ServiceRegistration<RoleMembershipPolicy>
		_serviceRegistration;

	private static class TestRoleMembershipPolicy
		implements RoleMembershipPolicy {

		@Override
		public void checkRoles(
			long[] userIds, long[] addRoleIds, long[] removeRoleIds) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public boolean isRoleAllowed(long userId, long roleId) {
			if (userId == 1) {
				return true;
			}

			return false;
		}

		@Override
		public boolean isRoleRequired(long userId, long roleId) {
			if (userId == 1) {
				return true;
			}

			return false;
		}

		@Override
		public void propagateRoles(
			long[] userIds, long[] addRoleIds, long[] removeRoleIds) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public void verifyPolicy() {
			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public void verifyPolicy(Role role) {
			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public void verifyPolicy(
			Role role, Role oldRole,
			Map<String, Serializable> oldExpandoAttributes) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
			_atomicBoolean = atomicBoolean;
		}

		private AtomicBoolean _atomicBoolean;

	}

}