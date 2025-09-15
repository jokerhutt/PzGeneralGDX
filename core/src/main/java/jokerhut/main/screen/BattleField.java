package jokerhut.main.screen;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.MainGame;
import jokerhut.main.DTs.Axial;
import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.entities.ArmoredUnit;
import jokerhut.main.entities.InfantryUnit;
import jokerhut.main.enums.Faction;

public class BattleField {

    private final HashMap<Axial, AbstractUnit> occupiedHexes = new HashMap<>();
    private final ArrayList<AbstractUnit> unitList = new ArrayList<>();

    public BattleField(MainGame gameContext) {

        addMockUnits();

    }

    public boolean moveUnit(AbstractUnit unit, Axial newPosition, Integer newMovePoints) {

        Axial oldPosition = unit.getPosition();

        if (occupiedHexes.containsKey(newPosition))
            return false;

        occupiedHexes.remove(oldPosition);
        unit.setPosition(newPosition);
        unit.setMovementPoints(newMovePoints);
        occupiedHexes.put(newPosition, unit);

        return true;

    }

    private void addMockUnits() {

        InfantryUnit unitOne = new InfantryUnit("15th Tommies", new Axial(20, 10),
                new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);
        InfantryUnit unitTwo = new InfantryUnit("3rd Highlanders", new Axial(22, 9),
                new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);

        ArmoredUnit armoredOne = new ArmoredUnit("Panzer Lehr", new Axial(18, 6),
                new TextureRegion(new Texture(Gdx.files.internal("units/panzerThree.png"))), Faction.GERMAN);

        spawn(unitOne, new Axial(20, 10));
        spawn(unitTwo, new Axial(22, 9));
        spawn(armoredOne, new Axial(18, 6));
    }

    private void spawn(AbstractUnit unit, Axial position) {

        if (occupiedHexes.containsKey(position))
            throw new IllegalStateException("TAKEN HEX");

        unit.setPosition(position);
        unitList.add(unit);
        occupiedHexes.put(position, unit);

    }

    private boolean isFreeHex(Axial axial) {
        if (occupiedHexes.containsKey(axial)) {
            return false;
        } else {
            return true;
        }
    }

    public AbstractUnit unitAt(Axial axial) {
        return occupiedHexes.get(axial);
    }

    public HashMap<Axial, AbstractUnit> getOccupiedHexes() {
        return occupiedHexes;
    }

    public ArrayList<AbstractUnit> getUnitList() {
        return unitList;
    }

}
