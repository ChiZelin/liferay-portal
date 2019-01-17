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

package com.liferay.portal.kernel.servlet.filters.invoker;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheHelperUtil;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.servlet.NonSerializableObjectRequestWrapper;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.tools.ToolDependencies;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portal.util.PortalImpl;
import com.liferay.portal.util.PropsImpl;

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Mika Koivisto
 * @auther Leon Chi
 */
public class InvokerFilterTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() {
		ToolDependencies.wireCaches();

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(new HttpImpl());

		PropsUtil.setProps(new PropsImpl());
	}

	@Test
	public void testClearFilterChainsCache() {

		// Test case 1: It will do nothing when _filterChains is null

		InvokerFilter invokerFilter1 = new InvokerFilter();

		invokerFilter1.clearFilterChainsCache();

		// Test case 2: Entries in _filterChains should be removed when
		// clearFilterChainsCache is called

		PortalCache<String, InvokerFilterChain> filterChains =
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, "TestPortalCacheName");

		filterChains.put("key", new InvokerFilterChain(new MockFilterChain()));

		InvokerFilter invokerFilter = new InvokerFilter();

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_filterChains", filterChains);

		invokerFilter.clearFilterChainsCache();

		Assert.assertEquals(null, filterChains.get("key"));
	}

	@Test
	public void testDestroy() {
		boolean[] calledportalDestroy = new boolean[1];

		InvokerFilter invokerFilter = new InvokerFilter() {

			@Override
			public void portalDestroy() {
				calledportalDestroy[0] = true;
			}

		};

		invokerFilter.destroy();

		Assert.assertTrue(
			"The portalDestroy() method should be called",
			calledportalDestroy[0]);
	}

	@Test
	public void testDoFilter() throws IOException, ServletException {
		InvokerFilter invokerFilter = new InvokerFilter();

		InvokerFilterChain invokerFilterChain = new InvokerFilterChain(
			new MockFilterChain());

		InvokerFilterHelper invokerFilterHelper = new InvokerFilterHelper() {

			@Override
			protected InvokerFilterChain createInvokerFilterChain(
				HttpServletRequest request, Dispatcher dispatcher, String uri,
				FilterChain filterChain) {

				return invokerFilterChain;
			}

		};

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_invokerFilterHelper", invokerFilterHelper);

		Assert.assertNull(
			ReflectionTestUtil.getFieldValue(
				invokerFilterChain, "_contextClassLoader"));

		invokerFilter.doFilter(
			new MockHttpServletRequest(), new MockHttpServletResponse(),
			new MockFilterChain());

		Assert.assertNotNull(
			ReflectionTestUtil.getFieldValue(
				invokerFilterChain, "_contextClassLoader"));
	}

	@Test
	public void testDoPortalDestroy() {

		// Tetst case 1:

		InvokerFilter invokerFilter = new InvokerFilter();

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_filterConfig", new MockFilterConfig());
		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_INVOKER_FILTER_CHAIN_ENABLED", false);

		invokerFilter.doPortalDestroy();

		// Test case 2:

		InvokerFilter invokerFilter2 = new InvokerFilter();

		MockFilterConfig mockFilterConfig2 = new MockFilterConfig(
			"TestPortalCacheName");

		ServletContext servletContext = mockFilterConfig2.getServletContext();

		boolean[] calledDestroy = new boolean[1];

		InvokerFilterHelper invokerFilterHelper = new InvokerFilterHelper() {

			@Override
			public void destroy() {
				calledDestroy[0] = true;
			}

		};

		servletContext.setAttribute(
			InvokerFilterHelper.class.getName(), invokerFilterHelper);

		ReflectionTestUtil.setFieldValue(
			invokerFilter2, "_filterConfig", mockFilterConfig2);
		ReflectionTestUtil.setFieldValue(
			invokerFilter2, "_INVOKER_FILTER_CHAIN_ENABLED", true);

		PortalCache<String, InvokerFilterChain> filterChains =
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, "TestPortalCacheName");

		invokerFilter2.doPortalDestroy();

		Assert.assertNull(
			servletContext.getAttribute(InvokerFilterHelper.class.getName()));
		Assert.assertTrue(calledDestroy[0]);
		Assert.assertNotSame(
			filterChains,
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, "TestPortalCacheName"));
	}

	@Test
	public void testDoPortalInit() throws Exception {
		//Test case 1:

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		InvokerFilter invokerFilter1 = new InvokerFilter();

		MockFilterConfig mockFilterConfig1 = new MockFilterConfig();

		mockFilterConfig1.addInitParameter("dispatcher", "ASYNC");

		ReflectionTestUtil.setFieldValue(
			invokerFilter1, "_filterConfig", mockFilterConfig1);

		ReflectionTestUtil.setFieldValue(
			invokerFilter1, "_INVOKER_FILTER_CHAIN_ENABLED", true);

		invokerFilter1.doPortalInit();

		Assert.assertNotNull(
			ReflectionTestUtil.getFieldValue(invokerFilter1, "_filterChains"));
		Assert.assertEquals(
			Dispatcher.ASYNC,
			ReflectionTestUtil.getFieldValue(invokerFilter1, "_dispatcher"));

		ServletContext servletContext1 = mockFilterConfig1.getServletContext();

		InvokerFilterHelper invokerFilterHelper1 =
			ReflectionTestUtil.getFieldValue(
				invokerFilter1, "_invokerFilterHelper");

		Assert.assertSame(
			servletContext1.getAttribute(InvokerFilterHelper.class.getName()),
			invokerFilterHelper1);

		List<InvokerFilter> invokerFilters = ReflectionTestUtil.getFieldValue(
			invokerFilterHelper1, "_invokerFilters");

		Assert.assertEquals(invokerFilter1, invokerFilters.get(0));

		// Test case 2:

		InvokerFilter invokerFilter2 = new InvokerFilter();

		ReflectionTestUtil.setFieldValue(
			invokerFilter2, "_INVOKER_FILTER_CHAIN_ENABLED", false);

		MockFilterConfig mockFilterConfig2 = new MockFilterConfig();

		mockFilterConfig2.addInitParameter("dispatcher", "ASYNC");

		ReflectionTestUtil.setFieldValue(
			invokerFilter2, "_filterConfig", mockFilterConfig2);

		ServletContext servletContext2 = mockFilterConfig2.getServletContext();

		InvokerFilterHelper invokerFilterHelper2 = new InvokerFilterHelper();

		servletContext2.setAttribute(
			InvokerFilterHelper.class.getName(), invokerFilterHelper2);

		invokerFilter2.doPortalInit();

		Assert.assertNull(
			ReflectionTestUtil.getFieldValue(invokerFilter2, "_filterChains"));
		Assert.assertSame(
			invokerFilterHelper2,
			ReflectionTestUtil.getFieldValue(
				invokerFilter2, "_invokerFilterHelper"));
		Assert.assertEquals(
			Dispatcher.ASYNC,
			ReflectionTestUtil.getFieldValue(invokerFilter2, "_dispatcher"));
	}

	@Test
	public void testGetInvokerFilterChain() {
		//Test case 1:

		InvokerFilter invokerFilter = new InvokerFilter();

		InvokerFilterChain invokerFilterChain = new InvokerFilterChain(
			new MockFilterChain());

		InvokerFilterHelper invokerFilterHelper = new InvokerFilterHelper() {

			@Override
			protected InvokerFilterChain createInvokerFilterChain(
				HttpServletRequest request, Dispatcher dispatcher, String uri,
				FilterChain filterChain) {

				return invokerFilterChain;
			}

		};

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_invokerFilterHelper", invokerFilterHelper);

		Assert.assertSame(
			invokerFilterChain,
			invokerFilter.getInvokerFilterChain(
				new MockHttpServletRequest(), "/c/portal/login",
				new MockFilterChain()));

		//Test case 2:

		InvokerFilter invokerFilter2 = new InvokerFilter();

		FilterChain filterChain1 = new MockFilterChain();

		InvokerFilterChain invokerFilterChain2 = new InvokerFilterChain(
			filterChain1);

		InvokerFilterHelper invokerFilterHelper2 = new InvokerFilterHelper() {

			@Override
			protected InvokerFilterChain createInvokerFilterChain(
				HttpServletRequest request, Dispatcher dispatcher, String uri,
				FilterChain filterChain) {

				return invokerFilterChain2;
			}

		};

		ReflectionTestUtil.setFieldValue(
			invokerFilter2, "_invokerFilterHelper", invokerFilterHelper2);

		ReflectionTestUtil.setFieldValue(
			invokerFilter2, "_filterChains",
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, "TestPortalCacheName"));

		InvokerFilterChain resultInvokerFilterChain1 =
			invokerFilter2.getInvokerFilterChain(
				new MockHttpServletRequest(), "/c/portal/login", filterChain1);

		Assert.assertNotSame(invokerFilterChain2, resultInvokerFilterChain1);
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(
				invokerFilterChain2, "_filterChain"),
			ReflectionTestUtil.getFieldValue(
				resultInvokerFilterChain1, "_filterChain"));
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(invokerFilterChain2, "_filters"),
			ReflectionTestUtil.getFieldValue(
				resultInvokerFilterChain1, "_filters"));

		//Test case 3:

		InvokerFilter invokerFilter3 = new InvokerFilter();

		InvokerFilterChain invokerFilterChain3 = new InvokerFilterChain(
			new MockFilterChain());

		InvokerFilterHelper invokerFilterHelper3 = new InvokerFilterHelper() {

			@Override
			protected InvokerFilterChain createInvokerFilterChain(
				HttpServletRequest request, Dispatcher dispatcher, String uri,
				FilterChain filterChain) {

				return invokerFilterChain3;
			}

		};

		ReflectionTestUtil.setFieldValue(
			invokerFilter3, "_invokerFilterHelper", invokerFilterHelper3);

		ReflectionTestUtil.setFieldValue(
			invokerFilter3, "_filterChains",
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, "TestPortalCacheName"));

		Assert.assertSame(
			invokerFilterChain3,
			invokerFilter3.getInvokerFilterChain(
				new MockHttpServletRequest() {
					{
						setQueryString("name=value");
					}
				},
				"/c/portal/login", new MockFilterChain()));

		//Test case 4:

		InvokerFilter invokerFilter4 = new InvokerFilter();

		FilterChain filterChain2 = new MockFilterChain();

		InvokerFilterChain invokerFilterChain4 = new InvokerFilterChain(
			filterChain2);

		PortalCache<String, InvokerFilterChain> filterChains =
			PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, "TestPortalCacheName");

		filterChains.put("/c/portal/login", invokerFilterChain4);

		ReflectionTestUtil.setFieldValue(
			invokerFilter4, "_filterChains", filterChains);

		InvokerFilterChain resultInvokerFilterChain2 =
			invokerFilter4.getInvokerFilterChain(
				new MockHttpServletRequest(), "/c/portal/login", filterChain2);

		Assert.assertNotSame(invokerFilterChain4, resultInvokerFilterChain2);
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(
				invokerFilterChain4, "_filterChain"),
			ReflectionTestUtil.getFieldValue(
				resultInvokerFilterChain2, "_filterChain"));
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(invokerFilterChain4, "_filters"),
			ReflectionTestUtil.getFieldValue(
				resultInvokerFilterChain2, "_filters"));
	}

	@Test
	public void testGetOriginalRequestURI() {
		_testGetOriginalRequestURI(
			Dispatcher.ERROR, JavaConstants.JAVAX_SERVLET_ERROR_REQUEST_URI,
			"/c/portal/error", "/c/portal/error");
		_testGetOriginalRequestURI(
			Dispatcher.INCLUDE, JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI,
			"/c/portal/include", "/c/portal/include");
		_testGetOriginalRequestURI(
			Dispatcher.REQUEST, null, null, "/c/portal/login");
	}

	@Test
	public void testGetPortalCacheName() throws ServletException {
		_testGetPortalCacheName(
			"TestPortalCacheName", new MockFilterConfig("TestPortalCacheName"));
		_testGetPortalCacheName(
			"/contextpath-TestPortalCacheName",
			new MockFilterConfig(
				new MockServletContext() {
					{
						setContextPath("/contextpath");
					}
				},
				"TestPortalCacheName"));
	}

	@Test
	public void testGetURI() {
		_testGetURI(
			"/c/portal/login", "/contextpath", "/contextpath/c/portal/login");
		_testGetURI("/c/portal/login", null, "/c/portal/login");
		_testGetURI("/c/portal/login", "/", "/c/portal/login");
		_testGetURI("/c/portal/login", "/contextpath", "/c/portal/login");
	}

	@Test
	public void testGetURIDeprecated() {

		// Test case 1:

		InvokerFilter invokerFilter1 = new InvokerFilter();

		Assert.assertNull(invokerFilter1.getURI(new MockHttpServletRequest()));

		// Test case 2:

		InvokerFilter invokerFilter2 = new InvokerFilter();

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(HttpMethods.GET, "/c/portal/login");

		Assert.assertEquals(
			"/c/portal/login",
			invokerFilter2.getURI(
				mockHttpServletRequest,
				invokerFilter2.getOriginalRequestURI(mockHttpServletRequest)));
	}

	@Test
	public void testGetURIWithDoubleSlash() {
		InvokerFilter invokerFilter = new InvokerFilter();

		Assert.assertEquals(
			"/c/portal/login",
			invokerFilter.getURI(
				invokerFilter.getOriginalRequestURI(
					new MockHttpServletRequest(
						HttpMethods.GET,
						"/c///portal/%2e/login;jsessionid=ae01b0f2af." +
							"worker1"))));

		Assert.assertEquals(
			"/c/portal/login",
			invokerFilter.getURI(
				invokerFilter.getOriginalRequestURI(
					new MockHttpServletRequest(
						HttpMethods.GET,
						"/c///portal/%2e/../login;jsessionid=ae01b0f2af." +
							"worker1"))));
	}

	@Test
	public void testGetURIWithJSessionId() {
		InvokerFilter invokerFilter = new InvokerFilter();

		Assert.assertEquals(
			"/c/portal/login",
			invokerFilter.getURI(
				invokerFilter.getOriginalRequestURI(
					new MockHttpServletRequest(
						HttpMethods.GET,
						"/c/portal/login;jsessionid=ae01b0f2af.worker1"))));
	}

	@Test
	public void testGetURL() {
		_testGetURL(
			"",
			new MockHttpServletRequest() {

				@Override
				public StringBuffer getRequestURL() {
					return null;
				}

			});
		_testGetURL(
			"http://localhost/c/portal/login",
			new MockHttpServletRequest(HttpMethods.GET, "/c/portal/login"));
		_testGetURL(
			"http://localhost/c/portal/login?name=value",
			new MockHttpServletRequest(HttpMethods.GET, "/c/portal/login") {
				{
					setQueryString("name=value");
				}
			});
	}

	@Test
	public void testHandleLongRequestURL() {
		_testWithLog(
			new String[0],
			() -> {
				try {
					_testHandleLongRequestURL(
						true, 200, 4000,
						new MockHttpServletRequest(
							HttpMethods.GET, "/c/portal/login"));
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					Assert.fail(
						"There is an exception that prevents testing, please " +
							"see the stack trace");
				}
			});
		_testWithLog(
			new String[] {
				"Rejected /c/portal/log... because it has more than 16 " +
					"characters"
			},
			() -> {
				try {
					_testHandleLongRequestURL(
						false, 414, 16,
						new MockHttpServletRequest(
							HttpMethods.GET, "/c/portal/login/too/long") {

							{
								setQueryString("name=value");
							}

						});
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
					Assert.fail(
						"There is an exception that prevents testing, please " +
							"see the stack trace");
				}
			});
	}

	@Test
	public void testHandleNonserializableRequest() {
		_testHandleNonserializableRequest(
			true, Boolean.TRUE, "WEBLOGIC", new MockHttpServletRequest(), true);
		_testHandleNonserializableRequest(
			true, Boolean.TRUE, "WEBLOGIC",
			new NonSerializableObjectRequestWrapper(
				new MockHttpServletRequest()),
			true);
		_testHandleNonserializableRequest(
			false, null, "TOMCAT", new MockHttpServletRequest(), true);
	}

	@Test
	public void testInit() {

		// Test case 1:

		boolean[] calledRegisterPortalLifecycle = new boolean[1];

		InvokerFilter invokerFilter1 = new InvokerFilter() {

			@Override
			public void registerPortalLifecycle() {
				calledRegisterPortalLifecycle[0] = true;
			}

		};

		MockFilterConfig mockFilterConfig1 = new MockFilterConfig();

		_testWithLog(
			new String[0],
			() -> {
				try {
					invokerFilter1.init(mockFilterConfig1);
				}
				catch (ServletException se) {
					se.printStackTrace();
					Assert.fail(
						"There is an exception that prevents testing, please " +
							"see the stack trace");
				}
			});

		Assert.assertSame(
			mockFilterConfig1,
			ReflectionTestUtil.getFieldValue(invokerFilter1, "_filterConfig"));

		Assert.assertEquals(
			"",
			ReflectionTestUtil.getFieldValue(invokerFilter1, "_contextPath"));
		Assert.assertTrue(
			"The registerPortalLifecycle() method should be called if " +
				"registerPortalLifecycle is true",
			calledRegisterPortalLifecycle[0]);

		// Test case 2:

		InvokerFilter invokerFilter2 = new InvokerFilter() {

			@Override
			protected void doPortalInit() throws Exception {
				throw new Exception("Test exception");
			}

		};

		MockFilterConfig mockFilterConfig2 = new MockFilterConfig();

		mockFilterConfig2.addInitParameter(
			"register-portal-lifecycle", "false");

		_testWithLog(
			new String[] {"java.lang.Exception: Test exception"},
			() -> {
				try {
					invokerFilter2.init(mockFilterConfig2);

					Assert.fail("Exception was not thrown");
				}
				catch (ServletException se) {
					Throwable throwable = se.getCause();

					Assert.assertEquals(
						"Test exception", throwable.getMessage());
				}
			});

		// Test case 3:

		boolean[] calledDoPortalInit = new boolean[1];

		InvokerFilter invokerFilter3 = new InvokerFilter() {

			@Override
			protected void doPortalInit() {
				calledDoPortalInit[0] = true;
			}

		};

		MockFilterConfig mockFilterConfig3 = new MockFilterConfig();

		mockFilterConfig3.addInitParameter(
			"register-portal-lifecycle", "false");

		_testWithLog(
			new String[0],
			() -> {
				try {
					invokerFilter3.init(mockFilterConfig3);
				}
				catch (ServletException se) {
					se.printStackTrace();
					Assert.fail(
						"There is an exception that prevents testing, please " +
							"see the stack trace");
				}
			});

		Assert.assertTrue(
			"The doPortalInit() method should be called if " +
				"registerPortalLifecycle is false",
			calledDoPortalInit[0]);
	}

	@Test
	public void testLongURLsWithPath() throws Exception {
		testLongURL("/c/portal/login/");
	}

	@Test
	public void testLongURLsWithPathParameters() throws Exception {
		testLongURL("/c/portal/login/;");
	}

	@Test
	public void testLongURLsWithQueryString() throws Exception {
		testLongURL("/c/portal/login?param=");
	}

	@Test
	public void testSecureResponseHeaders() {
		_testSecureResponseHeaders("DENY", null);
		_testSecureResponseHeaders(null, Boolean.FALSE);
	}

	protected void testLongURL(String urlPrefix) throws Exception {
		InvokerFilter invokerFilter = new InvokerFilter();

		int invokerFilterUriMaxLength = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.INVOKER_FILTER_URI_MAX_LENGTH));

		char[] chars = new char[invokerFilterUriMaxLength];

		for (int i = 0; i < chars.length; i++) {
			chars[i] = '0';
		}

		String payload = urlPrefix.concat(new String(chars));

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(HttpMethods.GET, payload);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		MockFilterChain mockFilterChain = new MockFilterChain();

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					InvokerFilter.class.getName(), Level.WARNING)) {

			invokerFilter.doFilter(
				mockHttpServletRequest, mockHttpServletResponse,
				mockFilterChain);

			int status = mockHttpServletResponse.getStatus();

			Assert.assertEquals(
				HttpServletResponse.SC_REQUEST_URI_TOO_LONG, status);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertTrue(
				logRecord.getMessage().startsWith("Rejected " + urlPrefix));
		}
	}

	private void _testGetOriginalRequestURI(
		Dispatcher dispatcher, String name, String value,
		String expectedOriginalRequestURI) {

		InvokerFilter invokerFilter = new InvokerFilter();

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_dispatcher", dispatcher);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(HttpMethods.GET, "/c/portal/login");

		if (name != null) {
			mockHttpServletRequest.setAttribute(name, value);
		}

		Assert.assertEquals(
			expectedOriginalRequestURI,
			invokerFilter.getOriginalRequestURI(mockHttpServletRequest));
	}

	private void _testGetPortalCacheName(
			String expectedPortalCacheName, FilterConfig filterConfig)
		throws ServletException {

		InvokerFilter invokerFilter = new InvokerFilter();

		invokerFilter.init(filterConfig);

		Assert.assertEquals(
			expectedPortalCacheName,
			ReflectionTestUtil.invoke(
				invokerFilter, "_getPortalCacheName", new Class<?>[0]));
	}

	private void _testGetURI(
		String expectedURI, String contextPath, String originalURI) {

		InvokerFilter invokerFilter = new InvokerFilter();

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_contextPath", contextPath);

		Assert.assertEquals(expectedURI, invokerFilter.getURI(originalURI));
	}

	private void _testGetURL(
		String expectedRequestURI, HttpServletRequest request) {

		InvokerFilter invokerFilter = new InvokerFilter();

		Assert.assertEquals(expectedRequestURI, invokerFilter.getURL(request));
	}

	private void _testHandleLongRequestURL(
			boolean expectedResult, int expectedStatus, int maxLength,
			HttpServletRequest request)
		throws IOException {

		InvokerFilter invokerFilter = new InvokerFilter();

		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_INVOKER_FILTER_URI_MAX_LENGTH", maxLength);
		ReflectionTestUtil.setFieldValue(
			invokerFilter, "_dispatcher", Dispatcher.REQUEST);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		Assert.assertEquals(
			expectedResult,
			invokerFilter.handleLongRequestURL(
				request, mockHttpServletResponse,
				invokerFilter.getOriginalRequestURI(request)));
		Assert.assertEquals(
			expectedStatus, mockHttpServletResponse.getStatus());
	}

	private void _testHandleNonserializableRequest(
		boolean wrapped, Boolean expectedValue, String serverType,
		HttpServletRequest request,
		boolean weblogicRequestWrapNonSerializable) {

		Object object = ReflectionTestUtil.getFieldValue(
			ServerDetector.class, "_serverType");

		ReflectionTestUtil.setFieldValue(
			ServerDetector.class, "_serverType",
			ReflectionTestUtil.getFieldValue(object.getClass(), serverType));

		ReflectionTestUtil.setFieldValue(
			NonSerializableObjectRequestWrapper.class,
			"_WEBLOGIC_REQUEST_WRAP_NON_SERIALIZABLE",
			weblogicRequestWrapNonSerializable);

		InvokerFilter invokerFilter = new InvokerFilter();

		request = invokerFilter.handleNonSerializableRequest(request);

		Assert.assertEquals(
			wrapped, request instanceof NonSerializableObjectRequestWrapper);
		Assert.assertSame(
			expectedValue,
			request.getAttribute(
				NonSerializableObjectRequestWrapper.class.getName()));
	}

	private void _testSecureResponseHeaders(
		String expectedXFrameOptions, Boolean secureResponse) {

		InvokerFilter invokerFilter = new InvokerFilter();

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			InvokerFilter.class.getName() + "SECURE_RESPONSE", secureResponse);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		invokerFilter.secureResponseHeaders(
			mockHttpServletRequest, mockHttpServletResponse);

		Assert.assertEquals(
			Boolean.FALSE,
			mockHttpServletRequest.getAttribute(
				InvokerFilter.class.getName() + "SECURE_RESPONSE"));
		Assert.assertEquals(
			expectedXFrameOptions,
			mockHttpServletResponse.getHeader(HttpHeaders.X_FRAME_OPTIONS));
	}

	private void _testWithLog(String[] expectedMessages, Runnable runnable) {
		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					InvokerFilter.class.getName(), Level.OFF)) {

			runnable.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(
				"logRecords should be empty because the log level is OFF but " +
					"contains " + logRecords,
				logRecords.isEmpty());

			captureHandler.resetLogLevel(Level.ALL);

			runnable.run();

			Assert.assertEquals(
				logRecords.toString(), expectedMessages.length,
				logRecords.size());

			for (int i = 0; i < logRecords.size(); i++) {
				LogRecord logRecord = logRecords.get(i);

				Assert.assertEquals(
					expectedMessages[i], logRecord.getMessage());
			}
		}
	}

}