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

package com.liferay.portal.cache.ehcache.internal.configurator;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.rule.NewEnv;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.test.rule.AdviseWith;
import com.liferay.portal.test.rule.AspectJNewEnvTestRule;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Xiangyue Cai
 */
public class SingleVMEhcachePortalCacheManagerConfiguratorTest
	extends BaseEhcachePortalCacheManagerConfiguratorTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			AspectJNewEnvTestRule.INSTANCE,
			new CodeCoverageAssertor() {

				@Override
				public void appendAssertClasses(List<Class<?>> assertClasses) {
					assertClasses.add(
						BaseEhcachePortalCacheManagerConfigurator.class);
				}

			});

	@AdviseWith(adviceClasses = UnsyncStringReaderAdvice.class)
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testParsePropertiesException() {
		SingleVMEhcachePortalCacheManagerConfigurator
			singleVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					null);

		try {
			singleVMEhcachePortalCacheManagerConfigurator.parseProperties(
				"key1=value1", StringPool.COMMA);

			Assert.fail("RuntimeException was not thrown");
		}
		catch (RuntimeException re) {
			Assert.assertSame(_IO_EXCEPTION, re.getCause());
		}
	}

	@Test
	public void testSetProps() {
		Props props = ProxyFactory.newDummyInstance(Props.class);
		SingleVMEhcachePortalCacheManagerConfigurator
			singleVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					null);

		singleVMEhcachePortalCacheManagerConfigurator.setProps(props);

		Assert.assertSame(
			props, singleVMEhcachePortalCacheManagerConfigurator.props);
	}

	@Aspect
	public static class UnsyncStringReaderAdvice {

		@Around(
			"execution(public int com.liferay.portal.kernel.io.unsync." +
				"UnsyncStringReader.read(char[]))"
		)
		public Object read() throws IOException {
			throw _IO_EXCEPTION;
		}

	}

	@Override
	protected <T extends BaseEhcachePortalCacheManagerConfigurator> T
		getBaseEhcachePortalCacheManagerConfigurator(
			Map<String, Object> propertie) {

		return (T) new SingleVMEhcachePortalCacheManagerConfigurator();
	}

	private static final IOException _IO_EXCEPTION = new IOException();

}