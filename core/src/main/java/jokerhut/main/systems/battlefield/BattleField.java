package jokerhut.main.systems.battlefield;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.app.MainGame;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.player.Player;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.unit.ArmoredUnit;
import jokerhut.main.model.unit.InfantryUnit;
import jokerhut.main.model.enums.AttackResult;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.enums.UnitType;
import jokerhut.main.systems.audio.SoundManager;

public class BattleField {

    private final HashMap<Axial, AbstractUnit> occupiedHexes = new HashMap<>();
    private final Player axisPlayer;
    private final Player alliedPlayer;
    private SoundManager soundManager;

    public BattleField(MainGame gameContext, Player axisPlayer, Player alliedPlayer,
                       SoundManager soundManager) {

        this.axisPlayer = axisPlayer;
        this.alliedPlayer = alliedPlayer;
        this.soundManager = soundManager;
        addMockUnits();

    }

    public void commitMove(AbstractUnit unit, Axial to) {
        Axial from = unit.getPosition();
        if (from.equals(to))
            return;

        AbstractUnit other = occupiedHexes.get(to);
        if (other != null && other != unit) {
            throw new IllegalStateException("step into occupied: " + to);
        }

        occupiedHexes.remove(from);
        unit.setPosition(to);
        occupiedHexes.put(to, unit);
    }

    public void playUnitSounds(AbstractUnit attackerUnit, Axial targetPosition) {
        AbstractUnit defendingUnit = unitAt(targetPosition);
        if (attackerUnit.getUnitType() == UnitType.INFANTRY && defendingUnit.getUnitType() == UnitType.INFANTRY) {
            soundManager.infantryToInfantryCombat.play();
        } else if (attackerUnit.getUnitType() == UnitType.LIGHT_ARMOR
                || defendingUnit.getUnitType() == UnitType.LIGHT_ARMOR) {
            soundManager.infantryToInfantryCombat.play();
            soundManager.infantryToTankCombat.play();
        }
    }

    public AttackResult attackUnit(AbstractUnit attackerUnit, Axial targetPosition, Integer newMovePoints) {
        AbstractUnit defendingUnit = unitAt(targetPosition);

        float attackerHealth = attackerUnit.getHealth();
        float defenderHealth = defendingUnit.getHealth();

        float attackerDefence = attackerUnit.getDefense();
        float defenderDefence = defendingUnit.getDefense();

        float attackerAttack = attackerUnit.getSoftAttack();
        float defenderAttack = defendingUnit.getSoftAttack();

        float newAttackerHealth = attackerHealth - (defenderAttack - attackerDefence / 2f);
        float newDefenderHealth = defenderHealth - (attackerAttack - defenderDefence);

        System.out.println("newAttackerHealth: " + attackerHealth + " - " + "(" + defenderAttack + " + "
                + attackerDefence + " / " + "2" + ") = " + newAttackerHealth);

        System.out.println("newDefenderHealth: " + defenderHealth + " - " + "(" + attackerAttack + " + "
                + defenderDefence + ") = " + newDefenderHealth);
        System.out.println("New Defender Health: " + newDefenderHealth + "New Attacker Health: " + newAttackerHealth);

        attackerUnit.setHealth(newAttackerHealth);
        defendingUnit.setHealth(newDefenderHealth);

        if (defendingUnit.getHealth() <= 0) {
            if (defendingUnit.getFaction() == Faction.GERMAN) {
                axisPlayer.getUnits().remove(defendingUnit);
            } else {
                alliedPlayer.getUnits().remove(defendingUnit);
            }
            occupiedHexes.remove(targetPosition);
            attackerUnit.setMovementPoints(newMovePoints);
            System.out.println("Unit points are now: " + attackerUnit.getMovementPoints());
            return AttackResult.FULLVICTORY;
        } else if (attackerUnit.getHealth() <= 0) {
            if (attackerUnit.getFaction() == Faction.GERMAN) {
                axisPlayer.getUnits().remove(attackerUnit);
            } else {
                alliedPlayer.getUnits().remove(attackerUnit);
                occupiedHexes.remove(attackerUnit.getPosition());
            }
            return AttackResult.FULLDEFEAT;
        } else {

            System.out.println("Current points: " + attackerUnit.getMovementPoints());
            float attackerHealthDifference = attackerHealth - newAttackerHealth;
            float defenderHealthDifference = defenderHealth - newDefenderHealth;
            attackerUnit.setMovementPoints(newMovePoints);

            System.out.println("NewPoints: " + newMovePoints);

            if (attackerHealthDifference >= defenderHealthDifference) {
                return AttackResult.VICTORY;
            } else {
                return AttackResult.DEFEAT;
            }
        }

    }

    private void addMockUnits() {

        InfantryUnit unitOne = new InfantryUnit("15th Tommies", new Axial(20, 10),
                new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);
        InfantryUnit unitTwo = new InfantryUnit("3rd Highlanders", new Axial(22, 9),
                new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);

        ArmoredUnit armoredOne = new ArmoredUnit("Panzer Lehr", new Axial(18, 6),
                new TextureRegion(new Texture(Gdx.files.internal("units/panzerThree.png"))), Faction.GERMAN);

        InfantryUnit infantryUnit = new InfantryUnit("GebirgsJager", new Axial(18, 8),
                new TextureRegion(new Texture(Gdx.files.internal("newAssets/bigunits/Wehrmacht_Inf.png"))),
                Faction.GERMAN);

        spawn(unitOne, new Axial(20, 10), alliedPlayer);
        spawn(unitTwo, new Axial(22, 9), alliedPlayer);
        spawn(armoredOne, new Axial(18, 6), axisPlayer);
        spawn(infantryUnit, new Axial(18, 8), axisPlayer);
    }

    private void spawn(AbstractUnit unit, Axial position, Player player) {

        if (occupiedHexes.containsKey(position))
            throw new IllegalStateException("TAKEN HEX");

        player.getUnits().add(unit);
        unit.setPosition(position);
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

}
