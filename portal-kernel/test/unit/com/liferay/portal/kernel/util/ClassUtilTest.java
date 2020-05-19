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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.IOException;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author André de Oliveira
 * @author Hugo Huijser
 */
public class ClassUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		URL.setURLStreamHandlerFactory(
			protocol -> {
				if (protocol.equals("vfs") || protocol.equals("zip") ||
					protocol.equals("bundleresource") ||
					protocol.equals("wsjar")) {

					return new URLStreamHandler() {

						protected URLConnection openConnection(URL url)
							throws IOException {

							return new URLConnection(url) {

								public void connect() throws IOException {
									throw new UnsupportedOperationException(
										"protocol not supported");
								}

							};
						}

					};
				}

				return null;
			});
	}

	@Test
	public void testGetClassesFromAnnotation() throws Exception {
		testGetClassesFromAnnotation("Annotation", "Annotation");
		testGetClassesFromAnnotation(
			"AnnotationClass.Annotation", "AnnotationClass");
	}

	@Test
	public void testGetClassesFromAnnotationsWithArrayParameter()
		throws Exception {

		testGetClassesFromAnnotation("Annotation", "Annotation", "A");
		testGetClassesFromAnnotation("Annotation", "Annotation", "A", "B");
		testGetClassesFromAnnotation("Annotation", "Annotation", "A", "B", "C");

		testGetClassesFromAnnotation(
			"AnnotationClass.Annotation", "AnnotationClass", "A");
		testGetClassesFromAnnotation(
			"AnnotationClass.Annotation", "AnnotationClass", "A", "B");
		testGetClassesFromAnnotation(
			"AnnotationClass.Annotation", "AnnotationClass", "A", "B", "C");
	}

	@Test
	public void testGetClassName() {
		Assert.assertEquals(
			"java.lang.Object", ClassUtil.getClassName(new Object()));
	}

	@Test
	public void testGetParentPath() {
		ClassLoader classLoader = ClassUtilTest.class.getClassLoader();

		String className = "java/lang/String.class";

		URL url = classLoader.getResource(className);

		URI uri = ClassUtil.getPathURIFromURL(url);

		Path path = Paths.get(uri);

		String expectedParentPath = StringUtil.replace(
			path.toString(), CharPool.BACK_SLASH, CharPool.SLASH);

		int pos = expectedParentPath.indexOf(className);

		expectedParentPath = expectedParentPath.substring(0, pos);

		Assert.assertEquals(
			expectedParentPath,
			ClassUtil.getParentPath(classLoader, "java.lang.String.class"));
		Assert.assertEquals(
			expectedParentPath,
			ClassUtil.getParentPath(classLoader, "java.lang.String"));

		//Test log output

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					ClassUtil.class.getName(), Level.FINE)) {

			ClassUtil.getParentPath(classLoader, "java.lang.String");

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 3, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Class name java.lang.String", logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals("URI " + uri, logRecord.getMessage());

			logRecord = logRecords.get(2);

			Assert.assertEquals(
				"Parent path " + expectedParentPath, logRecord.getMessage());
		}
	}

	@Test
	public void testGetPathURIFromURLLog() throws MalformedURLException {
		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					ClassUtil.class.getName(), Level.FINE)) {

			ClassUtil.getPathURIFromURL(
				new URL(
					"jar:file:/opt/liferay/tomcat/lib/servlet-api.jar" +
						"!/javax/servlet/Servlet.class"));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"URI file:/opt/liferay/tomcat/lib/servlet-api.jar!/javax" +
					"/servlet/Servlet.class",
				logRecord.getMessage());
		}
	}

	@Test
	public void testGetPathURIFromURLUnix() throws Exception {
		testGetPathURIFromURL(
			"jar:file:/opt/liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/opt/liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"jar:file:/opt/with%20space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/opt/with space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"file:/opt/liferay/tomcat/classes/javax/servlet/Servlet.class",
			"/opt/liferay/tomcat/classes/javax/servlet/Servlet.class");
	}

	@Test
	public void testGetPathURIFromURLWeblogic() throws Exception {
		testGetPathURIFromURL(
			"zip:/opt/liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/opt/liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"zip:/opt/with%20space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/opt/with space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"zip:C:/Liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/C:/Liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"zip:C:/With%20Space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/C:/With Space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
	}

	@Test
	public void testGetPathURIFromURLWebsphere() throws Exception {
		testGetPathURIFromURL(
			"bundleresource://266.fwk-486185329/javax/servlet/Servlet.class",
			"/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"wsjar:file:/opt/liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/opt/liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
	}

	@Test
	public void testGetPathURIFromURLWildfly() throws Exception {
		testGetPathURIFromURL(
			"vfs:/opt/liferay/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class",
			"/opt/liferay/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class");
		testGetPathURIFromURL(
			"vfs:/opt/with%20space/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class",
			"/opt/with space/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class");
		testGetPathURIFromURL(
			"vfs:/C:/Liferay/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class",
			"/C:/Liferay/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class");
		testGetPathURIFromURL(
			"vfs:/C:/With%20Space/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class",
			"/C:/With Space/tomcat/lib/servlet-api.jar/javax/servlet" +
				"/Servlet.class");
	}

	@Test
	public void testGetPathURIFromURLWindows() throws Exception {
		testGetPathURIFromURL(
			"jar:file:/C:/Liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/C:/Liferay/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"jar:file:/C:/With%20Space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class",
			"/C:/With Space/tomcat/lib/servlet-api.jar" +
				"!/javax/servlet/Servlet.class");
		testGetPathURIFromURL(
			"file:/C:/Liferay/tomcat/classes/javax/servlet/Servlet.class",
			"/C:/Liferay/tomcat/classes/javax/servlet/Servlet.class");
	}

	@Test(expected = URISyntaxException.class)
	public void testGetPathURIFromURLWithIllegalCharacter() throws Throwable {
		try {
			ClassUtil.getPathURIFromURL(
				new URL(
					"jar:file:/[opt/liferay/tomcat/lib/servlet-api.jar" +
						"!/javax/servlet/Servlet.class"));

			Assert.fail(
				"SystemException caused by URISyntaxException should be " +
					"thrown because of the illegal character '['");
		}
		catch (SystemException systemException) {
			throw systemException.getCause();
		}
	}

	@Test(expected = MalformedURLException.class)
	public void testGetPathURIFromURLWithUnknownProtocol() throws Throwable {
		try {
			ClassUtil.getPathURIFromURL(
				new URL(
					"jar:unknown:/[opt/liferay/tomcat/lib/servlet-api.jar" +
						"!/javax/servlet/Servlet.class"));

			Assert.fail(
				"SystemException caused by MalformedURLException should be " +
					"thrown because of the unknown protocol");
		}
		catch (SystemException systemException) {
			throw systemException.getCause();
		}
	}

	@Test
	public void testIsSubclass() {
		Assert.assertTrue(
			ClassUtil.isSubclass(TestClassA.class, TestClassA.class));
		Assert.assertFalse(
			ClassUtil.isSubclass(TestClassA.class, (Class<?>)null));
		Assert.assertFalse(ClassUtil.isSubclass(null, TestClassA.class));
		Assert.assertTrue(
			ClassUtil.isSubclass(TestClassB.class, TestClassA.class));
		Assert.assertTrue(
			ClassUtil.isSubclass(TestClassA.class, TestInterfaceA.class));
		Assert.assertTrue(
			ClassUtil.isSubclass(TestClassB.class, TestInterfaceA.class));
		Assert.assertFalse(
			ClassUtil.isSubclass(TestClassC.class, TestInterfaceA.class));

		Assert.assertTrue(
			ClassUtil.isSubclass(TestClassA.class, TestClassA.class.getName()));
		Assert.assertFalse(
			ClassUtil.isSubclass(TestClassA.class, (String)null));
		Assert.assertFalse(
			ClassUtil.isSubclass(null, TestClassA.class.getName()));
		Assert.assertTrue(
			ClassUtil.isSubclass(TestClassB.class, TestClassA.class.getName()));
		Assert.assertTrue(
			ClassUtil.isSubclass(
				TestClassA.class, TestInterfaceA.class.getName()));
		Assert.assertTrue(
			ClassUtil.isSubclass(
				TestClassB.class, TestInterfaceA.class.getName()));
		Assert.assertFalse(
			ClassUtil.isSubclass(
				TestClassC.class, TestInterfaceA.class.getName()));
	}

	protected void testGetClassesFromAnnotation(
			String annotation, String expectedAnnotationClassName,
			String... arrayParameterClassNames)
		throws Exception {

		StringBundler sb = new StringBundler(
			arrayParameterClassNames.length * 3 + 2);

		sb.append(StringPool.AT);
		sb.append(annotation);

		if (arrayParameterClassNames.length > 0) {
			sb.append("({");

			for (int i = 0; i < arrayParameterClassNames.length; i++) {
				sb.append(arrayParameterClassNames[i]);
				sb.append(".class");

				if (i < (arrayParameterClassNames.length - 1)) {
					sb.append(StringPool.COMMA);
				}
			}

			sb.append("})");
		}

		Set<String> actualClassNames = ClassUtil.getClasses(
			new StringReader(sb.toString()), null);

		Set<String> expectedClassNames = new HashSet<>();

		expectedClassNames.add(expectedAnnotationClassName);

		Collections.addAll(expectedClassNames, arrayParameterClassNames);

		Assert.assertEquals(expectedClassNames, actualClassNames);
	}

	protected void testGetPathURIFromURL(String url, String expectedPath)
		throws MalformedURLException {

		URI uri = ClassUtil.getPathURIFromURL(new URL(url));

		Assert.assertEquals(expectedPath, uri.getPath());
	}

	private class TestClassA implements TestInterfaceA {
	}

	private class TestClassB extends TestClassA {
	}

	private class TestClassC implements TestInterfaceB {
	}

	private interface TestInterfaceA {
	}

	private interface TestInterfaceB {
	}

}