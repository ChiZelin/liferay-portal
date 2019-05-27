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

package com.liferay.server.admin.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutBranch;
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.LayoutSetBranch;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.LayoutBranchLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutRevisionLocalService;
import com.liferay.portal.kernel.service.LayoutSetBranchLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.LayoutTestUtil;
import com.liferay.portlet.util.test.PortletKeys;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Andrew Betts
 */
@RunWith(Arquillian.class)
public class CleanUpPortletPreferencesUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testCleanUpOrphanePortletPreferences() throws Exception {
		LayoutRevision layoutRevision = _getLayoutRevision();

		PortletPreferences portletPreferences =
			_portletPreferencesLocalService.addPortletPreferences(
				TestPropsValues.getCompanyId(), TestPropsValues.getUserId(), 0,
				layoutRevision.getLayoutRevisionId(),
				RandomTestUtil.randomString(), new PortletImpl(),
				StringPool.BLANK);

		Assert.assertNotNull(portletPreferences);

		_callCleanUpLayoutRevisionPortletPreferences();

		Assert.assertNull(
			_portletPreferencesLocalService.fetchPortletPreferences(
				portletPreferences.getPortletPreferencesId()));
	}

	@Test
	public void testCleanUpProperPortletPreferences() throws Exception {
		LayoutRevision layoutRevision = _getLayoutRevision();

		String portletId = PortletIdCodec.encode(PortletKeys.TEST);

		UnicodeProperties typeSettingProperties =
			layoutRevision.getTypeSettingsProperties();

		typeSettingProperties.setProperty("column-1", portletId);

		layoutRevision = _layoutRevisionLocalService.updateLayoutRevision(
			layoutRevision);

		PortletPreferences portletPreferences =
			_portletPreferencesLocalService.addPortletPreferences(
				TestPropsValues.getCompanyId(), TestPropsValues.getUserId(), 0,
				layoutRevision.getLayoutRevisionId(), portletId, null,
				StringPool.BLANK);

		Assert.assertNotNull(portletPreferences);

		Layout layout = _layoutLocalService.getLayout(layoutRevision.getPlid());

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		List<String> portletIds = layoutTypePortlet.getPortletIds();

		Assert.assertTrue(portletIds.isEmpty());

		_callCleanUpLayoutRevisionPortletPreferences();

		Assert.assertNotNull(
			_portletPreferencesLocalService.fetchPortletPreferences(
				portletPreferences.getPortletPreferencesId()));
	}

	private void _callCleanUpLayoutRevisionPortletPreferences()
		throws InvalidSyntaxException {

		Bundle bundle = FrameworkUtil.getBundle(
			CleanUpPortletPreferencesUtilTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		Collection<ServiceReference<MVCActionCommand>> serviceReferences =
			bundleContext.getServiceReferences(MVCActionCommand.class, null);

		MVCActionCommand mvcActionCommand = null;

		for (ServiceReference<MVCActionCommand> serviceReference :
				serviceReferences) {

			if (com.liferay.portal.kernel.util.PortletKeys.SERVER_ADMIN.equals(
					serviceReference.getProperty("javax.portlet.name"))) {

				mvcActionCommand = bundleContext.getService(serviceReference);
			}
		}

		try {
			mvcActionCommand.processAction(
				(ActionRequest)ProxyUtil.newProxyInstance(
					ActionRequest.class.getClassLoader(),
					new Class<?>[] {ActionRequest.class},
					(proxy, method, args) -> {
						if (Objects.equals("getAttribute", method.getName()) &&
							(args[0] == WebKeys.THEME_DISPLAY)) {

							return new ThemeDisplay() {
								{
									setPermissionChecker(
										(PermissionChecker)
											ProxyUtil.newProxyInstance(
												PermissionChecker.class.
													getClassLoader(),
												new Class<?>[] {
													PermissionChecker.class
												},
												(proxy, method, args) -> {
													if (Objects.equals(
															method.getName(),
															"isOmniadmin")) {

														return true;
													}

													return null;
												}));
								}
							};
						}

						if (Objects.equals("getParameter", method.getName()) &&
							(args[0] == Constants.CMD)) {

							return "cleanUpPortletPreferences";
						}

						return null;
					}),
				ProxyFactory.newDummyInstance(ActionResponse.class));
		}
		catch (Exception e) {
		}
	}

	private LayoutRevision _getLayoutRevision() throws Exception {
		_group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(_group, false);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		LayoutSetBranch layoutSetBranch =
			_layoutSetBranchLocalService.addLayoutSetBranch(
				TestPropsValues.getUserId(), _group.getGroupId(), false,
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				true, 0, serviceContext);

		LayoutBranch layoutBranch =
			_layoutBranchLocalService.getMasterLayoutBranch(
				layoutSetBranch.getLayoutSetBranchId(), layout.getPlid());

		return _layoutRevisionLocalService.getLayoutRevision(
			layoutSetBranch.getLayoutSetBranchId(),
			layoutBranch.getLayoutBranchId(), layout.getPlid());
	}

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutBranchLocalService _layoutBranchLocalService;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private LayoutRevisionLocalService _layoutRevisionLocalService;

	@Inject
	private LayoutSetBranchLocalService _layoutSetBranchLocalService;

	@Inject
	private PortletPreferencesLocalService _portletPreferencesLocalService;

}