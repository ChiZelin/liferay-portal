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

package com.liferay.portal.workflow.kaleo.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignmentInstance;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskAssignmentInstanceLocalServiceUtil;

/**
 * The extended model base implementation for the KaleoTaskAssignmentInstance service. Represents a row in the &quot;KaleoTaskAssignmentInstance&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link KaleoTaskAssignmentInstanceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTaskAssignmentInstanceImpl
 * @see KaleoTaskAssignmentInstance
 * @generated
 */
@ProviderType
public abstract class KaleoTaskAssignmentInstanceBaseImpl
	extends KaleoTaskAssignmentInstanceModelImpl
	implements KaleoTaskAssignmentInstance {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a kaleo task assignment instance model instance should use the <code>KaleoTaskAssignmentInstance</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			KaleoTaskAssignmentInstanceLocalServiceUtil.
				addKaleoTaskAssignmentInstance(this);
		}
		else {
			KaleoTaskAssignmentInstanceLocalServiceUtil.
				updateKaleoTaskAssignmentInstance(this);
		}
	}

}