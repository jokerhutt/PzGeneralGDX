package jokerhut.main.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.DTs.Axial;
import jokerhut.main.enums.Faction;
import jokerhut.main.enums.UnitType;

public class InfantryUnit extends AbstractUnit {
    public InfantryUnit(String name, Axial pos, TextureRegion sprite, Faction faction) {
        setName(name);
        setFaction(faction);
        setUnitType(UnitType.INFANTRY);
        setPosition(pos);
        setSprite(sprite);
        setStartingMovementPoints(2);
        setMovementPoints(this.getStartingMovementPoints());
        setMaxHealth(10f);
        setHealth(10f);
        setDefense(2f);
        setSoftAttack(3f);
        setHardAttack(1f);
    }
}
