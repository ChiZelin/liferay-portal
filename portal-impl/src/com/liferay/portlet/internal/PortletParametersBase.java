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

package com.liferay.portlet.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.MutablePortletParameters;
import javax.portlet.PortletParameters;

import org.apache.ecs.xhtml.map;

/**
 * @author Neil Griffin
 */
public abstract class PortletParametersBase implements PortletParameters {

	public PortletParametersBase(Map<String, String[]> parameterMap) {
		this(parameterMap, null);
	}

	public PortletParametersBase(
		Map<String, String[]> parameterMap, String namespace) {

		_parameterMap = parameterMap;
		_namespace = namespace;
	}

	@Override
	public abstract MutablePortletParameters clone();

	@Override
	public Set<String> getNames() {
		if (_parameterMap == null) {
			return Collections.emptySet();
		}

		Set<String> keySet = _parameterMap.keySet();

		if (_namespace == null) {
			return keySet;
		}

		return new NameHashSet(keySet, _namespace);
	}

	@Override
	public String getValue(String name) {
		String[] values = getValues(name);

		if ((values == null) || (values.length < 1)) {
			return null;
		}

		return values[0];
	}

	@Override
	public String[] getValues(String name) {
		String[] values = _parameterMap.get(name);

		if (values != null) {
			return values;
		}

		if ((_namespace != null) && (name != null)) {
			values = _parameterMap.get(_namespace + name);

			if ((values == null) && name.startsWith(_namespace)) {
				values = _parameterMap.get(name.substring(_namespace.length()));
			}
		}

		return values;
	}

	@Override
	public boolean isEmpty() {
		return _parameterMap.isEmpty();
	}

	@Override
	public int size() {
		return _parameterMap.size();
	}

	protected Map<String, String[]> deepCopyMap(Map<String, String[]> map) {
		Map<String, String[]> copiedMap = new HashMap<>(map.size());
		Set<String> keySet = map.keySet();

		for (String key : keySet) {
			String[] values = map.get(key);

			if (values != null) {
				String[] copiedParameterValues = values.clone();

				copiedMap.put(key, copiedParameterValues);
			}
			else {
				copiedMap.put(key, null);
			}
		}

		return copiedMap;
	}

	protected Map<String, String[]> getParameterMap() {
		return _parameterMap;
	}

	private final String _namespace;
	private final Map<String, String[]> _parameterMap;

	private static class NameHashSet extends HashSet<String> {

		public NameHashSet(Set<String> names, String namespace) {
			for (String name : names) {
				if ((namespace != null) && name.startsWith(namespace)) {
					add(name.substring(namespace.length()));
				}
				else {
					add(name);
				}
			}

			_namespace = namespace;
		}

		@Override
		public boolean contains(Object name) {
			if (super.contains(name)) {
				return true;
			}

			String nameString = (String)name;

			if ((_namespace != null) && (nameString != null) &&
				nameString.startsWith(_namespace)) {

				return super.contains(
					nameString.substring(_namespace.length()));
			}

			return false;
		}

		private String _namespace;

	}

}