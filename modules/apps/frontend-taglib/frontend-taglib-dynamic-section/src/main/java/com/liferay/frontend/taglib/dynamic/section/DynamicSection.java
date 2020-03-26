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

package com.liferay.frontend.taglib.dynamic.section;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringBundlerAdapterUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

/**
 * @author Matthew Tambara
 */
public interface DynamicSection {

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *			 #modifySB(StringBundler, PageContext)}
	 */
	@Deprecated
	public com.liferay.portal.kernel.util.StringBundler modify(
			com.liferay.portal.kernel.util.StringBundler sb,
			PageContext pageContext)
		throws IOException, ServletException;

	public default StringBundler modifySB(
			StringBundler sb, PageContext pageContext)
		throws IOException, ServletException {

		return StringBundlerAdapterUtil.convertToPetraStringBundler(
			modify(
				StringBundlerAdapterUtil.convertToKernelStringBundler(sb),
				pageContext));
	}

}