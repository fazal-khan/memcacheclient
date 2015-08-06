package com.mm.memcached;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.mm.util.Util;

public final class Response extends MemcacheHandler {

	public void decode(InputStream is) throws IOException {

		decodeHeader(read(is, HEADER_LEN));
		decodeBody(read(is, getTotalLength()));

		if (this.status != 0 && this.value != null) {
			this.setError(new String(this.value, Util.UTF8_CHARSET));
		}
	}

	void decodeHeader(byte[] head) {

		if (head == null)
			throw new NullPointerException("Header buffer cannot be null.");
		if (head.length != HEADER_LEN)
			throw new IllegalArgumentException("Invalid heder buffer size, expected 24 found " + head.length);

		if (head[0] != (byte) RESPONSE) {
			throw new RuntimeException("Invalid response code");
		}

		ByteBuffer buf = ByteBuffer.wrap(head);
		buf.get();
		this.opcode = buf.get();
		this.keylen = buf.getShort();
		this.extralen = ((int) buf.get()) & 0xff;
		this.datatype = ((int) buf.get()) & 0xff;
		this.status = ((int) buf.getShort()) & 0xffff;
		this.totallen = buf.getInt();
		this.opaque = ((long) buf.getInt()) & 0xffff_ffff;
		this.cas = buf.getLong();
	}

	void decodeBody(byte[] body) {

		ByteBuffer buff = ByteBuffer.wrap(body);

		if (extralen > 0) {
			this.extra = new byte[extralen];
			buff.get(this.extra);
		}

		if (keylen > 0) {
			this.key = new byte[keylen];
			buff.get(this.key);
		}

		int valuelen = totallen - (keylen + extralen);
		if (valuelen > 0) {
			this.value = new byte[valuelen];
			buff.get(this.value);
		}
	}

	byte[] read(InputStream is, int size) throws IOException {

		// log("In read: size requested is :" + size);
		if (size <= 0)
			return new byte[0];
		byte[] buff = new byte[size];
		int total = 0;
		int read = -1;
		while ((read = is.read(buff, total, size - total)) != -1) {
			total += read;
			if (total == size)
				break;
		}
		// log("In read: size returning is :" + buff.length);
		return buff;
	}

}
