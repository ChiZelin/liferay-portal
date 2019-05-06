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

package com.liferay.portlet.invoker.filter.container.impl.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.InvokerFilterContainer;
import com.liferay.portal.kernel.portlet.PortletInstanceFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.ServletContextClassLoaderPool;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.model.impl.PortletAppImpl;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import javax.servlet.ServletContext;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Leon Chi
 */
@RunWith(Arquillian.class)
public class InvokerFilterContainerImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws PortletException {
		Bundle bundle = FrameworkUtil.getBundle(
			InvokerFilterContainerImplTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_actionFilterServiceRegistration = bundleContext.registerService(
			PortletFilter.class, new TestActionFilter(),
			new HashMapDictionary<String, Object>() {
				{
					put("javax.portlet.name", "InvokerFilterContainerImplTest");
					put("preinitialized.filter", "false");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_eventFilterServiceRegistration = bundleContext.registerService(
			PortletFilter.class, new TestEventFilter(),
			new HashMapDictionary<String, Object>() {
				{
					put("javax.portlet.name", "InvokerFilterContainerImplTest");
					put("preinitialized.filter", "true");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_renderFilterServiceRegistration = bundleContext.registerService(
			PortletFilter.class, new TestRenderFilter(),
			new HashMapDictionary<String, Object>() {
				{
					put("javax.portlet.name", "InvokerFilterContainerImplTest");
					put("preinitialized.filter", "true");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_resourceFilterServiceRegistration = bundleContext.registerService(
			PortletFilter.class, new TestResourceFilter(),
			new HashMapDictionary<String, Object>() {
				{
					put("javax.portlet.name", "InvokerFilterContainerImplTest");
					put("preinitialized.filter", "true");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		String servletContextName =
			ServletContextClassLoaderPool.getServletContextName(
				PortalClassLoaderUtil.getClassLoader());

		ServletContext servletContext = ServletContextPool.get(
			servletContextName);

		PortletAppImpl portletAppImpl = new PortletAppImpl(servletContextName);

		portletAppImpl.setWARFile(false);

		_portlet = new PortletImpl();

		_portlet.setPortletApp(portletAppImpl);
		_portlet.setPortletClass(MVCPortlet.class.getName());
		_portlet.setPortletId("InvokerFilterContainerImplTest");

		_invokerFilterContainer =
			(InvokerFilterContainer)_portletInstanceFactory.create(
				_portlet, servletContext, true);
	}

	@AfterClass
	public static void tearDownClass() {
		_actionFilterServiceRegistration.unregister();
		_eventFilterServiceRegistration.unregister();
		_renderFilterServiceRegistration.unregister();
		_resourceFilterServiceRegistration.unregister();

		_portletInstanceFactory.destroy(_portlet);
	}

	@Test
	public void testGetActionFilters() {
		boolean found = false;

		List<ActionFilter> actionFilters =
			_invokerFilterContainer.getActionFilters();

		for (ActionFilter actionFilter : actionFilters) {
			Class<?> clazz = actionFilter.getClass();

			String className = clazz.getName();

			if (className.equals(TestActionFilter.class.getName())) {
				found = true;

				break;
			}
		}

		Assert.assertTrue("Not found " + TestActionFilter.class, found);
	}

	@Test
	public void testGetEventFilters() {
		boolean found = false;

		List<EventFilter> eventFilters =
			_invokerFilterContainer.getEventFilters();

		for (EventFilter eventFilter : eventFilters) {
			Class<?> clazz = eventFilter.getClass();

			String className = clazz.getName();

			if (className.equals(TestEventFilter.class.getName())) {
				found = true;

				break;
			}
		}

		Assert.assertTrue("Not found " + TestEventFilter.class, found);
	}

	@Test
	public void testGetRenderFilters() {
		boolean found = false;

		List<RenderFilter> renderFilters =
			_invokerFilterContainer.getRenderFilters();

		for (RenderFilter renderFilter : renderFilters) {
			Class<?> clazz = renderFilter.getClass();

			String className = clazz.getName();

			if (className.equals(TestRenderFilter.class.getName())) {
				found = true;

				break;
			}
		}

		Assert.assertTrue("Not found " + TestRenderFilter.class, found);
	}

	@Test
	public void testGetResourceFilters() {
		boolean found = false;

		List<ResourceFilter> resourceFilters =
			_invokerFilterContainer.getResourceFilters();

		for (ResourceFilter resourceFilter : resourceFilters) {
			Class<?> clazz = resourceFilter.getClass();

			String className = clazz.getName();

			if (className.equals(TestResourceFilter.class.getName())) {
				found = true;

				break;
			}
		}

		Assert.assertTrue("Not found " + TestResourceFilter.class, found);
	}

	@Test
	public void testInit() {
		Assert.assertTrue(_calledInit);
	}

	private static ServiceRegistration<PortletFilter>
		_actionFilterServiceRegistration;
	private static boolean _calledInit;
	private static ServiceRegistration<PortletFilter>
		_eventFilterServiceRegistration;
	private static InvokerFilterContainer _invokerFilterContainer;
	private static Portlet _portlet;

	@Inject
	private static PortletInstanceFactory _portletInstanceFactory;

	private static ServiceRegistration<PortletFilter>
		_renderFilterServiceRegistration;
	private static ServiceRegistration<PortletFilter>
		_resourceFilterServiceRegistration;

	private static class TestActionFilter implements ActionFilter {

		@Override
		public void destroy() {
		}

		@Override
		public void doFilter(
			ActionRequest actionRequest, ActionResponse actionResponse,
			FilterChain filterChain) {
		}

		@Override
		public void init(FilterConfig filterConfig) {
			_calledInit = true;
		}

	}

	private static class TestEventFilter implements EventFilter {

		@Override
		public void destroy() {
		}

		@Override
		public void doFilter(
			EventRequest eventRequest, EventResponse eventResponse,
			FilterChain filterChain) {
		}

		@Override
		public void init(FilterConfig filterConfig) {
		}

	}

	private static class TestRenderFilter implements RenderFilter {

		@Override
		public void destroy() {
		}

		@Override
		public void doFilter(
			RenderRequest renderRequest, RenderResponse renderResponse,
			FilterChain filterChain) {
		}

		@Override
		public void init(FilterConfig filterConfig) {
		}

	}

	private static class TestResourceFilter implements ResourceFilter {

		@Override
		public void destroy() {
		}

		@Override
		public void doFilter(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse,
			FilterChain filterChain) {
		}

		@Override
		public void init(FilterConfig filterConfig) {
		}

	}

}