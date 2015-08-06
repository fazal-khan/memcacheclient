package com.mm.memcached;

import java.nio.ByteBuffer;

import com.mm.util.Util;

public abstract class MemcacheHandler {

	public final int			HEADER_LEN	= 24;
	public static final short	REQUEST		= 0x80;
	public static final short	RESPONSE	= 0x81;

	public static final byte	OP_GET		= 0x00;
	public static final byte	OP_SET		= 0x01;
	public static final byte	OP_ADD		= 0x02;
	public static final byte	OP_REPLACE	= 0x03;
	public static final byte	OP_DELETE	= 0x04;
	public static final byte	OP_INCR		= 0x05;
	public static final byte	OP_DECR		= 0x06;
	public static final byte	OP_QUIT		= 0x07;
	public static final byte	OP_FLUSH	= 0x08;
	public static final byte	OP_GETQ		= 0x09;
	public static final byte	OP_NOOP		= 0x0A;
	public static final byte	OP_VERSION	= 0x0B;
	public static final byte	OP_GETK		= 0x0C;
	public static final byte	OP_GETKQ	= 0x0D;
	public static final byte	OP_APPEND	= 0x0E;
	public static final byte	OP_PREPEND	= 0x0F;
	public static final byte	OP_STAT		= 0x10;
	public static final byte	OP_SETQ		= 0x11;
	public static final byte	OP_ADDQ		= 0x12;
	public static final byte	OP_REPLACEQ	= 0x13;
	public static final byte	OP_DELETEQ	= 0x14;
	public static final byte	OP_INCRQ	= 0x15;
	public static final byte	OP_DECRQ	= 0x16;
	public static final byte	OP_QUITQ	= 0x17;
	public static final byte	OP_FLUSHQ	= 0x18;
	public static final byte	OP_APPENDQ	= 0x19;
	public static final byte	OP_PREPENDQ	= 0x1A;

	public static final byte DATA_TYPE = 0x00; // currently not used.

	protected short		magic;
	protected byte		opcode;
	protected long		opaque;
	protected long		cas;
	protected int		datatype;
	protected int		status;
	protected int		totallen;
	protected byte[]	key;
	protected int		keylen;
	protected byte[]	value;
	protected int		valuelen;
	protected byte[]	extra;
	protected int		extralen;

	private String err = null;

	public short getMagic() {
		return magic;
	}

	public int getStatus() {
		return this.status;
	}

	public byte getOpcode() {
		return opcode;
	}

	public long getOpaque() {
		return this.opaque;
	}

	public long getCas() {
		return this.cas;
	}

	public int getDataType() {
		return this.datatype;
	}

	public int getTotalLength() {
		return this.totallen;
	}

	public byte[] getKey() {
		return this.key;
	}

	public byte[] getValue() {
		return this.value;
	}

	public byte[] getExtras() {
		return this.extra;
	}

	public void setError(String msg) {
		this.err = msg;
	}

	public String getError() {
		return this.err;
	}

	public boolean hasError() {
		return this.err != null;
	}

	public String getValueAsString() {
		if (this.value != null) {
			return new String(this.value, Util.UTF8_CHARSET);
		}
		return null;
	}

	public Long getValueAsLong() {
		if (this.value != null) {
			ByteBuffer buf = ByteBuffer.wrap(this.value);
			if (this.value.length >= 8) {
				return buf.getLong();
			}
			if (this.value.length >= 4) {
				return new Long(buf.getInt());
			}
		}
		return null;
	}
}
