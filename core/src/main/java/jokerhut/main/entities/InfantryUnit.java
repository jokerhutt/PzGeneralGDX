package jokerhut.main.entities;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.DTs.Axial;

public class InfantryUnit extends AbstractUnit {
    public InfantryUnit(Axial pos, TextureRegion sprite) {
        setName("Infantry");
        setPosition(pos);
        setSprite(sprite);
        setHealth(10f);
        setDefense(2f);
        setSoftAttack(3f);
        setHardAttack(1f);
    }
}
