package oz.infra.base64;

/* Encodes and decodes to and from Base64 notation.
 * Copyright (C) 2003 "Eric Glass" <jcifs at samba dot org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

public final class Base64Utils {

	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	private Base64Utils() {
		// default private
	}

	/**
	 * Base-64 encodes the supplied block of data. Line wrapping is not applied
	 * on output.
	 * 
	 * @param bytes
	 *            The block of data that is to be Base-64 encoded.
	 * @return A <code>String</code> containing the encoded data.
	 */
	public static String encode(final byte[] bytes) {
		int length = bytes.length;

		if (length == 0) {
			return "";
		}

		final StringBuilder buffer = new StringBuilder((int) Math.ceil(length / 3d) * 4);
		final int remainder = length % 3;
		length -= remainder;
		int block;
		int idx = 0;
		while (idx < length) {
			block = ((bytes[idx++] & 0xff) << 16) | ((bytes[idx++] & 0xff) << 8)
					| (bytes[idx++] & 0xff);
			buffer.append(ALPHABET.charAt(block >>> 18));
			buffer.append(ALPHABET.charAt((block >>> 12) & 0x3f));
			buffer.append(ALPHABET.charAt((block >>> 6) & 0x3f));
			buffer.append(ALPHABET.charAt(block & 0x3f));
		}
		if (remainder == 0) {
			return buffer.toString();
		}
		if (remainder == 1) {
			block = (bytes[idx] & 0xff) << 4;
			buffer.append(ALPHABET.charAt(block >>> 6));
			buffer.append(ALPHABET.charAt(block & 0x3f));
			buffer.append("==");
			return buffer.toString();
		}
		block = (((bytes[idx++] & 0xff) << 8) | ((bytes[idx]) & 0xff)) << 2;
		buffer.append(ALPHABET.charAt(block >>> 12));
		buffer.append(ALPHABET.charAt((block >>> 6) & 0x3f));
		buffer.append(ALPHABET.charAt(block & 0x3f));
		buffer.append("=");
		return buffer.toString();
	}

	/**
	 * Decodes the supplied Base-64 encoded string.
	 * 
	 * @param string
	 *            The Base-64 encoded string that is to be decoded.
	 * @return A <code>byte[]</code> containing the decoded data block.
	 */
	public static byte[] decode(final String string) {
		final int length = string.length();
		if (length == 0) {
			return new byte[0];
		}

		final int pad = (string.charAt(length - 2) == '=') ? 2
				: (string.charAt(length - 1) == '=') ? 1 : 0;
		final int size = length * 3 / 4 - pad;
		byte[] buffer = new byte[size];
		int block;
		int idx = 0;
		int index = 0;
		while (idx < length) {
			block = (ALPHABET.indexOf(string.charAt(idx++)) & 0xff) << 18
					| (ALPHABET.indexOf(string.charAt(idx++)) & 0xff) << 12
					| (ALPHABET.indexOf(string.charAt(idx++)) & 0xff) << 6
					| (ALPHABET.indexOf(string.charAt(idx++)) & 0xff);
			buffer[index++] = (byte) (block >>> 16);
			if (index < size) {
				buffer[index++] = (byte) ((block >>> 8) & 0xff);
			}
			if (index < size) {
				buffer[index++] = (byte) (block & 0xff);
			}
		}
		return buffer;
	}
}
