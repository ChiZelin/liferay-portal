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
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.util.StringBundlerAdapterUtil;
import com.liferay.taglib.servlet.PipingServletResponse;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * @author Matthew Tambara
 */
public abstract class BaseJSPDynamicSection implements DynamicSection {

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *			 #modifySB(StringBundler, PageContext)}
	 */
	@Deprecated
	@Override
	public com.liferay.portal.kernel.util.StringBundler modify(
			com.liferay.portal.kernel.util.StringBundler sb,
			PageContext pageContext)
		throws IOException, ServletException {

		return StringBundlerAdapterUtil.convertToKernelStringBundler(
			modifySB(
				StringBundlerAdapterUtil.convertToPetraStringBundler(sb),
				pageContext));
	}

	@Override
	public StringBundler modifySB(StringBundler sb, PageContext pageContext)
		throws IOException, ServletException {

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher(getJspPath());

		try (UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter()) {
			HttpServletResponse httpServletResponse = new PipingServletResponse(
				(HttpServletResponse)pageContext.getResponse(),
				unsyncStringWriter);

			requestDispatcher.include(
				pageContext.getRequest(), httpServletResponse);

			return unsyncStringWriter.getSB();
		}
	}

	protected abstract String getJspPath();

	protected abstract ServletContext getServletContext();

}