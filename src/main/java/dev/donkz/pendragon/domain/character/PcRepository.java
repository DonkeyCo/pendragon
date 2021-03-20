package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.Repository;

public interface PcRepository extends Repository<Pc> {
    Pc findByName(String name);
}
