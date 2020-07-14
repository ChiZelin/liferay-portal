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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class ClassLoaderPoolTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() {
		_classLoaders = ReflectionTestUtil.getFieldValue(
			ClassLoaderPool.class, "_classLoaders");

		_classLoaders.clear();

		_contextNames = ReflectionTestUtil.getFieldValue(
			ClassLoaderPool.class, "_contextNames");

		_contextNames.clear();

		_fallbackClassLoaders = ReflectionTestUtil.getFieldValue(
			ClassLoaderPool.class, "_fallbackClassLoaders");

		_fallbackClassLoaders.clear();
	}

	@Test
	public void testConstructor() {
		new ClassLoaderPool();
	}

	@Test
	public void testGetClassLoaderWithInvalidContextName() {
		ClassLoader classLoader = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader);

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		Assert.assertSame(
			contextClassLoader, ClassLoaderPool.getClassLoader("null"));
		Assert.assertSame(
			contextClassLoader, ClassLoaderPool.getClassLoader(null));
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
	public void testRegister() throws Exception {

		//Test case 1

		ClassLoader classLoader1 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader1);

		Assert.assertEquals(_contextNames.toString(), 1, _contextNames.size());
		Assert.assertEquals(_classLoaders.toString(), 1, _classLoaders.size());
		Assert.assertSame(classLoader1, _classLoaders.get(_CONTEXT_NAME));
		Assert.assertEquals(_CONTEXT_NAME, _contextNames.get(classLoader1));

		//Test case 2

		ClassLoader classLoader2 = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register("symbolic.name_1.0.0", classLoader1);
		ClassLoaderPool.register("symbolic.name_1.0.0", classLoader2);

		ConcurrentNavigableMap<Object, Object> concurrentNavigableMap =
			_fallbackClassLoaders.get("symbolic.name");

		Assert.assertEquals(
			concurrentNavigableMap.toString(), 1,
			concurrentNavigableMap.size());

		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		Assert.assertSame(
			classLoader2,
			concurrentNavigableMap.get(constructor.newInstance("1.0.0")));
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

		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		Assert.assertSame(
			classLoader2,
			concurrentNavigableMap.get(constructor.newInstance("2.0.0")));
	}

	@Test
	public void testUnregisterWithValidContextName() {
		ClassLoader classLoader = new URLClassLoader(new URL[0]);

		ClassLoaderPool.register(_CONTEXT_NAME, classLoader);

		ClassLoaderPool.unregister(_CONTEXT_NAME);

		_assertEmptyMaps();
	}

	@Test
	public void testVersionCompareTo() throws Exception {
		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		Method method = ReflectionTestUtil.getMethod(clazz, "compareTo", clazz);

		Object version = constructor.newInstance("2.1.1");

		Assert.assertEquals(0, method.invoke(version, version));
		Assert.assertEquals(
			1, method.invoke(version, constructor.newInstance("1.0.0")));
		Assert.assertEquals(
			1, method.invoke(version, constructor.newInstance("2.0.0")));
		Assert.assertEquals(
			1, method.invoke(version, constructor.newInstance("2.1.0")));
		Assert.assertEquals(
			0, method.invoke(version, constructor.newInstance("2.1.1")));
	}

	@Test
	public void testVersionConstructor() throws Exception {
		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		//Test case 1

		Object version1 = constructor.newInstance("1");

		Assert.assertEquals("1.0.0", version1.toString());

		//Test case 2

		Object version2 = constructor.newInstance("1.0");

		Assert.assertEquals("1.0.0", version2.toString());

		//Test case 3

		Object version3 = constructor.newInstance("1.0.0.0");

		Assert.assertEquals("1.0.0.0", version3.toString());

		//Test case 4

		Object version4 = constructor.newInstance("1.0.0.Aa0_-");

		Assert.assertEquals("1.0.0.Aa0_-", version4.toString());

		//Test case 5

		try {
			constructor.newInstance("1.x.0");

			Assert.fail(
				"InvocationTargetException should be thrown because of " +
					"invalid version \"1.x.0\": non-numeric \"x\"");
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			Assert.assertEquals(
				"invalid version \"1.x.0\": non-numeric \"x\"",
				throwable.getMessage());
		}

		//Test case 6

		try {
			constructor.newInstance("-1.0.0");

			Assert.fail(
				"InvocationTargetException should be thrown because of " +
					"invalid version \"-1.0.0\": negative number \"-1\"");
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			Assert.assertEquals(
				"invalid version \"-1.0.0\": negative number \"-1\"",
				throwable.getMessage());
		}

		//Test case 7

		try {
			constructor.newInstance("1.-1.0");

			Assert.fail(
				"InvocationTargetException should be thrown because of " +
					"invalid version \"1.-1.0\": negative number \"-1\"");
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			Assert.assertEquals(
				"invalid version \"1.-1.0\": negative number \"-1\"",
				throwable.getMessage());
		}

		//Test case 8

		try {
			constructor.newInstance("1.0.-1");

			Assert.fail(
				"InvocationTargetException should be thrown because of " +
					"invalid version \"1.0.-1\": negative number \"-1\"");
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			Assert.assertEquals(
				"invalid version \"1.0.-1\": negative number \"-1\"",
				throwable.getMessage());
		}

		//Test case 9

		try {
			constructor.newInstance("1.0.0.~");

			Assert.fail(
				"InvocationTargetException should be thrown because of " +
					"invalid version \"1.0.0.~\": invalid qualifier \"~\"");
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			Assert.assertEquals(
				"invalid version \"1.0.0.~\": invalid qualifier \"~\"",
				throwable.getMessage());
		}

		//Test case 10

		try {
			constructor.newInstance("1.0.");

			Assert.fail(
				"InvocationTargetException should be thrown because of " +
					"invalid version \"1.0.\": invalid format");
		}
		catch (InvocationTargetException invocationTargetException) {
			Throwable throwable = invocationTargetException.getCause();

			Assert.assertEquals(
				"invalid version \"1.0.\": invalid format",
				throwable.getMessage());
		}
	}

	@Test
	public void testVersionEquals() throws Exception {
		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		Object version = constructor.newInstance("1.0.0");

		Method method = ReflectionTestUtil.getMethod(
			clazz, "equals", Object.class);

		Assert.assertTrue(
			"Version instance 1.0.0 equals itself",
			(boolean)method.invoke(version, version));
		Assert.assertFalse(
			"Version instance 1.0.0 does not equals String instance 1.0.0",
			(boolean)method.invoke(version, "1.0.0"));
		Assert.assertFalse(
			"Version instance 1.0.0 does not equals Version instance 2.0.0",
			(boolean)method.invoke(version, constructor.newInstance("2.0.0")));
		Assert.assertFalse(
			"Version instance 1.0.0 does not equals Version instance 1.1.0",
			(boolean)method.invoke(version, constructor.newInstance("1.1.0")));
		Assert.assertFalse(
			"Version instance 1.0.0 does not equals Version instance 1.0.1",
			(boolean)method.invoke(version, constructor.newInstance("1.0.1")));
		Assert.assertFalse(
			"Version instance 1.0.0 does not equals Version instance 1.0.0.1",
			(boolean)method.invoke(
				version, constructor.newInstance("1.0.0.1")));
		Assert.assertTrue(
			"Version instance 1.0.0 equals Version instance 1.0.0",
			(boolean)method.invoke(version, constructor.newInstance("1.0.0")));
	}

	@Test
	public void testVersionHashCode() throws Exception {
		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		Object version = constructor.newInstance("1.0.0");

		Method method = ReflectionTestUtil.getMethod(clazz, "hashCode");

		Assert.assertEquals(
			0, (int)ReflectionTestUtil.getFieldValue(version, "_hash"));

		int result = (int)method.invoke(version);

		Assert.assertNotEquals(
			0, (int)ReflectionTestUtil.getFieldValue(version, "_hash"));

		Assert.assertEquals(result, method.invoke(version));
	}

	@Test
	public void testVersionToString() throws Exception {
		Class<?> clazz = Class.forName(
			"com.liferay.petra.lang.ClassLoaderPool$Version");

		Constructor<?> constructor = clazz.getConstructor(String.class);

		Method method = ReflectionTestUtil.getMethod(clazz, "toString");

		//Test case 1

		Object version1 = constructor.newInstance("1.0.0");

		String versionString = ReflectionTestUtil.getFieldValue(
			version1, "_versionString");

		Assert.assertNull(versionString);

		Assert.assertEquals("1.0.0", method.invoke(version1));

		versionString = ReflectionTestUtil.getFieldValue(
			version1, "_versionString");

		Assert.assertEquals("1.0.0", versionString);

		Assert.assertEquals("1.0.0", method.invoke(version1));

		//Test case 2

		Object version2 = constructor.newInstance("1.0.0.0");

		Assert.assertEquals("1.0.0.0", method.invoke(version2));
	}

	private void _assertEmptyMaps() {
		Assert.assertTrue(_contextNames.toString(), _contextNames.isEmpty());
		Assert.assertTrue(_classLoaders.toString(), _classLoaders.isEmpty());
	}

	private static final String _CONTEXT_NAME = "contextName";

	private static Map<String, ClassLoader> _classLoaders;
	private static Map<ClassLoader, String> _contextNames;
	private static Map<String, ConcurrentNavigableMap<Object, Object>>
		_fallbackClassLoaders;

}