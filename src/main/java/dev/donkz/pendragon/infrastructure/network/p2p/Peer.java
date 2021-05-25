package dev.donkz.pendragon.infrastructure.network.p2p;

import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;

import java.net.UnknownHostException;

public interface Peer {
    void start(String nodeId);
    void connect(String host, String nodeId) throws UnknownHostException;
    void register(String playerId) throws NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException, NoSessionException;
    void disconnect();
    void exchange(String content, String repository, String id) throws NoSessionException, NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException;
    void remove(String repository, String filename) throws NoSessionException, NoPeerConnectionException, InvalidProcessStateException, ProcessExecutionException;
}
