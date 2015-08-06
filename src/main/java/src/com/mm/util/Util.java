package com.mm.util;

import java.nio.charset.Charset;

public final class Util {

	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	public static final void log(String msg) {
		System.out.println(msg);
	}

	public static byte[] intToBytes(int[] values) {
		if (values == null || values.length == 0) {
			return new byte[0];
		}
		byte[] res = new byte[4 * values.length];
		for (int i = 0; i < values.length; i++) {
			intToBytes(values[i], res, 4 * i);
		}
		return res;
	}

	public static byte[] intToBytes(int i, byte[] res, int offset) {
		if (res == null) {
			res = new byte[4];
			offset = 0;
		}
		res[offset] = (byte) (i >>> 24);
		res[offset + 1] = (byte) (i >>> 16);
		res[offset + 2] = (byte) (i >>> 8);
		res[offset + 3] = (byte) (i >>> 0);

		return res;
	}

	public static byte[] longToBytes(long[] values) {
		if (values == null || values.length == 0) {
			return new byte[0];
		}
		byte[] res = new byte[8 * values.length];
		for (int i = 0; i < values.length; i++) {
			longToBytes(values[i], res, 8 * i);
		}
		return res;
	}

	public static byte[] longToBytes(long l, byte[] res, int offset) {
		if (res == null) {
			res = new byte[8];
			offset = 0;
		}
		res[offset] = (byte) (l >>> 56);
		res[offset + 1] = (byte) (l >>> 48);
		res[offset + 2] = (byte) (l >>> 40);
		res[offset + 3] = (byte) (l >>> 32);
		res[offset + 4] = (byte) (l >>> 24);
		res[offset + 5] = (byte) (l >>> 16);
		res[offset + 6] = (byte) (l >>> 8);
		res[offset + 7] = (byte) (l >>> 0);

		return res;
	}

	public static String toHex(byte[] bytes) {

		String hex = "";
		if (bytes == null)
			return hex;
		hex = "0x";
		for (int i = 0; i < bytes.length; i++) {
			String temp = Integer.toHexString(bytes[i]);
			hex += temp.length() == 2 ? temp : "0" + temp;
		}
		return hex;
	}

	public static byte[] getBytes(Object value) {
		return value.toString().getBytes(UTF8_CHARSET);
	}

}
