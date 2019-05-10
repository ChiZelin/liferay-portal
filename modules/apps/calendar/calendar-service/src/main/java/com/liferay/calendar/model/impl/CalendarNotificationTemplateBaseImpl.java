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

package com.liferay.calendar.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.calendar.model.CalendarNotificationTemplate;
import com.liferay.calendar.service.CalendarNotificationTemplateLocalServiceUtil;

/**
 * The extended model base implementation for the CalendarNotificationTemplate service. Represents a row in the &quot;CalendarNotificationTemplate&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CalendarNotificationTemplateImpl}.
 * </p>
 *
 * @author Eduardo Lundgren
 * @see CalendarNotificationTemplateImpl
 * @see CalendarNotificationTemplate
 * @generated
 */
@ProviderType
public abstract class CalendarNotificationTemplateBaseImpl
	extends CalendarNotificationTemplateModelImpl
	implements CalendarNotificationTemplate {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a calendar notification template model instance should use the <code>CalendarNotificationTemplate</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			CalendarNotificationTemplateLocalServiceUtil.
				addCalendarNotificationTemplate(this);
		}
		else {
			CalendarNotificationTemplateLocalServiceUtil.
				updateCalendarNotificationTemplate(this);
		}
	}

}