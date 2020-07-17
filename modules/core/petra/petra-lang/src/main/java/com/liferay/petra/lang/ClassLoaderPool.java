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

		int index = contextName.lastIndexOf("_");

		if ((index == -1) || (index == (contextName.length() - 1))) {
			return;
		}

		Version version = Version.parse(contextName.substring(index + 1));

		if (version == null) {
			return;
		}

		_fallbackClassLoaders.compute(
			contextName.substring(0, index),
			(key, classLoaders) -> {
				if (classLoaders == null) {
					classLoaders = new ConcurrentSkipListMap<>();
				}

				classLoaders.put(version, classLoader);

				return classLoaders;
			});
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
		int index = contextName.lastIndexOf("_");

		if ((index == -1) || (index == (contextName.length() - 1))) {
			return;
		}

		Version version = Version.parse(contextName.substring(index + 1));

		if (version == null) {
			return;
		}

		_fallbackClassLoaders.computeIfPresent(
			contextName.substring(0, index),
			(key, classLoaders) -> {
				classLoaders.remove(version);

				if (classLoaders.isEmpty()) {
					return null;
				}

				return classLoaders;
			});
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

		public static Version parse(String version) {
			int major;
			int minor = 0;
			int micro = 0;
			String qualifier = "";

			try {
				StringTokenizer stringTokenizer = new StringTokenizer(
					version, _SEPARATOR, true);

				major = Integer.parseInt(stringTokenizer.nextToken());

				if (stringTokenizer.hasMoreTokens()) {
					stringTokenizer.nextToken();

					minor = Integer.parseInt(stringTokenizer.nextToken());

					if (stringTokenizer.hasMoreTokens()) {
						stringTokenizer.nextToken();

						micro = Integer.parseInt(stringTokenizer.nextToken());

						if (stringTokenizer.hasMoreTokens()) {
							stringTokenizer.nextToken();

							qualifier = stringTokenizer.nextToken("");
						}
					}
				}
			}
			catch (Exception exception) {
				return null;
			}

			if ((major < 0) || (minor < 0) || (micro < 0)) {
				return null;
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

				return null;
			}

			return new Version(major, minor, micro, qualifier);
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

		@Override
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

		@Override
		public int hashCode() {
			int hash = _hash;

			if (hash != 0) {
				return hash;
			}

			hash = 31 * 17;
			hash = (31 * hash) + _major;
			hash = (31 * hash) + _minor;
			hash = (31 * hash) + _micro;
			hash = (31 * hash) + _qualifier.hashCode();

			return _hash = hash;
		}

		private Version(int major, int minor, int micro, String qualifier) {
			_major = major;
			_minor = minor;
			_micro = micro;
			_qualifier = qualifier;
		}

		private static final String _SEPARATOR = ".";

		private transient int _hash;
		private final int _major;
		private final int _micro;
		private final int _minor;
		private final String _qualifier;

	}

}