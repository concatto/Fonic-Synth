package br.concatto.fonicsynth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class ConnectionManager {
	private boolean connected = false;
	private OutputStream out;
	private InputStream in;
	private Consumer<KeyboardEvent> receiveAction;

	public ConnectionManager() {
	}
	
	public void connect(String target) {
		String[] parts = target.split(":");
		try {
			Socket socket = new Socket(parts[0], Integer.parseInt(parts[1]));
			listen(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen(Socket socket) throws IOException {
		out = socket.getOutputStream();
		in = socket.getInputStream();
		connected = true;
		
		new Thread(() -> {
			while (true) {
				if (receiveAction != null) {
					try {
						int type = in.read();
						int data = in.read();
						receiveAction.accept(new KeyboardEvent((byte) type, (byte) data));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void host(String port) {
		try {
			ServerSocket server = new ServerSocket(Integer.parseInt(port));
			listen(server.accept());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setOnReceive(Consumer<KeyboardEvent> receiveAction) {
		this.receiveAction = receiveAction;
	}
	
	public void writeEvent(KeyboardEvent event) {
		try {
			out.write(event.getEventType());
			out.write(event.getEventData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
}