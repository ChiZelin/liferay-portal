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
import com.liferay.portal.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.Collections;
import java.util.HashMap;
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
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
						}
					});

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
			new Properties(),
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_replicatorProperties"));
		Assert.assertNull(
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_defaultBootstrapLoaderPropertiesString"));
		Assert.assertNull(
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_defaultReplicatorPropertiesString"));
	}

	@Test
	public void testGetMergedPropertiesMap() {

		// Test 1: _bootstrapLoaderEnabled is true, _bootstrapLoaderProperties
		// and _replicatorProperties are empty

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap1 = ReflectionTestUtil.invoke(
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
						}

					}),
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
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
										StringPool.PERIOD,
								_properties.clone());
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}

					}),
				"_getMergedPropertiesMap", null, null);

		Assert.assertEquals(
			mergedPropertiesMap2.toString(), 1, mergedPropertiesMap2.size());

		_assertMergedPropertiesMap(
			"portalCacheName",
			new Properties() {
				{
					put("key", "value");
				}
			},
			new Properties() {
				{
					put("key", "value");
					put("replicator", true);
				}
			},
			mergedPropertiesMap2);

		// Test 3: _bootstrapLoaderEnabled is false, _bootstrapLoaderProperties
		// and _replicatorProperties are non-empty

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap3 = ReflectionTestUtil.invoke(
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED,
								"false");
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}

					}),
				"_getMergedPropertiesMap", null, null);

		Assert.assertEquals(
			mergedPropertiesMap3.toString(), 1, mergedPropertiesMap3.size());

		_assertMergedPropertiesMap(
			"portalCacheName", null,
			new Properties() {
				{
					put("key", "value");
					put("replicator", true);
				}
			},
			mergedPropertiesMap3);
	}

	@Test
	public void testGetPortalPropertiesString() {
		_testGetPortalPropertiesString(null, new String[0]);
		_testGetPortalPropertiesString("key=value", new String[] {"key=value"});
		_testGetPortalPropertiesString(
			"key1=value1,key2=value2",
			new String[] {"key1=value1", "key2=value2"});
	}

	@Override
	@Test
	public void testIsRequireSerialization() {
		super.testIsRequireSerialization();

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
						}
					});

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
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(PropsKeys.CLUSTER_LINK_ENABLED, "false");
						}
					});

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
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
										StringPool.PERIOD,
								_properties.clone());
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}
					});

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
			portalCacheManagerConfiguration1, "portalCacheName",
			new Properties() {
				{
					put("key", "value");
				}
			},
			new Properties() {
				{
					put("key", "value");
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
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}
					});

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
			portalCacheManagerConfiguration2, "portalCacheName", null,
			new Properties() {
				{
					put("key", "value");
					put("replicator", true);
				}
			});

		// Test 4: clusterEnabled is true, _bootstrapLoaderEnabled is false,
		// _bootstrapLoaderProperties is empty, _replicatorProperties is
		// non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator4 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED,
								"false");
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}
					});

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
			portalCacheManagerConfiguration3, "portalCacheName", null,
			new Properties() {
				{
					put("key", "value");
					put("replicator", true);
				}
			});

		// Test 5: clusterEnabled is true, _bootstrapLoaderEnabled is true,
		// _bootstrapLoaderProperties is non-empty, _replicatorProperties is
		// empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator5 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
										StringPool.PERIOD,
								_properties.clone());
						}
					});

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
			portalCacheManagerConfiguration4, "portalCacheName",
			new Properties() {
				{
					put("key", "value");
				}
			},
			new Properties[0]);

		// Test 6: clusterEnabled is true, _bootstrapLoaderEnabled is true,
		// _bootstrapLoaderProperties and _replicatorProperties are non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator6 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
										StringPool.PERIOD,
								_properties.clone());
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}
					});

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

		portalCacheManagerConfiguration5.putPortalCacheConfiguration(
			"portalCacheName",
			new PortalCacheConfiguration(
				"portalCacheName", new HashSet<>(), null));

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
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
						}
					});

		_assertPortalCacheConfiguration(
			"portalCacheNameOutsideProperties", new Properties(),
			Collections.singleton(
				new Properties() {
					{
						put("replicator", true);
					}
				}),
			multiVMEhcachePortalCacheManagerConfigurator1.
				parseCacheListenerConfigurations(
					new CacheConfiguration(
						"portalCacheNameOutsideProperties", 0),
					null, true));

		// Test 3: clusterEnabled is true, _bootstrapLoaderEnabled is false,
		// _bootstrapLoaderProperties and _replicatorProperties are empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator2 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED,
								"false");
						}
					});

		_assertPortalCacheConfiguration(
			"portalCacheNameOutsideProperties", null,
			Collections.singleton(
				new Properties() {
					{
						put("replicator", true);
					}
				}),
			multiVMEhcachePortalCacheManagerConfigurator2.
				parseCacheListenerConfigurations(
					new CacheConfiguration(
						"portalCacheNameOutsideProperties", 0),
					null, true));

		// Test 4: clusterEnabled and _bootstrapLoaderEnabled are true,
		// _bootstrapLoaderProperties and _replicatorProperties are non-empty

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator3 =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);

							put(
								PropsKeys.
									EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
										StringPool.PERIOD,
								_properties.clone());
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
									StringPool.PERIOD,
								_properties.clone());
						}
					});

		PortalCacheConfiguration portalCacheConfiguration =
			multiVMEhcachePortalCacheManagerConfigurator3.
				parseCacheListenerConfigurations(
					new CacheConfiguration("portalCacheName", 0), null, true);

		Properties bootstrapLoaderProperties = ReflectionTestUtil.getFieldValue(
			multiVMEhcachePortalCacheManagerConfigurator3,
			"_bootstrapLoaderProperties");
		Properties replicatorProperties = ReflectionTestUtil.getFieldValue(
			multiVMEhcachePortalCacheManagerConfigurator3,
			"_replicatorProperties");

		Assert.assertNull(bootstrapLoaderProperties.get("portalCacheName"));
		Assert.assertNull(replicatorProperties.get("portalCacheName"));

		_assertPortalCacheConfiguration(
			"portalCacheName",
			new Properties() {
				{
					put("key", "value");
				}

			},
			Collections.singleton(
				new Properties() {
					{
						put("key", "value");
						put("replicator", true);
					}
				}),
			portalCacheConfiguration);
	}

	@Test
	public void testSetProps() {
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		Props dummyProps = ProxyFactory.newDummyInstance(Props.class);

		multiVMEhcachePortalCacheManagerConfigurator.setProps(dummyProps);

		Assert.assertSame(
			dummyProps,
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator, "props"));
	}

	@Override
	protected MultiVMEhcachePortalCacheManagerConfigurator
		getBaseEhcachePortalCacheManagerConfigurator(
			Map<String, Object> propertie) {

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		if (propertie != null) {
			multiVMEhcachePortalCacheManagerConfigurator.setProps(
				PropsTestUtil.setProps(propertie));

			multiVMEhcachePortalCacheManagerConfigurator.activate();
		}

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

	private void _testGetPortalPropertiesString(
		String expectedString, String[] array) {

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(
					new HashMap<String, Object>() {
						{
							putAll(_propertiesMap);
							put(
								PropsKeys.EHCACHE_REPLICATOR_PROPERTIES_DEFAULT,
								array);
						}
					});

		Assert.assertEquals(
			expectedString,
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString(
					PropsKeys.EHCACHE_REPLICATOR_PROPERTIES_DEFAULT));
	}

	private final Properties _properties = new Properties() {
		{
			put("portalCacheName", "key=value");
		}
	};
	private final Map<String, Object> _propertiesMap = new HashMap() {
		{
			put(PropsKeys.CLUSTER_LINK_ENABLED, "true");
			put(PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED, "true");
			put(
				PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
					StringPool.PERIOD,
				new Properties());
			put(
				PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
					StringPool.PERIOD,
				new Properties());
			put(
				PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES_DEFAULT,
				new String[0]);
			put(PropsKeys.EHCACHE_REPLICATOR_PROPERTIES_DEFAULT, new String[0]);
		}
	};

}