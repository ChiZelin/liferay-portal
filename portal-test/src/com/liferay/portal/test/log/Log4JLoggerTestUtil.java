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

package com.liferay.portal.test.log;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogWrapper;
import com.liferay.portal.kernel.test.ReflectionTestUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Shuyang Zhou
 */
public class Log4JLoggerTestUtil {

	public static CaptureAppender configureLog4JLogger(
		String name, Level level) {

		LogWrapper logWrapper = (LogWrapper)LogFactoryUtil.getLog(name);

		Log log = logWrapper.getWrappedLog();

		try {
			Class<?> clazz = Class.forName(
				"com.liferay.portal.log.Log4jLogImpl");

			if (!clazz.isInstance(log)) {
				throw new IllegalStateException(
					"Log " + name + " is not a Log4j logger");
			}
		}
		catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(cnfe);
		}

		Logger logger = ReflectionTestUtil.invoke(
			log, "getWrappedLogger", new Class<?>[0]);

		CaptureAppender captureAppender = new CaptureAppender(logger);

		logger.addAppender(captureAppender);

		logger.setLevel(level);

		return captureAppender;
	}

	public static Level setLoggerLevel(String name, Level level) {
		LogWrapper logWrapper = (LogWrapper)LogFactoryUtil.getLog(name);

		Log log = logWrapper.getWrappedLog();

		try {
			Class<?> clazz = Class.forName(
				"com.liferay.portal.log.Log4jLogImpl");

			if (!clazz.isInstance(log)) {
				throw new IllegalStateException(
					"Log " + name + " is not a Log4j logger");
			}
		}
		catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(cnfe);
		}

		Logger logger = ReflectionTestUtil.invoke(
			log, "getWrappedLogger", new Class<?>[0]);

		Level oldLevel = logger.getLevel();

		logger.setLevel(level);

		return oldLevel;
	}

}