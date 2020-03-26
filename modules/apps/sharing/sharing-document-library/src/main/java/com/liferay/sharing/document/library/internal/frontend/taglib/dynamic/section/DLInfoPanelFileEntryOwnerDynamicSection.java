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

package com.liferay.sharing.document.library.internal.frontend.taglib.dynamic.section;

import com.liferay.frontend.taglib.dynamic.section.DynamicSection;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringBundlerAdapterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.sharing.configuration.SharingConfiguration;
import com.liferay.sharing.configuration.SharingConfigurationFactory;
import com.liferay.taglib.servlet.PipingServletResponse;

import java.io.ByteArrayOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = "name=com.liferay.document.library.web#/document_library/info_panel_file_entry.jsp#fileEntryOwner",
	service = DynamicSection.class
)
public class DLInfoPanelFileEntryOwnerDynamicSection implements DynamicSection {

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *			 #modifySB(StringBundler, PageContext)}
	 */
	@Deprecated
	@Override
	public com.liferay.portal.kernel.util.StringBundler modify(
		com.liferay.portal.kernel.util.StringBundler sb,
		PageContext pageContext) {

		return StringBundlerAdapterUtil.convertToKernelStringBundler(
			modifySB(
				StringBundlerAdapterUtil.convertToPetraStringBundler(sb),
				pageContext));
	}

	@Override
	public StringBundler modifySB(StringBundler sb, PageContext pageContext) {
		HttpServletRequest httpServletRequest =
			(HttpServletRequest)pageContext.getRequest();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		SharingConfiguration sharingConfiguration =
			_sharingConfigurationFactory.getGroupSharingConfiguration(
				themeDisplay.getSiteGroup());

		if (!sharingConfiguration.isEnabled()) {
			return sb;
		}

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(
				"/META-INF/resources/dynamic_section" +
					"/info_panel_file_entry.jsp");

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			HttpServletResponse httpServletResponse = new PipingServletResponse(
				(HttpServletResponse)pageContext.getResponse(), outputStream);

			requestDispatcher.include(
				pageContext.getRequest(), httpServletResponse);

			return new StringBundler(new String(outputStream.toByteArray()));
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.sharing.document.library)"
	)
	private ServletContext _servletContext;

	@Reference
	private SharingConfigurationFactory _sharingConfigurationFactory;

}