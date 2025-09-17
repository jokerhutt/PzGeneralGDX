package jokerhut.main.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.enums.Faction;
import jokerhut.main.enums.UnitType;
import jokerhut.main.utils.HexUtils;

public class InfantryUnit extends AbstractUnit {
    public InfantryUnit(String name, Axial pos, TextureRegion sprite, Faction faction) {
        setName(name);
        setFaction(faction);
        setUnitType(UnitType.INFANTRY);
        setPosition(pos);
        setSprite(sprite);
        setStartingMovementPoints(3);
        setMovementPoints(this.getStartingMovementPoints());
        setMaxHealth(10f);
        setHealth(10f);
        setDefense(2f);
        setSoftAttack(3f);
        setHardAttack(1f);
        Vector2 p = HexUtils.axialToPixelCenter(pos);
        setRenderPosPx(p);
    }
}
