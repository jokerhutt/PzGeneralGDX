package jokerhut.main.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.DTs.Axial;
import jokerhut.main.enums.UnitType;

public class InfantryUnit extends AbstractUnit {
    public InfantryUnit(String name, Axial pos, TextureRegion sprite) {
        setName(name);
        setUnitType(UnitType.INFANTRY);
        setPosition(pos);
        setSprite(sprite);
        setHealth(10f);
        setDefense(2f);
        setSoftAttack(3f);
        setHardAttack(1f);
    }
}
