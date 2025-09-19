package jokerhut.main.model.unit;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.enums.UnitType;
import jokerhut.main.utils.HexUtils;

public class ArmoredUnit extends AbstractUnit {

    public ArmoredUnit(String name, Axial pos, TextureRegion sprite, Faction faction) {
        setName(name);
        setFaction(faction);
        setUnitType(UnitType.LIGHT_ARMOR);
        setPosition(pos);
        setSprite(sprite);
        setStartingMovementPoints(4);
        setMovementPoints(this.getStartingMovementPoints());
        setMaxHealth(20f);
        setHealth(10f);
        setDefense(3f);
        setSoftAttack(4f);
        setHardAttack(4f);
        setFuelCount(20f);
        setMaxFuelCount(20f);
        setOrganization(15);
        setMaxOrganization(15);
        Vector2 p = HexUtils.axialToPixelCenter(pos);
        setRenderPosPx(p);
    }

}
