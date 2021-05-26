package dev.donkz.pendragon.infrastructure.network;

import dev.donkz.pendragon.exception.infrastructure.ConnectionException;
import io.socket.client.Socket;


public interface Communicator {
    boolean connect() throws ConnectionException;

    void disconnect();

    void send(String event, Object... objects);

    Socket getSocket();
}
