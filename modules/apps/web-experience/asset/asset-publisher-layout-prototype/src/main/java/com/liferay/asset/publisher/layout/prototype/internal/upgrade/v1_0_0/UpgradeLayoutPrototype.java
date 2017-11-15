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

package com.liferay.asset.publisher.layout.prototype.internal.upgrade.v1_0_0;

import com.liferay.portal.kernel.upgrade.BaseUpgradePrototype;
import com.liferay.portal.language.LanguageResources;

/**
 * @author Leon Chi
 */
public class UpgradeLayoutPrototype extends BaseUpgradePrototype {

	@Override
	protected void doUpgrade() throws Exception {
		upgradePrototype(
			getClass(), LanguageResources.RESOURCE_BUNDLE_LOADER,
			"LayoutPrototype", _NAME, _DESCRIPTION,
			"layout-prototype-web-content-title",
			"layout-prototype-web-content-description");
	}

	private static final String _DESCRIPTION =
		"Create, edit, and explore web content with this page. Search " +
			"available content, explore related content with tags, and " +
				"browse content categories.";

	private static final String _NAME =
		"<?xml version='1.0' encoding='UTF-8'?><root available-locales=" +
			"\"en_US\" default-locale=\"en_US\"><Name language-id=\"en_US\">" +
				"Content Display Page</Name></root>";

}