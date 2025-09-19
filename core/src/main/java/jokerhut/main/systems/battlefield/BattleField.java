package jokerhut.main.systems.battlefield;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.app.MainGame;
import jokerhut.main.model.enums.AttackResult;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.enums.UnitType;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.player.Player;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.unit.ArmoredUnit;
import jokerhut.main.model.unit.InfantryUnit;
import jokerhut.main.systems.audio.SoundManager;
import jokerhut.main.utils.CombatUtils;

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

	private void performCombat(AbstractUnit attackerUnit, AbstractUnit defendingUnit) {

		float attackerHealth = attackerUnit.getHealth();
		float defenderHealth = defendingUnit.getHealth();

		float attackerDefence = attackerUnit.getDefense();
		float defenderDefence = defendingUnit.getDefense();

		float attackerAttack = attackerUnit.getSoftAttack();
		float defenderAttack = defendingUnit.getSoftAttack();

		float attackerOrganization = attackerUnit.getOrganization();
		float defenderOrganization = defendingUnit.getOrganization();

		float attackerMaxOrganization = attackerUnit.getMaxOrganization();
		float defenderMaxOrganization = defendingUnit.getMaxOrganization();

		float attackerEffectiveDamage = CombatUtils.effAtk(attackerAttack, attackerOrganization,
				attackerMaxOrganization);
		float defenderEffectiveDamage = CombatUtils.effAtk(defenderAttack, defenderOrganization,
				defenderMaxOrganization);

		float dmgToDefender = CombatUtils.dmg(attackerEffectiveDamage, defenderDefence, defenderOrganization,
				defenderMaxOrganization);
		float dmgToAttacker = CombatUtils.dmg(defenderEffectiveDamage, attackerDefence, attackerOrganization,
				attackerMaxOrganization);

		float DEF_BONUS = 1.2f;

		dmgToDefender = dmgToDefender / DEF_BONUS;

		dmgToDefender = Math.min(dmgToDefender, defenderHealth);
		dmgToAttacker = Math.min(dmgToAttacker, attackerHealth);

		attackerUnit.setHealth(attackerHealth - dmgToAttacker);
		defendingUnit.setHealth(defenderHealth - dmgToDefender);

		defendingUnit
				.setOrganization(defenderOrganization - (CombatUtils.ORG_DMG * dmgToDefender) - CombatUtils.ORG_FIRE);
		attackerUnit
				.setOrganization(attackerOrganization - (CombatUtils.ORG_DMG * dmgToAttacker) - CombatUtils.ORG_FIRE);

	}

	public AttackResult attackUnit(AbstractUnit attackerUnit, Axial targetPosition, Integer newMovePoints,
			Float newFuelPoints, Float newOrganizationPoints) {
		attackerUnit.setOrganization(newOrganizationPoints);
		AbstractUnit defendingUnit = unitAt(targetPosition);

		float attackerHealth = attackerUnit.getHealth();
		float defenderHealth = defendingUnit.getHealth();

		performCombat(attackerUnit, defendingUnit);

		float newAttackerHealth = attackerUnit.getHealth();
		float newDefenderHealth = defendingUnit.getHealth();

		float attackerHealthDifference = attackerHealth - newAttackerHealth;
		float defenderHealthDifference = defenderHealth - newDefenderHealth;

		if (defendingUnit.getHealth() <= 0) {
			if (defendingUnit.getFaction() == Faction.GERMAN) {
				axisPlayer.getUnits().remove(defendingUnit);
			} else {
				alliedPlayer.getUnits().remove(defendingUnit);
			}
			occupiedHexes.remove(targetPosition);
			attackerUnit.setMovementPoints(newMovePoints);
			attackerUnit.setFuelCount(newFuelPoints);
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

			attackerUnit.setMovementPoints(newMovePoints);
			attackerUnit.setFuelCount(newFuelPoints);

			System.out.println("NewPoints: " + newMovePoints);

			if (attackerHealthDifference >= defenderHealthDifference) {
				return AttackResult.VICTORY;
			} else {
				return AttackResult.DEFEAT;
			}
		}

	}

	private void addMockUnits() {

		// Allies (UK)
		InfantryUnit uk18thBrigade = new InfantryUnit("18th Brigade", new Axial(20, 10),
				new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);
		InfantryUnit uk25thBrigade = new InfantryUnit("25th Brigade", new Axial(22, 9),
				new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);
		InfantryUnit uk24thBrigade = new InfantryUnit("24th Brigade", new Axial(17, 13),
				new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);
		InfantryUnit uk20thBrigade = new InfantryUnit("20th Brigade", new Axial(18, 12),
				new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))), Faction.BRITISH);
		ArmoredUnit uk4thHussars = new ArmoredUnit("4th Queen's Own Hussars", new Axial(16, 13),
				new TextureRegion(new Texture(Gdx.files.internal("units/matilda.png"))), Faction.BRITISH);

		// Axis (Germany)
		ArmoredUnit dePzAbt190 = new ArmoredUnit("Panzer-Abteilung 190", new Axial(18, 6),
				new TextureRegion(new Texture(Gdx.files.internal("units/panzerThree.png"))), Faction.GERMAN);
		ArmoredUnit dePzRgt8 = new ArmoredUnit("Panzer-Regiment 8", new Axial(19, 7),
				new TextureRegion(new Texture(Gdx.files.internal("units/panzerThree.png"))), Faction.GERMAN);
		InfantryUnit dePzGren155 = new InfantryUnit("Panzergrenadier-Regiment 155", new Axial(18, 8),
				new TextureRegion(new Texture(Gdx.files.internal("newAssets/bigunits/Wehrmacht_Inf.png"))),
				Faction.GERMAN);
		InfantryUnit dePzGren361A = new InfantryUnit("Panzergrenadier-Regiment 361", new Axial(18, 4),
				new TextureRegion(new Texture(Gdx.files.internal("newAssets/bigunits/Wehrmacht_Inf.png"))),
				Faction.GERMAN);
		InfantryUnit dePzGren361B = new InfantryUnit("Panzergrenadier-Regiment 361", new Axial(17, 9),
				new TextureRegion(new Texture(Gdx.files.internal("newAssets/bigunits/Wehrmacht_Inf.png"))),
				Faction.GERMAN);

		spawn(uk18thBrigade, uk18thBrigade.getPosition(), alliedPlayer);
		spawn(uk25thBrigade, uk25thBrigade.getPosition(), alliedPlayer);
		spawn(uk24thBrigade, uk24thBrigade.getPosition(), alliedPlayer);
		spawn(uk20thBrigade, uk20thBrigade.getPosition(), alliedPlayer);
		spawn(uk4thHussars, uk4thHussars.getPosition(), alliedPlayer);

		spawn(dePzAbt190, dePzAbt190.getPosition(), axisPlayer);
		spawn(dePzRgt8, dePzRgt8.getPosition(), axisPlayer);
		spawn(dePzGren155, dePzGren155.getPosition(), axisPlayer);
		spawn(dePzGren361A, dePzGren361A.getPosition(), axisPlayer);
		spawn(dePzGren361B, dePzGren361B.getPosition(), axisPlayer);
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
