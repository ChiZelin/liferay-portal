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

package com.liferay.portal.deploy.hot.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.EmailAddress;
import com.liferay.portal.kernel.service.EmailAddressLocalService;
import com.liferay.portal.kernel.service.EmailAddressLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;
import com.liferay.portal.deploy.hot.ServiceWrapperRegistry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 * @author Miguel Pastor
 */
@RunWith(Arquillian.class)
public class ServiceWrapperRegistryTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_serviceWrapperRegistry = new ServiceWrapperRegistry();

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			ServiceWrapper.class, new TestEmailLocalServiceWrapper());
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceWrapperRegistry.close();
		_serviceRegistration.unregister();
	}

	@Test
	public void testInvokeOverrideMethod() throws PortalException {
		EmailAddressLocalService emailAddressLocalService =
			(EmailAddressLocalService)PortalBeanLocatorUtil.locate(
				EmailAddressLocalService.class.getName());

		EmailAddress emailAddress = emailAddressLocalService.getEmailAddress(1);

		Assert.assertEquals("email@liferay.com", emailAddress.getAddress());
	}

	private static ServiceRegistration<ServiceWrapper> _serviceRegistration;
	private static ServiceWrapperRegistry _serviceWrapperRegistry;

	private static class TestEmailLocalServiceWrapper
		extends EmailAddressLocalServiceWrapper {

		public TestEmailLocalServiceWrapper() {
			super(null);
		}

		public TestEmailLocalServiceWrapper(
			EmailAddressLocalService emailAddressService) {

			super(emailAddressService);
		}

		@Override
		public EmailAddress getEmailAddress(long emailAddressId) {
			EmailAddress emailAddress = createEmailAddress(1);

			emailAddress.setAddress("email@liferay.com");

			return emailAddress;
		}

	}

}