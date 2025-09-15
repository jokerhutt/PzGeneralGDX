package jokerhut.main.screen;

import java.util.ArrayList;
import java.util.List;

import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.enums.Faction;

public class PlayerState {

    private final Faction faction;
    private int prestige;
    private final List<AbstractUnit> units;

    public PlayerState(Faction faction, int startingPrestige) {
        this.faction = faction;
        this.units = new ArrayList<>();
        this.prestige = startingPrestige;
    }

    public Faction getFaction() {
        return faction;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public List<AbstractUnit> getUnits() {
        return units;
    }

}
