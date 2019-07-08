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

package com.liferay.document.library.repository.authorization.oauth2;

import com.liferay.document.library.repository.authorization.capability.AuthorizationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Adolfo Pérez
 */
public class OAuth2AuthorizationException extends AuthorizationException {

	public static final OAuth2AuthorizationException getErrorException(
		String error, String description) {

		Function<String, OAuth2AuthorizationException> function =
			_functions.getOrDefault(error, OAuth2AuthorizationException::new);

		return function.apply(description);
	}

	public static class AccessDenied extends OAuth2AuthorizationException {

		public AccessDenied(String description) {
			super(
				"The client is not authorized to request an authorization " +
					"code using method: ".concat(description));
		}

	}

	public static class InvalidRequest extends OAuth2AuthorizationException {

		public InvalidRequest(String description) {
			super(
				("The request is missing a required parameter, includes an " +
					"invalid parameter value, includes a parameter more than " +
						"once, or is otherwise malformed: ").concat(
							description));
		}

	}

	public static class InvalidScope extends OAuth2AuthorizationException {

		public InvalidScope(String description) {
			super(
				"The requested scope is invalid, unknown, or malformed: ".
					concat(description));
		}

	}

	public static class InvalidState extends OAuth2AuthorizationException {

		public InvalidState(String state) {
			super(
				"The resource owner returned an invalid state value: ".concat(
					state));
		}

	}

	public static class ServerError extends OAuth2AuthorizationException {

		public ServerError(String description) {
			super(
				"The authorization server encountered an unexpected " +
					"condition that prevented it from fulfilling the " +
						"request: ".concat(description));
		}

	}

	public static class TemporarilyUnavailable
		extends OAuth2AuthorizationException {

		public TemporarilyUnavailable(String description) {
			super(
				"The authorization server is currently unable to handle the " +
					"request due to a temporary overloading or maintenance " +
						"of the server: ".concat(description));
		}

	}

	public static class UnauthorizedClient
		extends OAuth2AuthorizationException {

		public UnauthorizedClient(String description) {
			super(
				"The resource owner or authorization server denied the " +
					"request: ".concat(description));
		}

	}

	public static class UnsupportedResponseType
		extends OAuth2AuthorizationException {

		public UnsupportedResponseType(String description) {
			super(
				("The authorization server does not support obtaining an" +
					"authorization code using this method: ").concat(
						description));
		}

	}

	protected OAuth2AuthorizationException() {
	}

	protected OAuth2AuthorizationException(String msg) {
		super(msg);
	}

	protected OAuth2AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	protected OAuth2AuthorizationException(Throwable cause) {
		super(cause);
	}

	private static final Map
		<String, Function<String, OAuth2AuthorizationException>> _functions =
			new HashMap<String, Function<String, OAuth2AuthorizationException>>(
				7) {

				{
					put(
						"access_denied",
						OAuth2AuthorizationException.AccessDenied::new);
					put(
						"invalid_request",
						OAuth2AuthorizationException.InvalidRequest::new);
					put(
						"invalid_scope",
						OAuth2AuthorizationException.InvalidScope::new);
					put(
						"server_error",
						OAuth2AuthorizationException.ServerError::new);
					put(
						"temporarily_unavailable",
						OAuth2AuthorizationException.TemporarilyUnavailable::
							new);
					put(
						"unauthorized_client",
						OAuth2AuthorizationException.UnauthorizedClient::new);
					put(
						"unsupported_response_type",
						OAuth2AuthorizationException.UnsupportedResponseType::
							new);
				}
			};

}