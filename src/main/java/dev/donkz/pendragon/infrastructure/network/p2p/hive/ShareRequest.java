package dev.donkz.pendragon.infrastructure.network.p2p.hive;

import lombok.Getter;

import java.util.UUID;

/**
 * A request object for data sharing
 */
@Getter
public class ShareRequest {
    private final String id;
    private final String requesterId;

    public ShareRequest(String requesterId) {
        this.id = UUID.randomUUID().toString();
        this.requesterId = requesterId;
    }
}
