package jokerhut.main.model.player;

import java.util.ArrayList;
import java.util.List;

import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.enums.Faction;

public class Player {

    private final Faction faction;
    private int prestige;
    private final List<AbstractUnit> units;

    public Player(Faction faction, int startingPrestige) {
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
