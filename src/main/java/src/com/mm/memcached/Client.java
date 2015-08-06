package com.mm.memcached;

// NOT Thread Safe
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Client implements AutoCloseable {

	private String			host	= null;
	private int				port	= 0;
	private Socket			soc		= null;
	private InputStream		is		= null;
	private OutputStream	os		= null;

	public Client(String host, int port) throws Exception {
		this.host = host;
		this.port = port;
		connect();
	}

	void connect() throws IOException {
		soc = new Socket(host, port);
		is = soc.getInputStream();
		os = soc.getOutputStream();
	}

	public final Response sendRequest(Request req) throws IOException {

		String msg = req.validate();
		Response resp = null;
		if (msg != null) {
			resp = new Response();
			resp.setError(msg);
		} else {
			os.write(req.encode());
			os.flush();
			resp = readResponse();
		}
		return resp;
	}

	public boolean isConnected() {
		return soc == null ? false : soc.isConnected();
	}

	@Override
	public void close() throws Exception {
		close(is);
		close(os);
		close(soc);
	}

	void close(Closeable c) {
		try {
			if (c != null)
				c.close();
		} catch (Exception e) {
		}
	}

	private Response readResponse() throws IOException {
		Response resp = new Response();
		resp.decode(is);
		return resp;
	}

}
