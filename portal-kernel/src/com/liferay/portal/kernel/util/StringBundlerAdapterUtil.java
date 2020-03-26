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

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.StringBundler;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Leon Chi
 */
public class StringBundlerAdapterUtil {

	public static com.liferay.portal.kernel.util.StringBundler
		convertToKernelStringBundler(StringBundler petraSB) {

		return new com.liferay.portal.kernel.util.StringBundler() {

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				boolean b) {

				petraSB.append(b);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(char c) {
				petraSB.append(c);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				char[] chars) {

				petraSB.append(chars);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				com.liferay.portal.kernel.util.StringBundler sb) {

				petraSB.append(sb.getStrings());

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				double d) {

				petraSB.append(d);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				float f) {

				petraSB.append(f);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(int i) {
				petraSB.append(i);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(long l) {
				petraSB.append(l);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				Object obj) {

				petraSB.append(obj);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				String s) {

				petraSB.append(s);

				return this;
			}

			@Override
			public com.liferay.portal.kernel.util.StringBundler append(
				String[] stringArray) {

				petraSB.append(stringArray);

				return this;
			}

			@Override
			public int capacity() {
				return petraSB.capacity();
			}

			@Override
			public String[] getStrings() {
				return petraSB.getStrings();
			}

			@Override
			public int index() {
				return petraSB.index();
			}

			@Override
			public int length() {
				return petraSB.length();
			}

			@Override
			public void setIndex(int newIndex) {
				petraSB.setIndex(newIndex);
			}

			@Override
			public void setStringAt(String s, int index) {
				petraSB.setStringAt(s, index);
			}

			@Override
			public String stringAt(int index) {
				return petraSB.stringAt(index);
			}

			@Override
			public String toString() {
				return petraSB.toString();
			}

			@Override
			public void writeTo(Writer writer) throws IOException {
				petraSB.writeTo(writer);
			}

		};
	}

	public static StringBundler convertToPetraStringBundler(
		com.liferay.portal.kernel.util.StringBundler kernelSB) {

		return new StringBundler() {

			@Override
			public StringBundler append(boolean b) {
				kernelSB.append(b);

				return this;
			}

			@Override
			public StringBundler append(char c) {
				kernelSB.append(c);

				return this;
			}

			@Override
			public StringBundler append(char[] chars) {
				kernelSB.append(chars);

				return this;
			}

			@Override
			public StringBundler append(double d) {
				kernelSB.append(d);

				return this;
			}

			@Override
			public StringBundler append(float f) {
				kernelSB.append(f);

				return this;
			}

			@Override
			public StringBundler append(int i) {
				kernelSB.append(i);

				return this;
			}

			@Override
			public StringBundler append(long l) {
				kernelSB.append(l);

				return this;
			}

			@Override
			public StringBundler append(Object obj) {
				kernelSB.append(obj);

				return this;
			}

			@Override
			public StringBundler append(String s) {
				kernelSB.append(s);

				return this;
			}

			@Override
			public StringBundler append(String[] stringArray) {
				kernelSB.append(stringArray);

				return this;
			}

			@Override
			public StringBundler append(StringBundler sb) {
				kernelSB.append(sb.getStrings());

				return this;
			}

			@Override
			public int capacity() {
				return kernelSB.capacity();
			}

			@Override
			public String[] getStrings() {
				return kernelSB.getStrings();
			}

			@Override
			public int index() {
				return kernelSB.index();
			}

			@Override
			public int length() {
				return kernelSB.length();
			}

			@Override
			public void setIndex(int newIndex) {
				kernelSB.setIndex(newIndex);
			}

			@Override
			public void setStringAt(String s, int index) {
				kernelSB.setStringAt(s, index);
			}

			@Override
			public String stringAt(int index) {
				return kernelSB.stringAt(index);
			}

			@Override
			public String toString() {
				return kernelSB.toString();
			}

			@Override
			public void writeTo(Writer writer) throws IOException {
				kernelSB.writeTo(writer);
			}

		};
	}

}