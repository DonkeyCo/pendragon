package dev.donkz.pendragon.domain.common;

public enum PriceUnit {
    CP("Copper", 1),
    SP("Silver", 10),
    EP("Electrum", 50),
    GP("Gold", 100),
    PP("Platinum", 1000);

    private final String name;
    private final int conversion;
    PriceUnit(String name, int conversion) {
        this.name = name;
        this.conversion = conversion;
    }

    public String getName() {
        return name;
    }

    public int getConversion() {
        return conversion;
    }
}
