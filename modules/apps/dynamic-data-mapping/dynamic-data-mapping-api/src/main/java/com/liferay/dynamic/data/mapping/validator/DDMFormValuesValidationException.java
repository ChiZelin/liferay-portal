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

package com.liferay.dynamic.data.mapping.validator;

import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.petra.string.StringBundler;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Marcellus Tavares
 */
@ProviderType
public class DDMFormValuesValidationException extends StorageException {

	public DDMFormValuesValidationException() {
	}

	public DDMFormValuesValidationException(String msg) {
		super(msg);
	}

	public DDMFormValuesValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DDMFormValuesValidationException(Throwable cause) {
		super(cause);
	}

	public static class MustNotSetValue
		extends DDMFormValuesValidationException {

		public MustNotSetValue(String fieldName) {
			super(
				"Value should not be set for transient field name ".concat(
					fieldName));

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private String _fieldName;

	}

	public static class MustSetValidAvailableLocales
		extends DDMFormValuesValidationException {

		public MustSetValidAvailableLocales(String fieldName) {
			super(
				"Invalid available locales set for field name ".concat(
					fieldName));

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private String _fieldName;

	}

	public static class MustSetValidDefaultLocale
		extends DDMFormValuesValidationException {

		public MustSetValidDefaultLocale(String fieldName) {
			super(
				"Invalid default locale set for field name ".concat(fieldName));

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private String _fieldName;

	}

	public static class MustSetValidField
		extends DDMFormValuesValidationException {

		public MustSetValidField(String fieldName) {
			super(
				StringBundler.concat(
					"There is no field name ", fieldName, " defined on form"));

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private String _fieldName;

	}

	public static class MustSetValidValue
		extends DDMFormValuesValidationException {

		public MustSetValidValue(String fieldName) {
			super("Invalid value set for field name ".concat(fieldName));

			_fieldName = fieldName;
		}

		public MustSetValidValue(String fieldName, Throwable cause) {
			super("Invalid value set for field name ".concat(fieldName), cause);

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private String _fieldName;

	}

	public static class MustSetValidValuesSize
		extends DDMFormValuesValidationException {

		public MustSetValidValuesSize(String fieldName) {
			super(
				"Incorrect number of values set for field name ".concat(
					fieldName));

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private String _fieldName;

	}

	public static class RequiredValue extends DDMFormValuesValidationException {

		public RequiredValue(String fieldName) {
			super("No value defined for field name ".concat(fieldName));

			_fieldName = fieldName;
		}

		public String getFieldName() {
			return _fieldName;
		}

		private final String _fieldName;

	}

}