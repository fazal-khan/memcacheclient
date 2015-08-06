package com.mm.memcached;

import java.io.IOException;

public class MemcacheClient implements AutoCloseable {

	public final static int			MEM_PORT	= 11211;
	private final AbstractClient	client;

	public MemcacheClient() throws Exception {
		this("127.0.0.1", MEM_PORT);
	}

	public MemcacheClient(String host, int port) throws Exception {
		client = new AbstractClient(host, port) {
		};
	}

	// 4.2 helpers
	public String getString(String key) throws IOException {
		Response res = client.get(key, false, false);
		checkError(res);
		return res.getValueAsString();
	}

	public Long getLong(String key) throws IOException {
		// String val = getString(key);
		// return Long.parseLong(val.trim());
		return client.incr(key, 0, 0, 0).getValueAsLong();
	}

	// 4.3 helpers for string & long
	public void add(String key, String value) throws IOException {
		Response res = client.addReplaceSet(Request.OP_ADD, key, value, 0, 0);
		checkError(res);
	}

	public void add(String key, long value) throws IOException {
		// Response res = addReplaceSet(Request.OP_ADD, key, new
		// Long(value).toString(), 0, 0);
		Response res = client.incr(key, 0, value, 0);
		checkError(res);
	}

	public void set(String key, String value) throws IOException {
		Response res = client.addReplaceSet(Request.OP_SET, key, value, 0, 0);
		checkError(res);
	}

	public void replace(String key, String value) throws IOException {
		Response res = client.addReplaceSet(Request.OP_SET, key, value, 0, 0);
		checkError(res);
	}

	public void append(String key, String value) throws IOException {
		Response res = client.append(key, value, false);
		checkError(res);
	}

	public void prepend(String key, String value) throws IOException {
		Response res = client.prepend(key, value, false);
		checkError(res);
	}

	// 4.5 helpers
	public Long increment(String key, long delta) throws IOException {
		Response res = client.incr(key, delta, 0, 0);
		checkError(res);
		return res.getValueAsLong();
	}

	// 4.5 helpers
	public Long increment(String key, long delta, long initValue) throws IOException {
		Response res = client.incr(key, delta, initValue, 0);
		checkError(res);
		return res.getValueAsLong();
	}

	public Long decrement(String key, long delta) throws IOException {
		Response res = client.decr(key, delta, 0, 0);
		checkError(res);
		return res.getValueAsLong();
	}

	public String version() throws IOException {
		return this.client.version();
	}

	public boolean delete(String key) throws IOException {
		return this.client.delete(key);
	}

	// helpers
	public boolean delete(String... keys) throws IOException {
		boolean success = true;
		for (String key : keys) {
			success = success && this.client.delete(key);
		}
		return success;
	}

	// helpers
	public String[] mulitget(String... keys) throws IOException {
		String[] response = new String[keys.length];
		for (int i = 0; i < keys.length - 1; i++) {
			Response res = client.get(keys[i], false, true);
			response[i] = res.hasError() ? null : res.getValueAsString();
		}
		Response res = client.get(keys[keys.length - 1]);
		response[keys.length - 1] = res.hasError() ? null : res.getValueAsString();
		return response;
	}

	public boolean quit() {
		try {
			this.client.quit();
			return true;
		} catch (Exception e) {
			// ignored
		}
		return false;
	}

	public void noop() throws IOException {
		this.client.noop();
	}

	// raw req and resp
	public Response send(Request r) throws IOException {
		return this.client.sendRequest(r);
	}

	@Override
	public void close() throws Exception {
		if (client != null)
			client.close();
	}

	protected Client getClient() {
		return client;
	}

	private static void checkError(Response res) {
		if (res.hasError()) {
			throw new RuntimeException("Memcache Response Error : " + res.getError());
		}
	}

}
