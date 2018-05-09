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

package com.liferay.portlet;

import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Dante Wang
 */
public class AsyncPortletServletResponse extends HttpServletResponseWrapper {

	public AsyncPortletServletResponse(HttpServletResponse response) {
		super(response);

		_originalResponse = response;

		while (_originalResponse instanceof ServletResponseWrapper) {
			_originalResponse =
				((ServletResponseWrapper)_originalResponse).getResponse();
		}
	}

	@Override
	public void resetBuffer() {
		if (!super.isCommitted()) {
			super.resetBuffer();
		}
	}

	@Override
	public boolean isCommitted() {
		return _originalResponse.isCommitted();
	}

	private ServletResponse _originalResponse;

}