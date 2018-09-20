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
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
			TestClass1.class, "testMethod", String.class);
	}

	@Test
	public void testConstructors() throws NoSuchMethodException {
		Assert.assertEquals(TestClass1.class, _methodKey.getDeclaringClass());
		Assert.assertEquals("testMethod", _methodKey.getMethodName());
		Assert.assertEquals(String.class, _methodKey.getParameterTypes()[0]);

		MethodKey methodKey = new MethodKey();

		Assert.assertNull(methodKey.getDeclaringClass());
		Assert.assertNull(methodKey.getMethodName());
		Assert.assertNull(methodKey.getParameterTypes());

		Method method = TestClass1.class.getMethod("testMethod", String.class);

		methodKey = new MethodKey(method);

		Assert.assertEquals(TestClass1.class, methodKey.getDeclaringClass());
		Assert.assertEquals("testMethod", methodKey.getMethodName());
		Assert.assertEquals(String.class, methodKey.getParameterTypes()[0]);

		methodKey = new MethodKey(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestMethodKey",
			"testMethod", String.class);

		Assert.assertEquals(TestClass1.class, methodKey.getDeclaringClass());
		Assert.assertEquals("testMethod", methodKey.getMethodName());
		Assert.assertEquals(String.class, methodKey.getParameterTypes()[0]);

		try {
			new MethodKey("ClassNotFound", "testMethod", String.class);

			Assert.fail("No RuntimeException throw!");
		}
		catch (RuntimeException re) {
			Throwable throwable = re.getCause();

			Assert.assertTrue(throwable instanceof ClassNotFoundException);
		}
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(_methodKey.equals(_methodKey));

		Object object = new Object();

		Assert.assertFalse(_methodKey.equals(object));

		MethodKey methodKey = new MethodKey(
			TestClass1.class, "testMethod", String.class);

		Assert.assertTrue(_methodKey.equals(methodKey));

		methodKey = new MethodKey(TestClass1.class, "testMethod", int.class);

		Assert.assertFalse(_methodKey.equals(methodKey));

		methodKey = new MethodKey(
			TestClass1.class, "testMethod1", String.class);

		Assert.assertFalse(_methodKey.equals(methodKey));

		methodKey = new MethodKey(TestClass2.class, "testMethod", String.class);

		Assert.assertFalse(_methodKey.equals(methodKey));
	}

	@Test
	public void testGetMethod()
		throws IllegalAccessException, InvocationTargetException,
			   NoSuchMethodException {

		Method expectedMethod = TestClass1.class.getMethod(
			"testMethod", String.class);

		Method actualMethod = _methodKey.getMethod();

		Assert.assertEquals(expectedMethod, actualMethod);

		TestClass1 testClass = new TestClass1();

		String result = (String)actualMethod.invoke(testClass, "test");

		Assert.assertEquals("test", result);
	}

	@Test
	public void testHashCode() throws NoSuchMethodException {
		Method method = TestClass1.class.getMethod("testMethod", String.class);

		Assert.assertEquals(method.hashCode(), _methodKey.hashCode());
	}

	@Test
	public void testToString() {
		Assert.assertEquals(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestMethodKey." +
				"testMethod(java.lang.String)",
			_methodKey.toString());
		Assert.assertEquals(
			"com.liferay.portal.kernel.util.MethodKeyTest$TestMethodKey." +
				"testMethod(java.lang.String)",
			ReflectionTestUtil.getFieldValue(_methodKey, "_toString"));

		ReflectionTestUtil.setFieldValue(_methodKey, "_toString", "testString");

		Assert.assertEquals("testString", _methodKey.toString());
	}

	@Test
	public void testTransform() throws ClassNotFoundException {
		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		MethodKey methodKey = _methodKey.transform(classLoader);

		Assert.assertTrue(_methodKey.equals(methodKey));
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

	private class TestClass1 {

		public void testMethod(int parameter) {
		}

		public String testMethod(String parameter) {
			return parameter;
		}

		public void testMethod1(String parameter) {
		}

	}

	private class TestClass2 {

		public void testMethod(String parameter) {
		}

	}

}