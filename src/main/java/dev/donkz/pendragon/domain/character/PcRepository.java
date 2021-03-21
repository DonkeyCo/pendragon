package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.Repository;

import java.util.List;

public interface PcRepository extends Repository<Pc> {
    List<Pc> findByName(String name);
}
