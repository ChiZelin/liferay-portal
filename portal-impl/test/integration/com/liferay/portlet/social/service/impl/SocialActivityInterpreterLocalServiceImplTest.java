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

package com.liferay.portlet.social.service.impl;

import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.social.service.impl.bundle.socialactivityinterpreterlocalserviceimpl.TestSocialActivityInterpreter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;
import com.liferay.social.kernel.model.SocialActivityInterpreter;
import com.liferay.social.kernel.service.SocialActivityInterpreterLocalServiceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class SocialActivityInterpreterLocalServiceImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			SocialActivityInterpreter.class,
			new TestSocialActivityInterpreter(),
			new HashMap<String, Object>() {
				{
					put(
						"javax.portlet.name",
						"SocialActivityInterpreterLocalServiceImplTest");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetActivityInterpreters1() {
		Map<String, List<SocialActivityInterpreter>> activityInterpreters =
			SocialActivityInterpreterLocalServiceUtil.getActivityInterpreters();

		List<SocialActivityInterpreter> socialActivityInterpreters =
			activityInterpreters.get(TestSocialActivityInterpreter.SELECTOR);

		Assert.assertEquals(
			socialActivityInterpreters.toString(), 1,
			socialActivityInterpreters.size());

		SocialActivityInterpreter socialActivityInterpreter =
			socialActivityInterpreters.get(0);

		Assert.assertEquals(
			TestSocialActivityInterpreter.SELECTOR,
			socialActivityInterpreter.getSelector());

		String[] classNames = socialActivityInterpreter.getClassNames();

		Assert.assertEquals(Arrays.toString(classNames), 1, classNames.length);
		Assert.assertEquals(
			TestSocialActivityInterpreter.class.getName(), classNames[0]);
	}

	@Test
	public void testGetActivityInterpreters2() {
		List<SocialActivityInterpreter> activityInterpreters =
			SocialActivityInterpreterLocalServiceUtil.getActivityInterpreters(
				TestSocialActivityInterpreter.SELECTOR);

		Assert.assertEquals(
			activityInterpreters.toString(), 1, activityInterpreters.size());

		SocialActivityInterpreter socialActivityInterpreter =
			activityInterpreters.get(0);

		Assert.assertEquals(
			TestSocialActivityInterpreter.SELECTOR,
			socialActivityInterpreter.getSelector());

		String[] classNames = socialActivityInterpreter.getClassNames();

		Assert.assertEquals(Arrays.toString(classNames), 1, classNames.length);
		Assert.assertEquals(
			TestSocialActivityInterpreter.class.getName(), classNames[0]);
	}

	private static ServiceRegistration<SocialActivityInterpreter>
		_serviceRegistration;

}