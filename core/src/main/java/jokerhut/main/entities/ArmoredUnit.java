package jokerhut.main.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.DTs.Axial;
import jokerhut.main.enums.Faction;
import jokerhut.main.enums.UnitType;

public class ArmoredUnit extends AbstractUnit {

    public ArmoredUnit(String name, Axial pos, TextureRegion sprite, Faction faction) {
        setName(name);
        setFaction(faction);
        setUnitType(UnitType.LIGHT_ARMOR);
        setPosition(pos);
        setSprite(sprite);
        setStartingMovementPoints(4);
        setMovementPoints(this.getStartingMovementPoints());
        setHealth(20f);
        setDefense(3f);
        setSoftAttack(4f);
        setHardAttack(4f);
    }

}
