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

package com.liferay.portal.kernel.atom;

import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class AtomCollectionAdapterRegistryUtilTest {

	@BeforeClass
	public static void setUpClass() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_atomCollectionAdapter =
			(AtomCollectionAdapter)ProxyUtil.newProxyInstance(
				AtomCollectionAdapterRegistryUtilTest.class.getClassLoader(),
				new Class<?>[] {AtomCollectionAdapter.class},
				(proxy, method, args) -> {
					if ("getCollectionName".equals(method.getName())) {
						return _COLLECTION_NAME;
					}

					return null;
				});

		_serviceRegistration = registry.registerService(
			AtomCollectionAdapter.class, _atomCollectionAdapter);
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetAtomCollectionAdapter() {
		AtomCollectionAdapter<?> atomCollectionAdapter =
			AtomCollectionAdapterRegistryUtil.getAtomCollectionAdapter(
				_COLLECTION_NAME);

		Assert.assertSame(_atomCollectionAdapter, atomCollectionAdapter);
	}

	@Test
	public void testGetAtomCollectionAdapters() {
		List<AtomCollectionAdapter<?>> atomCollectionAdapters =
			AtomCollectionAdapterRegistryUtil.getAtomCollectionAdapters();

		Assert.assertEquals(
			atomCollectionAdapters.toString(), 1,
			atomCollectionAdapters.size());
		Assert.assertSame(
			_atomCollectionAdapter, atomCollectionAdapters.get(0));
	}

	private static final String _COLLECTION_NAME = "TestAtomCollectionAdapter";

	private static AtomCollectionAdapter<?> _atomCollectionAdapter;
	private static ServiceRegistration<AtomCollectionAdapter>
		_serviceRegistration;

}