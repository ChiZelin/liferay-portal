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

package com.liferay.subscription.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.subscription.model.Subscription;
import com.liferay.subscription.service.SubscriptionLocalServiceUtil;

/**
 * The extended model base implementation for the Subscription service. Represents a row in the &quot;Subscription&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SubscriptionImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SubscriptionImpl
 * @see Subscription
 * @generated
 */
@ProviderType
public abstract class SubscriptionBaseImpl
	extends SubscriptionModelImpl implements Subscription {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a subscription model instance should use the <code>Subscription</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			SubscriptionLocalServiceUtil.addSubscription(this);
		}
		else {
			SubscriptionLocalServiceUtil.updateSubscription(this);
		}
	}

}