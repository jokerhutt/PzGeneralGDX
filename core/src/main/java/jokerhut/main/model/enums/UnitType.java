package jokerhut.main.model.enums;

public enum UnitType {

    INFANTRY("Infantry"),
    LIGHT_ARMOR("Light Armor"),
    HEAVY_ARMOR("Heavy Armor"),
    MEDIUM_ARMOR("Medium Armor"),
    MACHINE_GUN_INFANTRY("Machine Gun Infantry");

    private String displayName;

    UnitType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName; // optional, so printing the enum gives the nice name
    }

}
