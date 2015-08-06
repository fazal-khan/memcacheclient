package com.mm.memcached;

import java.nio.ByteBuffer;

public final class Request extends MemcacheHandler {

	public Request(byte opcode) {
		this.opcode = opcode;
	}

	public Request(byte opcode, byte[] key) {
		this.opcode = opcode;
		this.setKey(key);
	}

	public void setKey(byte[] key) {
		this.key = key;
		if (key != null) {
			keylen = key.length;
		}
	}

	public void setValue(byte[] value) {
		this.value = value;
		if (value != null) {
			this.valuelen = value.length;
		}
	}

	public void setExtra(byte[] extadata) {
		this.extra = extadata;
		if (this.extra != null) {
			this.extralen = extra.length;
		}
	}

	public byte[] encode() {

		int len = HEADER_LEN + keylen + valuelen + extralen;
		ByteBuffer buf = ByteBuffer.allocate(len);
		buf.put((byte) REQUEST);
		buf.put(opcode);
		buf.putShort((short) keylen);
		buf.put((byte) extralen);
		buf.put(DATA_TYPE);
		buf.putShort((short) 0);
		long totlen = keylen + valuelen + extralen;
		buf.putInt((int) totlen);
		buf.putInt((int) opaque);
		buf.putLong(cas);

		if (extra != null) {
			buf.put(extra, 0, extralen);
		}
		if (key != null) {
			buf.put(key, 0, keylen);
		}
		if (value != null) {
			buf.put(value, 0, valuelen);
		}
		return buf.array();
	}

	public String validate() {

		String msg = null;

		switch (this.opcode) {

		// Get, Get Quietly, Get Key, Get Key Quietly :-> MUST not have extras
		case OP_GET:
		case OP_GETQ:
		case OP_GETK:
		case OP_GETKQ:
			if (this.extra != null) {
				msg = "Opcode " + this.opcode + " cannot have extras";
			}
			break;

		// Set, Add, Replace :-> MUST have extras, MUST have key, MUST have
		// value.
		case OP_SET:
		case OP_ADD:
		case OP_REPLACE:
			if (this.extra == null) {
				msg = "Opcode " + this.opcode + " extras cannot be empty.";
			}
			if (this.key == null) {
				msg = "Opcode " + this.opcode + " key cannot be empty.";
			}
			if (this.value == null) {
				msg = "Opcode " + this.opcode + " value cannot be empty.";
			}
			break;

		// Delete :-> MUST NOT have extras, MUST have key, MUST Not have value.
		case OP_DELETE:
			if (this.extra != null) {
				msg = "Opcode " + this.opcode + " cannot have extras";
			}
			if (this.key == null) {
				msg = "Opcode " + this.opcode + " key cannot be empty.";
			}
			if (this.value != null) {
				msg = "Opcode " + this.opcode + " cannot have value.";
			}
			break;

		// Increment, Decrement :-> MUST have extras, MUST have key, MUST NOT
		// have value.
		case OP_INCR:
		case OP_DECR:
			if (this.extra == null) {
				msg = "Opcode " + this.opcode + " extras cannot be empty.";
			}
			if (this.key == null) {
				msg = "Opcode " + this.opcode + " key cannot be empty.";
			}
			if (this.value != null) {
				msg = "Opcode " + this.opcode + " cannot have value.";
			}
			break;

		// quit :-> MUST NOT have extras, MUST NOT have key.
		case OP_QUIT:
			if (this.extra != null) {
				msg = "Opcode " + this.opcode + " cannot have extras";
			}
			if (this.key != null) {
				msg = "Opcode " + this.opcode + " cannot have key.";
			}
			break;

		// flush :-> MAY have extras. MUST NOT have key. MUST NOT have value.
		case OP_FLUSH:
			if (this.key != null) {
				msg = "Opcode " + this.opcode + " cannot have key";
			}
			if (this.value != null) {
				msg = "Opcode " + this.opcode + " cannot have value.";
			}
			break;

		// noop, version :-> MUST NOT have extras. MUST NOT have key. MUST NOT
		// have value.
		case OP_NOOP:
		case OP_VERSION:
			if (this.extra != null) {
				msg = "Opcode " + this.opcode + " cannot have extras";
			}
			if (this.key != null) {
				msg = "Opcode " + this.opcode + " cannot have key";
			}
			if (this.value != null) {
				msg = "Opcode " + this.opcode + " cannot have value.";
			}
			break;

		// append, prepend :-> MUST NOT have extras. MUST have key. MUST have
		// value.
		case OP_APPEND:
		case OP_PREPEND:
			if (this.extra != null) {
				msg = "Opcode " + this.opcode + " cannot have extras.";
			}
			if (this.key == null) {
				msg = "Opcode " + this.opcode + " key cannot be empty.";
			}
			if (this.value == null) {
				msg = "Opcode " + this.opcode + " value cannot be empty.";
			}
			break;

		// stat :-> MUST NOT have extras. MAY have key. MUST NOT have value.
		case OP_STAT:
			if (this.extra != null) {
				msg = "Opcode " + this.opcode + " cannot have extras.";
			}
			if (this.value != null) {
				msg = "Opcode " + this.opcode + " cannot have value.";
			}
			break;

		}

		return msg;
	}
}
