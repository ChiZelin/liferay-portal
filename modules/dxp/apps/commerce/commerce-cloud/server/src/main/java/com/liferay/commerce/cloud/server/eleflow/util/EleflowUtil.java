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

package com.liferay.commerce.cloud.server.eleflow.util;

import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Andrea Di Giorgi
 */
public class EleflowUtil {

	public static <D extends Enum<D>, E extends Enum<E>> D fromEleflow(
		E value, Class<D> clazz) {

		String name = value.toString();

		name = name.replace('-', '_');
		name = name.toUpperCase();

		return Enum.valueOf(clazz, name);
	}

	public static <D extends Enum<D>, E extends Enum<E>> Set<D> fromEleflow(
		Iterable<E> values, Class<D> clazz) {

		EnumSet<D> enumSet = EnumSet.noneOf(clazz);

		for (E value : values) {
			enumSet.add(fromEleflow(value, clazz));
		}

		return enumSet;
	}

	public static String getHost(JsonObject configJsonObject) {
		return configJsonObject.getString(
			"ELEFLOW_HOST", _DEFAULT_ELEFLOW_HOST);
	}

	public static int getInteger(BigDecimal bigDecimal) {
		return bigDecimal.intValue();
	}

	public static OffsetDateTime getOffsetDateTime(long time) {
		Instant instant = Instant.ofEpochMilli(time);

		return OffsetDateTime.ofInstant(instant, ZoneId.of("UTC"));
	}

	public static String getPath(JsonObject configJsonObject) {
		return configJsonObject.getString(
			"ELEFLOW_PATH", _DEFAULT_ELEFLOW_PATH);
	}

	public static String getString(BigDecimal bigDecimal) {
		return bigDecimal.toString();
	}

	public static <D extends Enum<D>, E extends Enum<E>> E toEleflow(
		D value, Function<String, E> function) {

		String name = value.toString();

		name = name.replace('_', '-');
		name = name.toLowerCase();

		return function.apply(name);
	}

	public static <D extends Enum<D>, E extends Enum<E>> List<E> toEleflow(
		Set<D> values, Function<String, E> function) {

		List<E> list = new ArrayList<>(values.size());

		for (D value : values) {
			list.add(toEleflow(value, function));
		}

		return list;
	}

	private static final String _DEFAULT_ELEFLOW_HOST =
		"b2l9j0pz3i.execute-api.us-west-2.amazonaws.com";

	private static final String _DEFAULT_ELEFLOW_PATH = "/Prod";

}