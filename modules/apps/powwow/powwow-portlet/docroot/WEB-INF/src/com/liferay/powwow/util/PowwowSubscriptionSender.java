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

package com.liferay.powwow.util;

import com.liferay.mail.kernel.template.MailTemplate;
import com.liferay.mail.kernel.template.MailTemplateContext;
import com.liferay.mail.kernel.template.MailTemplateFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.subscription.util.SubscriptionSender;

import java.util.Locale;

/**
 * @author Evan Thibodeau
 */
public class PowwowSubscriptionSender extends SubscriptionSender {

	public String getEmailNotificationBody(Locale locale) throws Exception {
		String processedBody = null;

		if (localizedBodyMap != null) {
			String localizedBody = localizedBodyMap.get(locale);

			if (Validator.isNull(localizedBody)) {
				Locale defaultLocale = LocaleUtil.getDefault();

				processedBody = localizedBodyMap.get(defaultLocale);
			}
			else {
				processedBody = localizedBody;
			}
		}
		else {
			processedBody = body;
		}

		return _replaceContent(processedBody, locale, true);
	}

	public String getEmailNotificationSubject(Locale locale) throws Exception {
		String processedSubject = null;

		if (localizedSubjectMap != null) {
			String localizedSubject = localizedSubjectMap.get(locale);

			if (Validator.isNull(localizedSubject)) {
				Locale defaultLocale = LocaleUtil.getDefault();

				processedSubject = localizedSubjectMap.get(defaultLocale);
			}
			else {
				processedSubject = localizedSubject;
			}
		}
		else {
			processedSubject = subject;
		}

		return _replaceContent(processedSubject, locale, false);
	}

	private String _replaceContent(
			String content, Locale locale, boolean escape)
		throws Exception {

		MailTemplateContext mailTemplateContext = getBasicMailTemplateContext(
			locale);

		MailTemplate mailTemplate = MailTemplateFactoryUtil.createMailTemplate(
			content, escape);

		return mailTemplate.renderAsString(locale, mailTemplateContext);
	}

}