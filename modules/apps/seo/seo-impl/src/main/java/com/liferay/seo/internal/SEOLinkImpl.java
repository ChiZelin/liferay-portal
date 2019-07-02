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

package com.liferay.seo.internal;

import com.liferay.portal.kernel.seo.SEOLink;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Adolfo Pérez
 */
public class SEOLinkImpl implements SEOLink {

	public SEOLinkImpl(
		String href, String hrefLang, SEOLink.SEOLinkRel seoLinkRel) {

		if (Validator.isNull(href)) {
			throw new IllegalArgumentException(
				"Null or empty string is not a valid SEOLink href");
		}

		if (seoLinkRel == null) {
			throw new IllegalArgumentException(
				"Null is not a valid SEOLink seoLinkRel");
		}

		_href = href;
		_hrefLang = hrefLang;
		_seoLinkRel = seoLinkRel;
	}

	public String getHref() {
		return _href;
	}

	public String getHrefLang() {
		return _hrefLang;
	}

	public SEOLink.SEOLinkRel getSeoLinkRel() {
		return _seoLinkRel;
	}

	private final String _href;
	private final String _hrefLang;
	private final SEOLink.SEOLinkRel _seoLinkRel;

}