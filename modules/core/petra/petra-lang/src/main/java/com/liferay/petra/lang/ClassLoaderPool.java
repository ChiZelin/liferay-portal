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

package com.liferay.petra.lang;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Maps servlet context names to/from the servlet context's class loader.
 *
 * @author Shuyang Zhou
 */
public class ClassLoaderPool {

	/**
	 * Returns the class loader associated with the context name.
	 *
	 * <p>
	 * If no class loader is found for the context name, the thread's context
	 * class loader is returned as a fallback.
	 * </p>
	 *
	 * @param  contextName the servlet context's name
	 * @return the class loader associated with the context name
	 */
	public static ClassLoader getClassLoader(String contextName) {
		ClassLoader classLoader = null;

		if ((contextName != null) && !contextName.equals("null")) {
			classLoader = _classLoaders.get(contextName);

			if (classLoader == null) {
				int pos = contextName.lastIndexOf("_");

				if (pos > 0) {
					ConcurrentNavigableMap<Version, ClassLoader> classLoaders =
						_fallbackClassLoaders.get(
							contextName.substring(0, pos));

					if (classLoaders != null) {
						Map.Entry<Version, ClassLoader> entry =
							classLoaders.lastEntry();

						if (entry != null) {
							classLoader = entry.getValue();
						}
					}
				}
			}
		}

		if (classLoader == null) {
			Thread currentThread = Thread.currentThread();

			classLoader = currentThread.getContextClassLoader();
		}

		return classLoader;
	}

	/**
	 * Returns the context name associated with the class loader.
	 *
	 * <p>
	 * If the class loader is <code>null</code> or if no context name is
	 * associated with the class loader, {@link <code>"null"</code>} is
	 * returned.
	 * </p>
	 *
	 * @param  classLoader the class loader
	 * @return the context name associated with the class loader
	 */
	public static String getContextName(ClassLoader classLoader) {
		if (classLoader == null) {
			return "null";
		}

		String contextName = _contextNames.get(classLoader);

		if (contextName == null) {
			contextName = "null";
		}

		return contextName;
	}

	public static void register(String contextName, ClassLoader classLoader) {
		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);

		int pos = contextName.lastIndexOf("_");

		if (pos > 0) {
			try {
				Version version = new Version(contextName.substring(pos + 1));

				_fallbackClassLoaders.compute(
					contextName.substring(0, pos),
					(key, classLoaders) -> {
						if (classLoaders == null) {
							classLoaders = new ConcurrentSkipListMap<>();
						}

						classLoaders.put(version, classLoader);

						return classLoaders;
					});
			}
			catch (Exception exception) {
				if (!Objects.equals(
						exception.getMessage(), "Invalid version")) {

					throw new RuntimeException(exception);
				}
			}
		}
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);

			_unregisterFallback(contextName);
		}
	}

	public static void unregister(String contextName) {
		ClassLoader classLoader = _classLoaders.remove(contextName);

		if (classLoader != null) {
			_contextNames.remove(classLoader);

			_unregisterFallback(contextName);
		}
	}

	private static void _unregisterFallback(String contextName) {
		int pos = contextName.lastIndexOf("_");

		if (pos > 0) {
			try {
				Version version = new Version(contextName.substring(pos + 1));

				_fallbackClassLoaders.computeIfPresent(
					contextName.substring(0, pos),
					(key, classLoaders) -> {
						classLoaders.remove(version);

						if (classLoaders.isEmpty()) {
							return null;
						}

						return classLoaders;
					});
			}
			catch (Exception exception) {
				if (!Objects.equals(
						exception.getMessage(), "Invalid version")) {

					throw new RuntimeException(exception);
				}
			}
		}
	}

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();
	private static final Map
		<String, ConcurrentNavigableMap<Version, ClassLoader>>
			_fallbackClassLoaders = new ConcurrentHashMap<>();

	static {
		register("SystemClassLoader", ClassLoader.getSystemClassLoader());
		register("GlobalClassLoader", ClassLoaderPool.class.getClassLoader());
	}

	private static class Version implements Comparable<Version> {

		public Version(String version) throws Exception {
			int major;
			int minor = 0;
			int micro = 0;
			String qualifier = "";

			try {
				StringTokenizer stringTokenizer = new StringTokenizer(
					version, _SEPARATOR, true);

				major = _parseInt(stringTokenizer.nextToken(), version);

				if (stringTokenizer.hasMoreTokens()) {
					stringTokenizer.nextToken();

					minor = _parseInt(stringTokenizer.nextToken(), version);

					if (stringTokenizer.hasMoreTokens()) {
						stringTokenizer.nextToken();

						micro = _parseInt(stringTokenizer.nextToken(), version);

						if (stringTokenizer.hasMoreTokens()) {
							stringTokenizer.nextToken();

							qualifier = stringTokenizer.nextToken("");
						}
					}
				}
			}
			catch (NoSuchElementException noSuchElementException) {
				throw new Exception("Invalid version", noSuchElementException);
			}
			catch (IllegalArgumentException illegalArgumentException) {
				throw new Exception(
					"Invalid version", illegalArgumentException);
			}

			if ((major < 0) || (minor < 0) || (micro < 0)) {
				throw new Exception("Invalid version");
			}

			for (char ch : qualifier.toCharArray()) {
				if (('A' <= ch) && (ch <= 'Z')) {
					continue;
				}

				if (('a' <= ch) && (ch <= 'z')) {
					continue;
				}

				if (('0' <= ch) && (ch <= '9')) {
					continue;
				}

				if ((ch == '_') || (ch == '-')) {
					continue;
				}

				throw new Exception("Invalid version");
			}

			_major = major;
			_minor = minor;
			_micro = micro;
			_qualifier = qualifier;
		}

		@Override
		public int compareTo(Version other) {
			if (other == this) {
				return 0;
			}

			int result = _major - other._major;

			if (result != 0) {
				return result;
			}

			result = _minor - other._minor;

			if (result != 0) {
				return result;
			}

			result = _micro - other._micro;

			if (result != 0) {
				return result;
			}

			return _qualifier.compareTo(other._qualifier);
		}

		public boolean equals(Object object) {
			if (object == this) {
				return true;
			}

			if (!(object instanceof Version)) {
				return false;
			}

			Version other = (Version)object;

			if ((_major == other._major) && (_minor == other._minor) &&
				(_micro == other._micro) &&
				_qualifier.equals(other._qualifier)) {

				return true;
			}

			return false;
		}

		public int hashCode() {
			int h = _hash;

			if (h != 0) {
				return h;
			}

			h = 31 * 17;
			h = (31 * h) + _major;
			h = (31 * h) + _minor;
			h = (31 * h) + _micro;
			h = (31 * h) + _qualifier.hashCode();

			return _hash = h;
		}

		public String toString() {
			String s = _versionString;

			if (s != null) {
				return s;
			}

			int q = _qualifier.length();

			StringBuffer result = new StringBuffer(20 + q);

			result.append(_major);
			result.append(_SEPARATOR);
			result.append(_minor);
			result.append(_SEPARATOR);
			result.append(_micro);

			if (q > 0) {
				result.append(_SEPARATOR);
				result.append(_qualifier);
			}

			return _versionString = result.toString();
		}

		private static int _parseInt(String value, String version) {
			try {
				return Integer.parseInt(value);
			}
			catch (NumberFormatException numberFormatException) {
				IllegalArgumentException illegalArgumentException =
					new IllegalArgumentException(
						"invalid version \"" + version + "\": non-numeric \"" +
							value + "\"");

				illegalArgumentException.initCause(numberFormatException);

				throw illegalArgumentException;
			}
		}

		private static final String _SEPARATOR = ".";

		private transient int _hash;
		private final int _major;
		private final int _micro;
		private final int _minor;
		private final String _qualifier;
		private transient String _versionString;

	}

}