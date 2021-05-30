package dev.donkz.pendragon.infrastructure.network.socket;

import dev.donkz.pendragon.exception.infrastructure.ConnectionException;
import dev.donkz.pendragon.infrastructure.network.Communicator;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

/**
 * Communicator which uses WebSocket as means of communication
 */
public class WebSocketCommunicator implements Communicator {

    private Socket socket;

    public WebSocketCommunicator() {
    }

    @Override
    public boolean connect() throws ConnectionException {
        try {
            this.socket = IO.socket(System.getenv("websocket"));
            this.socket.connect();
        } catch (URISyntaxException e) {
            throw new ConnectionException();
        }
        return this.socket.connected();
    }

    @Override
    public void disconnect() {
        this.socket.disconnect();
    }

    @Override
    public void send(String event, Object... objects) {
        this.socket.emit(event, objects);
    }

    public Socket getSocket() {
        return this.socket;
    }
}
