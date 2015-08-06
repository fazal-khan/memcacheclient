package com.mm.memcached;

import static com.mm.util.Util.UTF8_CHARSET;
import static com.mm.util.Util.getBytes;
import static com.mm.util.Util.intToBytes;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class AbstractClient extends Client {

	public AbstractClient(String host, int port) throws Exception {
		super(host, port);
	}

	// ----------------------------------------------------------------------------->
	// ----- implementation of functionalities as per specs.
	// ----------------------------------------------------------------------------->

	// 4.2 get, getq, getqk, getk
	public Response get(String key) throws IOException {
		return get(key, false, false);
	}

	public Response get(String key, boolean needKey, boolean quiet) throws IOException {
		byte opcode = needKey && quiet ? Request.OP_GETKQ
				: needKey ? Request.OP_GETK : quiet ? Request.OP_GETQ : Request.OP_GET;
		Request req = new Request(opcode, getBytes(key));
		return this.sendRequest(req);
	}

	// 4.3 add, set and replace
	public Response add(String key, Object value, int extra, int expiry) throws IOException {
		return addReplaceSet(Request.OP_ADD, key, value, extra, expiry);
	}

	public Response set(String key, Object value, int extra, int expiry) throws IOException {
		return addReplaceSet(Request.OP_SET, key, value, extra, expiry);
	}

	public Response replace(String key, Object value, int extra, int expiry) throws IOException {
		return addReplaceSet(Request.OP_REPLACE, key, value, extra, expiry);
	}

	// 4.4 delete
	public Response del(String key) throws IOException {
		Request req = new Request(Request.OP_DELETE, getBytes(key));
		return this.sendRequest(req);
	}

	public Response delQuiet(String key) throws IOException {
		Request req = new Request(Request.OP_DELETEQ, getBytes(key));
		return this.sendRequest(req);
	}

	// 4.4 delete helper
	public boolean delete(String key) throws IOException {
		Response res = del(key);
		return res.hasError() ? false : true;
	}

	// 4.5 incr
	public Response incr(String key, long delta, long initValue, int expiry) throws IOException {
		return incrOrDecr(Request.OP_INCR, key, delta, initValue, expiry);
	}

	// 4.5 decr
	public Response decr(String key, long delta, long initValue, int expiry) throws IOException {
		return incrOrDecr(Request.OP_DECR, key, delta, initValue, expiry);
	}

	private Response incrOrDecr(byte opcode, String key, long delta, long initValue, int expiry) throws IOException {
		byte[] extras = new byte[8 * 2 + 4];

		ByteBuffer buf = ByteBuffer.wrap(extras);
		buf.putLong(delta);
		buf.putLong(initValue);
		buf.putInt(expiry);

		Request req = new Request(opcode, getBytes(key));
		req.setExtra(extras);
		return this.sendRequest(req);
	}

	// 4.6 quit
	public Response quit() throws IOException {
		Request req = new Request(Request.OP_QUIT);
		return this.sendRequest(req);
	}

	// 4.7 flush
	public Response flush(int delay) throws IOException {

		byte[] extras = intToBytes(delay, null, 0);
		Request req = new Request(Request.OP_FLUSH);
		req.setExtra(extras);
		return this.sendRequest(req);
	}

	// 4.8 noop
	public Response noop() throws IOException {
		Request req = new Request(Request.OP_NOOP);
		return this.sendRequest(req);
	}

	// 4.9 version
	public String version() throws IOException {
		Request req = new Request(Request.OP_VERSION);
		Response res = this.sendRequest(req);
		return res.getValueAsString();
	}

	// 4.10 append
	public Response append(String key, Object value, boolean quiet) throws IOException {
		byte opcode = quiet ? Request.OP_APPENDQ : Request.OP_APPEND;
		Request req = new Request(opcode, getBytes(key));
		req.setValue(getBytes(value));
		return this.sendRequest(req);
	}

	// 4.10 perpend
	public Response prepend(String key, Object value, boolean quiet) throws IOException {
		byte opcode = quiet ? Request.OP_PREPENDQ : Request.OP_PREPEND;
		Request req = new Request(opcode, getBytes(key));
		req.setValue(getBytes(value));
		return this.sendRequest(req);
	}

	// 4.11 stat
	public Response stat() throws IOException {
		Request req = new Request(Request.OP_STAT);
		return this.sendRequest(req);
	}

	// 4.11 stat
	public Response stat(String key) throws IOException {
		Request req = new Request(Request.OP_STAT, getBytes(key));
		return this.sendRequest(req);
	}

	protected Response addReplaceSet(byte opcode, String key, Object value, int extra, int expiry) throws IOException {
		Request req = new Request(opcode);
		req.setKey(key.getBytes(UTF8_CHARSET));
		req.setValue(getBytes(value));
		req.setExtra(intToBytes(new int[] { extra, expiry }));
		return this.sendRequest(req);
	}

}
