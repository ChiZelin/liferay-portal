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

import com.liferay.portal.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class MultiVMEhcachePortalCacheManagerConfiguratorTest
	extends BaseEhcachePortalCacheManagerConfiguratorTestCase {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testActivate() {
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, false, false));

		Assert.assertTrue(
			"The _bootstrapLoaderEnabled should be true if props.get(" +
				"PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED) return true",
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_bootstrapLoaderEnabled"));
		Assert.assertTrue(
			"The clusterEnabled should be true if props.get(PropsKeys." +
				"CLUSTER_LINK_ENABLED) return true",
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"clusterEnabled"));
		Assert.assertEquals(
			new Properties(),
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_bootstrapLoaderProperties"));
		Assert.assertEquals(
			"key1=value1,key2=value2",
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_defaultBootstrapLoaderPropertiesString"));
		Assert.assertEquals(
			"key1=value1,key2=value2",
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_defaultReplicatorPropertiesString"));
		Assert.assertEquals(
			new Properties(),
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_replicatorProperties"));
	}

	@Test
	public void testGetMergedPropertiesMap() {

		// Test 1: _bootstrapLoaderEnabled is true, _bootstrapLoaderProperties
		// and _replicatorProperties are empty

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap1 = ReflectionTestUtil.invoke(
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, false, false)),
				"_getMergedPropertiesMap", null, null);

		Assert.assertTrue(
			"The mergedPropertiesMap should be empty if " +
				"bootstrapLoaderProperties and replicatorProperties are empty",
			mergedPropertiesMap1.isEmpty());

		// Test 2: _bootstrapLoaderEnabled is true, _bootstrapLoaderProperties
		// and _replicatorProperties are non-empty

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap2 = ReflectionTestUtil.invoke(
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, true, true)),
				"_getMergedPropertiesMap", null, null);

		Assert.assertEquals(
			mergedPropertiesMap2.toString(), 3, mergedPropertiesMap2.size());

		_assertMergedPropertiesMap(
			"portalCacheName1",
			new Properties() {
				{
					put("key1", "value1");
				}
			},
			new Properties() {
				{
					put("key1", "value1");
					put("replicator", true);
				}
			},
			mergedPropertiesMap2);
		_assertMergedPropertiesMap(
			"portalCacheName2",
			new Properties() {
				{
					put("key2", "value2");
				}
			},
			null, mergedPropertiesMap2);
		_assertMergedPropertiesMap(
			"portalCacheName3", null,
			new Properties() {
				{
					put("key3", "value3");
					put("replicator", true);
				}
			},
			mergedPropertiesMap2);

		// Test 3: _bootstrapLoaderEnabled is false, _bootstrapLoaderProperties
		// and _replicatorProperties are non-empty

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap3 = ReflectionTestUtil.invoke(
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, false, true, true)),
				"_getMergedPropertiesMap", null, null);

		Assert.assertEquals(
			mergedPropertiesMap3.toString(), 2, mergedPropertiesMap3.size());

		_assertMergedPropertiesMap(
			"portalCacheName1", null,
			new Properties() {
				{
					put("key1", "value1");
					put("replicator", true);
				}
			},
			mergedPropertiesMap3);
		_assertMergedPropertiesMap(
			"portalCacheName3", null,
			new Properties() {
				{
					put("key3", "value3");
					put("replicator", true);
				}
			},
			mergedPropertiesMap3);
	}

	@Test
	public void testGetPortalPropertiesString() {
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, false, false));

		Assert.assertNull(
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString("portal.property.Key1"));
		Assert.assertEquals(
			"key=value",
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString("portal.property.Key2"));
		Assert.assertEquals(
			"key1=value1,key2=value2",
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString("portal.property.Key3"));
	}

	@Override
	@Test
	public void testIsRequireSerialization() {
		super.testIsRequireSerialization();

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true));

		Assert.assertTrue(
			"The true value should be returned if clusterEnabled is true",
			multiVMEhcachePortalCacheManagerConfigurator.isRequireSerialization(
				new CacheConfiguration()));
	}

	@Test
	public void testManageConfiguration() {

		// Test 1: clusterEnabled is false

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator1 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(false, true, false, false));

		final boolean[] calledGetDefaultPortalCacheConfiguration = {false};

		multiVMEhcachePortalCacheManagerConfigurator1.manageConfiguration(
			new Configuration(),
			new PortalCacheManagerConfiguration(null, null, null) {

				@Override
				public PortalCacheConfiguration
					getDefaultPortalCacheConfiguration() {

					calledGetDefaultPortalCacheConfiguration[0] = true;

					return null;
				}

			});

		Assert.assertFalse(
			"The manageConfiguration method should be returned directly if " +
				"clusterEnabled is false",
			calledGetDefaultPortalCacheConfiguration[0]);

		// Test 2: clusterEnabled is true, _bootstrapLoaderProperties and
		// _replicatorProperties are non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator2 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, true, true));

		Set<Properties> portalCacheListenerPropertiesSet = new HashSet<>();

		portalCacheListenerPropertiesSet.add(
			new Properties() {
				{
					put("replicator", true);
				}
			});

		portalCacheListenerPropertiesSet.add(
			new Properties() {
				{
					put("replicator", false);
				}
			});

		PortalCacheManagerConfiguration portalCacheManagerConfiguration1 =
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT,
					portalCacheListenerPropertiesSet, null),
				null);

		multiVMEhcachePortalCacheManagerConfigurator2.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration1);

		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration1, "portalCacheName1",
			new Properties() {
				{
					put("key1", "value1");
				}
			},
			new Properties() {
				{
					put("key1", "value1");
					put("replicator", true);
				}
			},
			new Properties() {
				{
					put("replicator", false);
				}
			});
		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration1, "portalCacheName2",
			new Properties() {
				{
					put("key2", "value2");
				}
			},
			new Properties() {
				{
					put("replicator", true);
				}
			},
			new Properties() {
				{
					put("replicator", false);
				}
			});
		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration1, "portalCacheName3", null,
			new Properties() {
				{
					put("key3", "value3");
					put("replicator", true);
				}
			},
			new Properties() {
				{
					put("replicator", false);
				}
			});

		// Test 3: clusterEnabled and _bootstrapLoaderEnabled are true,
		// _bootstrapLoaderProperties is empty, _replicatorProperties is
		// non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator3 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, false, true));

		PortalCacheManagerConfiguration portalCacheManagerConfiguration2 =
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null);

		multiVMEhcachePortalCacheManagerConfigurator3.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration2);

		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration2, "portalCacheName1", null,
			new Properties() {
				{
					put("key1", "value1");
					put("replicator", true);
				}
			});
		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration2, "portalCacheName3", null,
			new Properties() {
				{
					put("key3", "value3");
					put("replicator", true);
				}
			});

		// Test 4: clusterEnabled is true, _bootstrapLoaderEnabled is false,
		// _bootstrapLoaderProperties is empty, _replicatorProperties is
		// non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator4 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, false, false, true));

		PortalCacheManagerConfiguration portalCacheManagerConfiguration3 =
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null);

		multiVMEhcachePortalCacheManagerConfigurator4.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration3);

		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration3, "portalCacheName1", null,
			new Properties() {
				{
					put("key1", "value1");
					put("replicator", true);
				}
			});
		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration3, "portalCacheName3", null,
			new Properties() {
				{
					put("key3", "value3");
					put("replicator", true);
				}
			});

		// Test 5: clusterEnabled is true, _bootstrapLoaderEnabled is true,
		// _bootstrapLoaderProperties is non-empty, _replicatorProperties is
		// empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator5 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, true, false));

		PortalCacheManagerConfiguration portalCacheManagerConfiguration4 =
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null);

		multiVMEhcachePortalCacheManagerConfigurator5.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration4);

		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration4, "portalCacheName1",
			new Properties() {
				{
					put("key1", "value1");
				}
			},
			new Properties[0]);
		_assertPortalCacheManagerConfiguration(
			portalCacheManagerConfiguration4, "portalCacheName2",
			new Properties() {
				{
					put("key2", "value2");
				}
			},
			new Properties[0]);

		// Test 6: clusterEnabled is true, _bootstrapLoaderEnabled is true,
		// _bootstrapLoaderProperties and _replicatorProperties are non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator6 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, true, true));

		final boolean[] calledNewPortalCacheConfiguration = {false};

		PortalCacheConfiguration defaultPortalCacheConfiguration =
			new PortalCacheConfiguration(
				PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT,
				new HashSet<>(), null) {

				@Override
				public PortalCacheConfiguration newPortalCacheConfiguration(
					String portalCacheName) {

					calledNewPortalCacheConfiguration[0] = true;

					return null;
				}

			};

		PortalCacheManagerConfiguration portalCacheManagerConfiguration5 =
			new PortalCacheManagerConfiguration(
				null, defaultPortalCacheConfiguration, null);

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap = ReflectionTestUtil.invoke(
				multiVMEhcachePortalCacheManagerConfigurator6,
				"_getMergedPropertiesMap", null, null);

		for (Map.Entry<String, ObjectValuePair<Properties, Properties>> entry :
				mergedPropertiesMap.entrySet()) {

			String portalCacheName = entry.getKey();

			portalCacheManagerConfiguration5.putPortalCacheConfiguration(
				portalCacheName,
				new PortalCacheConfiguration(
					portalCacheName, new HashSet<Properties>(), null));
		}

		multiVMEhcachePortalCacheManagerConfigurator6.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration5);

		Assert.assertFalse(
			"The portalCacheConfiguration should be get from " +
				"portalCacheManagerConfiguration if it exists",
			calledNewPortalCacheConfiguration[0]);
	}

	@Override
	@Test
	public void testParseCacheListenerConfigurations() {

		// Test 1: clusterEnabled is false, _bootstrapLoaderEnabled is false,
		// _bootstrapLoaderProperties and _replicatorProperties are empty

		super.testParseCacheListenerConfigurations();

		// Test 2: clusterEnabled and _bootstrapLoaderEnabled are true,
		// _bootstrapLoaderProperties and _replicatorProperties are empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator1 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, false, false));

		_assertPortalCacheConfiguration(
			"portalCacheNameOutsideProperties",
			new Properties() {
				{
					put("key1", "value1");
					put("key2", "value2");
				}
			},
			Collections.singleton(
				new Properties() {
					{
						put("key1", "value1");
						put("key2", "value2");
						put("replicator", true);
					}
				}),
			multiVMEhcachePortalCacheManagerConfigurator1.
				parseCacheListenerConfigurations(
					new CacheConfiguration(
						"portalCacheNameOutsideProperties", 0),
					true));

		// Test 3: clusterEnabled is true, _bootstrapLoaderEnabled is false,
		// _bootstrapLoaderProperties and _replicatorProperties are empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator2 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true));

		_assertPortalCacheConfiguration(
			"portalCacheNameOutsideProperties", null,
			Collections.singleton(
				new Properties() {
					{
						put("key1", "value1");
						put("key2", "value2");
						put("replicator", true);
					}
				}),
			multiVMEhcachePortalCacheManagerConfigurator2.
				parseCacheListenerConfigurations(
					new CacheConfiguration(
						"portalCacheNameOutsideProperties", 0),
					true));

		// Test 4: clusterEnabled and _bootstrapLoaderEnabled are true,
		// _bootstrapLoaderProperties and _replicatorProperties are non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator3 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(true, true, true, true));

		PortalCacheConfiguration portalCacheConfiguration =
			multiVMEhcachePortalCacheManagerConfigurator3.
				parseCacheListenerConfigurations(
					new CacheConfiguration("portalCacheName1", 0), true);

		Properties bootstrapLoaderProperties = ReflectionTestUtil.getFieldValue(
			multiVMEhcachePortalCacheManagerConfigurator3,
			"_bootstrapLoaderProperties");
		Properties replicatorProperties = ReflectionTestUtil.getFieldValue(
			multiVMEhcachePortalCacheManagerConfigurator3,
			"_replicatorProperties");

		Assert.assertNull(bootstrapLoaderProperties.get("portalCacheName1"));
		Assert.assertNull(replicatorProperties.get("portalCacheName1"));

		_assertPortalCacheConfiguration(
			"portalCacheName1",
			new Properties() {
				{
					put("key1", "value1");
				}
			},
			Collections.singleton(
				new Properties() {
					{
						put("key1", "value1");
						put("replicator", true);
					}
				}),
			portalCacheConfiguration);
	}

	@Test
	public void testSetProps() {
		Props props = (Props)ProxyUtil.newProxyInstance(
			MultiVMEhcachePortalCacheManagerConfiguratorTest.
				class.getClassLoader(),
			new Class<?>[] {Props.class},
			new PropsInvocationHandler(true, true, false, false));

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		multiVMEhcachePortalCacheManagerConfigurator.setProps(props);

		Assert.assertSame(
			props,
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator, "props"));
	}

	protected MultiVMEhcachePortalCacheManagerConfigurator
		getBaseEhcachePortalCacheManagerConfigurator(
			PropsInvocationHandler propsInvocationHandler) {

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		multiVMEhcachePortalCacheManagerConfigurator.setProps(
			(Props)ProxyUtil.newProxyInstance(
				MultiVMEhcachePortalCacheManagerConfiguratorTest.
					class.getClassLoader(),
				new Class<?>[] {Props.class}, propsInvocationHandler));

		multiVMEhcachePortalCacheManagerConfigurator.activate();

		return multiVMEhcachePortalCacheManagerConfigurator;
	}

	private void _assertMergedPropertiesMap(
		String expectedPortalCacheName,
		Properties expectedBootstrapLoaderProperties,
		Properties expectedReplicatorProperties,
		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap) {

		ObjectValuePair objectValuePair = mergedPropertiesMap.get(
			expectedPortalCacheName);

		Assert.assertEquals(
			expectedBootstrapLoaderProperties, objectValuePair.getKey());
		Assert.assertEquals(
			expectedReplicatorProperties, objectValuePair.getValue());
	}

	private void _assertPortalCacheConfiguration(
		String expectedName, Properties expectedBootstrapLoaderProperties,
		Set<Properties> expectedPortalCacheListenerPropertiesSet,
		PortalCacheConfiguration portalCacheConfiguration) {

		Assert.assertEquals(
			expectedName, portalCacheConfiguration.getPortalCacheName());
		Assert.assertEquals(
			expectedBootstrapLoaderProperties,
			portalCacheConfiguration.getPortalCacheBootstrapLoaderProperties());
		Assert.assertEquals(
			expectedPortalCacheListenerPropertiesSet,
			portalCacheConfiguration.getPortalCacheListenerPropertiesSet());
	}

	private void _assertPortalCacheManagerConfiguration(
		PortalCacheManagerConfiguration portalCacheManagerConfiguration,
		String expectedPortalCacheName,
		Properties expectedPortalCacheBootstrapLoaderProperties,
		Properties... expectedPortalCacheListenerPropertiesArgs) {

		PortalCacheConfiguration portalCacheConfiguration =
			portalCacheManagerConfiguration.getPortalCacheConfiguration(
				expectedPortalCacheName);

		Set<Properties> expectedPortalCacheListenerPropertiesSet =
			new HashSet<>();

		for (Properties expectedPortalCacheListenerProperties :
				expectedPortalCacheListenerPropertiesArgs) {

			expectedPortalCacheListenerPropertiesSet.add(
				expectedPortalCacheListenerProperties);
		}

		_assertPortalCacheConfiguration(
			expectedPortalCacheName,
			expectedPortalCacheBootstrapLoaderProperties,
			expectedPortalCacheListenerPropertiesSet, portalCacheConfiguration);
	}

}