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

package com.liferay.portal.kernel.util;

import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.petra.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.petra.process.ClassPathUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.net.URLClassLoader;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class MethodKeyTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() {
		_methodKey = new MethodKey(
			TestClass.class, "testMethod", String.class);
	}

	@Test
	public void testConstructors() throws NoSuchMethodException {
		Assert.assertEquals(TestClass.class, _methodKey.getDeclaringClass());
		Assert.assertEquals("testMethod", _methodKey.getMethodName());
		Assert.assertEquals(String.class, _methodKey.getParameterTypes()[0]);

		MethodKey methodKey = new MethodKey();

		Assert.assertNull(methodKey.getDeclaringClass());
		Assert.assertNull(methodKey.getMethodName());
		Assert.assertNull(methodKey.getParameterTypes());

		Method method = TestClass.class.getMethod("testMethod", String.class);

		methodKey = new MethodKey(method);

		Assert.assertEquals(TestClass.class, methodKey.getDeclaringClass());
		Assert.assertEquals("testMethod", methodKey.getMethodName());
		Assert.assertEquals(String.class, methodKey.getParameterTypes()[0]);

		methodKey = new MethodKey(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestClass1",
			"testMethod", String.class);

		Assert.assertEquals(TestClass.class, methodKey.getDeclaringClass());
		Assert.assertEquals("testMethod", methodKey.getMethodName());
		Assert.assertEquals(String.class, methodKey.getParameterTypes()[0]);

		try {
			new MethodKey("ClassNotFound", "testMethod", String.class);

			Assert.fail("No RuntimeException thrown!");
		}
		catch (RuntimeException re) {
			Assert.assertTrue(re.getCause() instanceof ClassNotFoundException);
		}
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(_methodKey.equals(_methodKey));

		Object object = new Object();

		Assert.assertFalse(_methodKey.equals(object));

		MethodKey methodKey = new MethodKey(
			TestClass.class, "testMethod", String.class);

		Assert.assertTrue(_methodKey.equals(methodKey));

		methodKey = new MethodKey(TestClass.class, "testMethod", int.class);

		Assert.assertFalse(_methodKey.equals(methodKey));

		methodKey = new MethodKey(
			TestClass.class, "anotherTestMethod", String.class);

		Assert.assertFalse(_methodKey.equals(methodKey));

		methodKey = new MethodKey(Object.class, "testMethod", String.class);

		Assert.assertFalse(_methodKey.equals(methodKey));
	}

	@Test
	public void testGetMethodAndResetCache() throws NoSuchMethodException {
		Map<MethodKey, Method> methods = ReflectionTestUtil.getFieldValue(
			MethodKey.class, "_methods");

		// Test 1, MethodKey.getMethod returns and caches the method

		Method expectedMethod = TestClass.class.getMethod(
			"testMethod", String.class);

		Method actualMethod1 = _methodKey.getMethod();

		Assert.assertEquals(expectedMethod, actualMethod1);
		Assert.assertTrue(actualMethod1.isAccessible());

		Assert.assertEquals(methods.toString(), 1, methods.size());

		// Test 2, method is retrieved from cache and set accessible

		actualMethod1.setAccessible(false);

		Method actualMethod2 = _methodKey.getMethod();

		Assert.assertSame(actualMethod2, actualMethod1);
		Assert.assertTrue(actualMethod2.isAccessible());

		Assert.assertEquals(methods.toString(), 1, methods.size());

		// Test 3, resetCache() clears the method cache

		MethodKey.resetCache();

		Assert.assertEquals(methods.toString(), 0, methods.size());
	}

	@Test
	public void testHashCode() throws NoSuchMethodException {
		Method method = TestClass.class.getMethod("testMethod", String.class);

		Assert.assertEquals(method.hashCode(), _methodKey.hashCode());
	}

	@Test
	public void testToString() {
		Assert.assertEquals(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestClass1." +
				"testMethod(java.lang.String)",
			_methodKey.toString());
		Assert.assertEquals(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestClass1." +
				"testMethod(java.lang.String)",
			ReflectionTestUtil.getFieldValue(_methodKey, "_toString"));

		ReflectionTestUtil.setFieldValue(_methodKey, "_toString", "testString");

		Assert.assertEquals("testString", _methodKey.toString());
	}

	@Test
	public void testTransform() throws Exception {
		ClassLoader newClassLoader = new URLClassLoader(
			ClassPathUtil.getClassPathURLs(ClassPathUtil.getJVMClassPath(true)),
			null);

		Class<?> methodKeyTestClazz = newClassLoader.loadClass(
			"com.liferay.portal.kernel.util.MethodKeyTest");
		Class<?> testClass1Clazz = newClassLoader.loadClass(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestClass");

		Constructor constructor = testClass1Clazz.getDeclaredConstructor(
			methodKeyTestClazz);

		constructor.setAccessible(true);

		Object testClass1Instance = constructor.newInstance(
			methodKeyTestClazz.newInstance());

		Method method = _methodKey.getMethod();

		try {
			method.invoke(testClass1Instance, "test");

			Assert.fail("No IllegalArgumentException thrown!");
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"object is not an instance of declaring class",
				iae.getMessage());
		}

		MethodKey methodKey = _methodKey.transform(newClassLoader);

		method = methodKey.getMethod();

		Assert.assertEquals("test", method.invoke(testClass1Instance, "test"));
	}

	@Test
	public void testWriteAndReadExternal()
		throws ClassNotFoundException, IOException {

		MethodKey methodKey = new MethodKey();

		try (UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream()) {

			try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					unsyncByteArrayOutputStream)) {

				_methodKey.writeExternal(objectOutputStream);
			}

			try (UnsyncByteArrayInputStream unsyncByteArrayInputStream =
					new UnsyncByteArrayInputStream(
						unsyncByteArrayOutputStream.unsafeGetByteArray(), 0,
						unsyncByteArrayOutputStream.size());
				ObjectInputStream objectInputStream = new ObjectInputStream(
					unsyncByteArrayInputStream)) {

				methodKey.readExternal(objectInputStream);
			}
		}

		Assert.assertTrue(_methodKey.equals(methodKey));
	}

	private MethodKey _methodKey;

	private class TestClass {

		public void testMethod(int parameter) {
		}

		public String testMethod(String parameter) {
			return parameter;
		}

		public void anotherTestMethod(String parameter) {
		}

	}

}