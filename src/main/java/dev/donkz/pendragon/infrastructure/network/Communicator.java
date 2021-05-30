package dev.donkz.pendragon.infrastructure.network;

import dev.donkz.pendragon.exception.infrastructure.ConnectionException;
import io.socket.client.Socket;

/**
 * Communicator Interface
 */
public interface Communicator {
    /**
     * Connect to server
     * @return isConnected
     * @throws ConnectionException
     */
    boolean connect() throws ConnectionException;

    /**
     * Disconnect from server
     */
    void disconnect();

    /**
     * Send an event with given objects
     * @param event event name
     * @param objects list of objects
     */
    void send(String event, Object... objects);

    /**
     * Get the created socket
     * @return socket
     */
    Socket getSocket();
}
