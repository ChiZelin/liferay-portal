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

package com.liferay.petra.lang;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class ClassLoaderPoolTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_classLoaders = ReflectionTestUtil.getFieldValue(
			ClassLoaderPool.class, "_classLoaders");

		_contextNames = ReflectionTestUtil.getFieldValue(
			ClassLoaderPool.class, "_contextNames");

		_fallbackClassLoaders = ReflectionTestUtil.getFieldValue(
			ClassLoaderPool.class, "_fallbackClassLoaders");

		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		_versionConstructor = clazz.getDeclaredConstructor(
			int.class, int.class, int.class, String.class);

		_versionConstructor.setAccessible(true);
	}

	@Before
	public void setUp() {
		_clear();
	}

	@Test
	public void testConstructor() {
		new ClassLoaderPool();
	}

	@Test
	public void testGetClassLoaderWithInvalidContextName() {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		Assert.assertSame(
			contextClassLoader, ClassLoaderPool.getClassLoader("null"));
		Assert.assertSame(
			contextClassLoader, ClassLoaderPool.getClassLoader(null));
		Assert.assertSame(
			contextClassLoader,
			ClassLoaderPool.getClassLoader("symbolic.name_"));
	}

	@Test
	public void testGetClassLoaderWithSymbolicNameAndVersion() {
		ClassLoader classLoader = new URLClassLoader(new URL[0]);

		// Test case 1

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader);

		_classLoaders.clear();

		Thread currentThread = Thread.currentThread();

		Assert.assertSame(
			currentThread.getContextClassLoader(),
			ClassLoaderPool.getClassLoader(_CONTEXT_NAME));

		// Test case 2

		ClassLoaderPool.register("symbolic.name1_1.0.0", classLoader);

		_classLoaders.clear();

		Assert.assertSame(
			classLoader,
			ClassLoaderPool.getClassLoader("symbolic.name1_1.0.0"));

		ConcurrentNavigableMap<Object, Object> classLoaders =
			_fallbackClassLoaders.get("symbolic.name1");

		classLoaders.clear();

		Assert.assertSame(
			currentThread.getContextClassLoader(),
			ClassLoaderPool.getClassLoader("symbolic.name1_1.0.0"));

		// Test case 3

		Assert.assertSame(
			currentThread.getContextClassLoader(),
			ClassLoaderPool.getClassLoader("symbolic.name2_1.0.0"));

		// Test case 4

		_clear();

		ClassLoaderPool.register(
			"symbolic.name1_1.0.0", new URLClassLoader(new URL[0]));
		ClassLoaderPool.register("symbolic.name1_2.0.0", classLoader);

		Assert.assertSame(
			classLoader,
			ClassLoaderPool.getClassLoader("symbolic.name1_3.0.0"));
	}

	@Test
	public void testGetClassLoaderWithValidContextName() {
		ClassLoader classLoader = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader);

		Assert.assertSame(
			classLoader, ClassLoaderPool.getClassLoader(_CONTEXT_NAME));
	}

	@Test
	public void testGetContextNameWithInvalidClassLoader() {
		ClassLoader classLoader = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader);

		Assert.assertEquals(
			"null",
			ClassLoaderPool.getContextName(new URLClassLoader(new URL[0])));
		Assert.assertEquals("null", ClassLoaderPool.getContextName(null));
	}

	@Test
	public void testGetContextNameWithValidClassLoader() {
		ClassLoader classLoader = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader);

		Assert.assertEquals(
			_CONTEXT_NAME, ClassLoaderPool.getContextName(classLoader));
	}

	@Test
	public void testParse() throws Exception {
		Method method = ReflectionTestUtil.getMethod(
			ClassLoaderPool.class, "_parseVersion", String.class);

		_assertEquals("1.0.0", method.invoke(null, "1"));
		_assertEquals("1.0.0", method.invoke(null, "1.0"));
		_assertEquals("1.0.0.0", method.invoke(null, "1.0.0.0"));
		_assertEquals("1.0.0.Aa0_-", method.invoke(null, "1.0.0.Aa0_-"));

		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "1.x.0"));
		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "-1.0.0"));
		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "1.-1.0"));
		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "1.0.-1"));
		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "1.0.0.~"));
		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "1.0."));
		Assert.assertNull(
			"null should be return because of invalid version",
			method.invoke(null, "1.0.0." + (char)128));
	}

	@Test
	public void testRegister() throws Exception {

		//Test case 1

		ClassLoader classLoader1 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader1);

		Assert.assertEquals(_contextNames.toString(), 1, _contextNames.size());
		Assert.assertEquals(_classLoaders.toString(), 1, _classLoaders.size());
		Assert.assertSame(classLoader1, _classLoaders.get(_CONTEXT_NAME));
		Assert.assertEquals(_CONTEXT_NAME, _contextNames.get(classLoader1));
		Assert.assertEquals(
			_fallbackClassLoaders.toString(), 0, _fallbackClassLoaders.size());

		//Test case 2

		_clear();

		ClassLoader classLoader2 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic_name", classLoader2);

		Assert.assertEquals(_contextNames.toString(), 1, _contextNames.size());
		Assert.assertEquals(_classLoaders.toString(), 1, _classLoaders.size());
		Assert.assertSame(classLoader2, _classLoaders.get("symbolic_name"));
		Assert.assertEquals("symbolic_name", _contextNames.get(classLoader2));

		Assert.assertEquals(
			_fallbackClassLoaders.toString(), 0, _fallbackClassLoaders.size());

		//Test case 3

		_clear();

		ClassLoader classLoader3 = new URLClassLoader(new URL[0]);
		ClassLoader classLoader4 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic.name_1.0.0", classLoader3);
		ClassLoaderPool.register("symbolic.name_1.0.0", classLoader4);

		ConcurrentNavigableMap<Object, Object> concurrentNavigableMap =
			_fallbackClassLoaders.get("symbolic.name");

		Assert.assertEquals(
			concurrentNavigableMap.toString(), 1,
			concurrentNavigableMap.size());

		Assert.assertSame(
			classLoader4,
			concurrentNavigableMap.get(
				_versionConstructor.newInstance(1, 0, 0, "")));

		//Test case 4

		_clear();

		ClassLoader classLoader5 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic.name_", classLoader5);

		Assert.assertEquals(_contextNames.toString(), 1, _contextNames.size());
		Assert.assertEquals(_classLoaders.toString(), 1, _classLoaders.size());
		Assert.assertSame(classLoader5, _classLoaders.get("symbolic.name_"));
		Assert.assertEquals("symbolic.name_", _contextNames.get(classLoader5));

		Assert.assertEquals(
			_fallbackClassLoaders.toString(), 0, _fallbackClassLoaders.size());
	}

	@Test(expected = NullPointerException.class)
	public void testRegisterWithNullClassLoader() {
		ClassLoaderPool.register("null", null);
	}

	@Test(expected = NullPointerException.class)
	public void testRegisterWithNullContextName() {
		ClassLoaderPool.register(null, null);
	}

	@Test
	public void testUnregisterWithInvalidClassLoader() {
		ClassLoaderPool.unregister(new URLClassLoader(new URL[0]));

		_assertEmptyMaps();
	}

	@Test
	public void testUnregisterWithInvalidContextName() {
		//Test case 1

		ClassLoaderPool.unregister(_CONTEXT_NAME);

		_assertEmptyMaps();

		//Test case 2

		ClassLoaderPool.unregister("symbolic.name_1.0.0");

		_assertEmptyMaps();
	}

	@Test
	public void testUnregisterWithValidClassLoader() throws Exception {
		ClassLoader classLoader1 = new URLClassLoader(new URL[0]);

		//Test case 1

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader1);

		ClassLoaderPool.unregister(classLoader1);

		_assertEmptyMaps();

		//Test case 2

		ClassLoaderPool.register("symbolic.name_1.0.0", classLoader1);

		ClassLoaderPool.unregister(classLoader1);

		_assertEmptyMaps();

		//Test case 3

		ClassLoader classLoader2 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic.name_1.0.0", classLoader1);
		ClassLoaderPool.register("symbolic.name_2.0.0", classLoader2);

		ClassLoaderPool.unregister(classLoader1);

		ConcurrentNavigableMap<Object, Object> concurrentNavigableMap =
			_fallbackClassLoaders.get("symbolic.name");

		Assert.assertEquals(
			concurrentNavigableMap.toString(), 1,
			concurrentNavigableMap.size());

		Assert.assertSame(
			classLoader2,
			concurrentNavigableMap.get(
				_versionConstructor.newInstance(2, 0, 0, "")));
	}

	@Test
	public void testUnregisterWithValidContextName() {
		//Test case 1

		ClassLoader classLoader1 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader1);

		ClassLoaderPool.unregister(_CONTEXT_NAME);

		_assertEmptyMaps();

		//Test case 2

		ClassLoader classLoader2 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic_name", classLoader2);

		ClassLoaderPool.unregister("symbolic_name");

		_assertEmptyMaps();

		//Test case 3

		ClassLoader classLoader3 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic.name_", classLoader3);

		ClassLoaderPool.unregister("symbolic.name_");

		_assertEmptyMaps();
	}

	@Test
	public void testVersionCompareTo() throws Exception {
		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Method method = ReflectionTestUtil.getMethod(clazz, "compareTo", clazz);

		Object version = _versionConstructor.newInstance(2, 1, 1, "");

		Assert.assertEquals(0, method.invoke(version, version));
		Assert.assertEquals(
			1,
			method.invoke(
				version, _versionConstructor.newInstance(1, 0, 0, "")));
		Assert.assertEquals(
			1,
			method.invoke(
				version, _versionConstructor.newInstance(2, 0, 0, "")));
		Assert.assertEquals(
			1,
			method.invoke(
				version, _versionConstructor.newInstance(2, 1, 0, "")));
		Assert.assertEquals(
			0,
			method.invoke(
				version, _versionConstructor.newInstance(2, 1, 1, "")));
	}

	private void _assertEmptyMaps() {
		Assert.assertTrue(_contextNames.toString(), _contextNames.isEmpty());
		Assert.assertTrue(_classLoaders.toString(), _classLoaders.isEmpty());
		Assert.assertTrue(
			_fallbackClassLoaders.toString(), _fallbackClassLoaders.isEmpty());
	}

	private void _assertEquals(String expectedVersion, Object version) {
		int major = ReflectionTestUtil.getFieldValue(version, "_major");
		int minor = ReflectionTestUtil.getFieldValue(version, "_minor");
		int micro = ReflectionTestUtil.getFieldValue(version, "_micro");

		String qualifier = ReflectionTestUtil.getFieldValue(
			version, "_qualifier");

		int length = qualifier.length();

		StringBuilder result = new StringBuilder(20 + length);

		result.append(major);
		result.append(".");
		result.append(minor);
		result.append(".");
		result.append(micro);

		if (length > 0) {
			result.append(".");
			result.append(qualifier);
		}

		Assert.assertEquals(expectedVersion, result.toString());
	}

	private void _clear() {
		_classLoaders.clear();

		_contextNames.clear();

		_fallbackClassLoaders.clear();
	}

	private static final String _CONTEXT_NAME = "contextName";

	private static Map<String, ClassLoader> _classLoaders;
	private static Map<ClassLoader, String> _contextNames;
	private static Map<String, ConcurrentNavigableMap<Object, Object>>
		_fallbackClassLoaders;
	private static Constructor<?> _versionConstructor;

}