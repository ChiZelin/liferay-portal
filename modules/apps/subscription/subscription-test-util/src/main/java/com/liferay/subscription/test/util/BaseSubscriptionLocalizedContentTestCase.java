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

package com.liferay.subscription.test.util;

import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ModifiableSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.test.rule.Inject;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.test.mail.MailMessage;
import com.liferay.portal.test.mail.MailServiceTestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public abstract class BaseSubscriptionLocalizedContentTestCase
	extends BaseSubscriptionTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		defaultLocale = LocaleThreadLocal.getDefaultLocale();
		layout = LayoutTestUtil.addLayout(group);
	}

	@After
	public void tearDown() throws Exception {
		LocaleThreadLocal.setDefaultLocale(defaultLocale);
	}

	@Test
	public void testSubscriptionLocalizedContentWhenAddingBaseModel()
		throws Exception {

		Map<Locale, String> previousLocalizedContents = new HashMap<>();

		previousLocalizedContents.putAll(localizedContents);

		localizedContents.put(LocaleUtil.GERMANY, GERMAN_BODY);

		setBaseModelSubscriptionBodyPreferences(
			getSubscriptionAddedBodyPreferenceName());

		addSubscriptionContainerModel(getDefaultContainerModelId());

		LocaleThreadLocal.setDefaultLocale(LocaleUtil.GERMANY);

		addBaseModel(creatorUser.getUserId(), getDefaultContainerModelId());

		List<MailMessage> messages = MailServiceTestUtil.getMailMessages(
			"Body", GERMAN_BODY);

		Assert.assertEquals(messages.toString(), 1, messages.size());

		localizedContents = previousLocalizedContents;
	}

	@Test
	public void testSubscriptionLocalizedContentWhenUpdatingBaseModel()
		throws Exception {

		Map<Locale, String> previousLocalizedContents = new HashMap<>();

		previousLocalizedContents.putAll(localizedContents);

		localizedContents.put(LocaleUtil.SPAIN, SPANISH_BODY);

		setBaseModelSubscriptionBodyPreferences(
			getSubscriptionUpdatedBodyPreferenceName());

		LocaleThreadLocal.setDefaultLocale(LocaleUtil.SPAIN);

		long baseModelId = addBaseModel(
			creatorUser.getUserId(), getDefaultContainerModelId());

		addSubscriptionContainerModel(getDefaultContainerModelId());

		updateBaseModel(creatorUser.getUserId(), baseModelId);

		List<MailMessage> messages = MailServiceTestUtil.getMailMessages(
			"Body", SPANISH_BODY);

		Assert.assertEquals(messages.toString(), 1, messages.size());

		localizedContents = previousLocalizedContents;
	}

	protected abstract void addSubscriptionContainerModel(long containerModelId)
		throws Exception;

	protected long getDefaultContainerModelId() {
		return PARENT_CONTAINER_MODEL_ID_DEFAULT;
	}

	protected abstract String getPortletId();

	protected String getServiceName() {
		return StringPool.BLANK;
	}

	protected abstract String getSubscriptionAddedBodyPreferenceName();

	protected abstract String getSubscriptionUpdatedBodyPreferenceName();

	protected void setBaseModelSubscriptionBodyPreferences(
			String bodyPreferenceName)
		throws Exception {

		Settings settings = _settingsFactory.getSettings(
			new GroupServiceSettingsLocator(
				group.getGroupId(), getServiceName()));

		ModifiableSettings modifiableSettings =
			settings.getModifiableSettings();

		for (Map.Entry<Locale, String> localizedContent :
				localizedContents.entrySet()) {

			modifiableSettings.setValue(
				LocalizationUtil.getLocalizedName(
					bodyPreferenceName,
					LocaleUtil.toLanguageId(localizedContent.getKey())),
				localizedContent.getValue());
		}

		modifiableSettings.store();
	}

	protected static final String GERMAN_BODY = "Hallo Welt";

	protected static final String SPANISH_BODY = "Hola Mundo";

	protected Locale defaultLocale;
	protected Layout layout;
	protected Map<Locale, String> localizedContents = new HashMap<>();

	@Inject
	private SettingsFactory _settingsFactory;

}