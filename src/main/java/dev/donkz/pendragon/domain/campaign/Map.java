package dev.donkz.pendragon.domain.campaign;

import lombok.Data;

@Data
public class Map {
    private String id;

    private String name;
    private String description;
    private String tilemapPath;
    private Size size;
}
