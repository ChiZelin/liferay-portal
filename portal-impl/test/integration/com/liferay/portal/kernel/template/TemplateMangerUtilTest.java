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

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.template.bundle.templatemanagerutil.TestTemplate;
import com.liferay.portal.kernel.template.bundle.templatemanagerutil.TestTemplateManager;
import com.liferay.portal.kernel.template.bundle.templatemanagerutil.TestTemplateResource;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class TemplateMangerUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			TemplateManager.class, new TestTemplateManager(),
			new HashMap<String, Object>() {
				{
					put("language.type", "English");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetTemplate1() throws TemplateException {
		TestTemplateResource testTemplateResource = new TestTemplateResource();

		Template template = TemplateManagerUtil.getTemplate(
			TestTemplateManager.TEST_TEMPLATE_MANAGER_NAME,
			testTemplateResource, false);

		Class<?> clazz = template.getClass();

		Assert.assertEquals(TestTemplate.class.getName(), clazz.getName());
	}

	@Test
	public void testGetTemplate2() throws TemplateException {
		TestTemplateResource testTemplateResource = new TestTemplateResource();

		Template template = TemplateManagerUtil.getTemplate(
			TestTemplateManager.TEST_TEMPLATE_MANAGER_NAME,
			testTemplateResource, null, false);

		Class<?> clazz = template.getClass();

		Assert.assertEquals(TestTemplate.class.getName(), clazz.getName());
	}

	@Test
	public void testGetTemplateManager() {
		TemplateManager templateManager =
			TemplateManagerUtil.getTemplateManager(
				TestTemplateManager.TEST_TEMPLATE_MANAGER_NAME);

		Class<?> clazz = templateManager.getClass();

		Assert.assertEquals(
			TestTemplateManager.class.getName(), clazz.getName());
	}

	@Test
	public void testGetTemplateManagerName() {
		Set<String> templateManagerNames =
			TemplateManagerUtil.getTemplateManagerNames();

		Assert.assertTrue(
			templateManagerNames.toString(),
			templateManagerNames.contains(
				TestTemplateManager.TEST_TEMPLATE_MANAGER_NAME));
	}

	@Test
	public void testGetTemplateManagers() {
		Map<String, TemplateManager> templateManagers =
			TemplateManagerUtil.getTemplateManagers();

		TemplateManager templateManager = templateManagers.get(
			TestTemplateManager.TEST_TEMPLATE_MANAGER_NAME);

		Class<?> clazz = templateManager.getClass();

		Assert.assertEquals(
			TestTemplateManager.class.getName(), clazz.getName());
	}

	@Test
	public void testHasTemplateManager() {
		Assert.assertTrue(
			TemplateManagerUtil.hasTemplateManager(
				TestTemplateManager.TEST_TEMPLATE_MANAGER_NAME));
	}

	private static ServiceRegistration<TemplateManager> _serviceRegistration;

}