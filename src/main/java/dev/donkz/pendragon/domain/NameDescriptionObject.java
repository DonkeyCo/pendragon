package dev.donkz.pendragon.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class NameDescriptionObject {
    private final String name;
    private final String description;
}
