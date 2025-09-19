package jokerhut.main.systems.selection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

import jokerhut.main.listeners.CombatListener;
import jokerhut.main.listeners.MovementListener;
import jokerhut.main.listeners.SelectionListener;
import jokerhut.main.listeners.TurnListener;
import jokerhut.main.model.combat.PendingAttack;
import jokerhut.main.model.enums.AttackResult;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.enums.SelectionType;
import jokerhut.main.model.geo.TerrainProfile;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.selection.ClickEvent;
import jokerhut.main.model.selection.Selection;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.selection.MovementOverlay;
import jokerhut.main.selection.SupplyRangeOverlay;
import jokerhut.main.systems.audio.SoundManager;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.systems.combat.CombatSystem;
import jokerhut.main.systems.effect.EffectSystem;
import jokerhut.main.systems.movement.MovementSystem;
import jokerhut.main.systems.overlay.MovementService;
import jokerhut.main.systems.turn.TurnManager;
import jokerhut.main.utils.HexUtils;

public class SelectionController implements SelectionListener, MovementListener, CombatListener, TurnListener {

	private Selection current;
	private MovementOverlay movementOverlay;
	private SupplyRangeOverlay supplyRangeOverlay;

	private HashMap<Axial, Hex> gameMapContext;
	private BattleField battleFieldContext;
	private TurnManager turnManagerContext;
	private EffectSystem effectSystem;
	private CombatSystem combatSystem;

	private MovementSystem movementSystem;

	private SoundManager soundManager;
	private ObjectMap<AbstractUnit, PendingAttack> pendingAttacks = new ObjectMap<>();

	public SelectionController(HashMap<Axial, Hex> gameMapContext, BattleField battleFieldContext,
			TurnManager turnManagerContext, SoundManager soundManager, MovementSystem movementSystem,
			EffectSystem effectSystem, CombatSystem combatSystem) {
		this.gameMapContext = gameMapContext;
		this.effectSystem = effectSystem;
		this.battleFieldContext = battleFieldContext;
		this.turnManagerContext = turnManagerContext;
		this.soundManager = soundManager;
		this.movementSystem = movementSystem;
		this.combatSystem = combatSystem;
		movementSystem.addListener(this);
		combatSystem.addListener(this);
	}

	public void onSelect(ClickEvent clickEvent) {

		if (clickEvent == null) {
			this.movementOverlay = null;
			this.supplyRangeOverlay = null;
			return;
		}

		if (clickEvent.selectionType() == SelectionType.SELECTION) {

			handleLeftClick(clickEvent);

			this.current = new Selection(clickEvent.axial(), clickEvent.hex(), clickEvent.unit());

			soundManager.leftClickSound.play();

		} else if (this.current != null && this.movementOverlay != null
				&& clickEvent.selectionType() == SelectionType.MOUSEACTION) {

			handleRightClick(clickEvent);

		}

	}

	private void handleLeftClick(ClickEvent clickEvent) {

		Faction playerFaction = turnManagerContext.getCurrentPlayer().getFaction();
		TerrainProfile clickedHexTerrain = gameMapContext.get(clickEvent.axial()).getTerrainProfile();

		if (clickEvent.unit() != null) {

			boolean isOwnUnit = turnManagerContext.getCurrentPlayer().getFaction() == clickEvent.unit().getFaction();

			if (isOwnUnit) {
				this.movementOverlay = MovementService.compute(clickEvent.axial(),
						clickEvent.unit().getMovementPoints(), clickEvent.unit().getFuelCount(),
						gameMapContext, battleFieldContext, playerFaction);
			} else if (clickEvent != null) {
				this.movementOverlay = null;
			}

		}

		if (clickedHexTerrain != null && clickedHexTerrain.isProvidesSupply()) {

			this.supplyRangeOverlay = clickedHexTerrain.getSupplyRangeOverlay();

		} else {
			this.supplyRangeOverlay = null;
		}

	}

	@Override
	public void onStartTurn() {

	}

	@Override
	public void onMotionFinished(AbstractUnit unit) {

		unit.setIdleFor(0);

		System.out.println("Points after attack are now: " + unit.getMovementPoints());
		if (this.current.unit() == unit) {
			this.current = new Selection(unit.getPosition(), gameMapContext.get(unit.getPosition()), unit);
			this.movementOverlay = MovementService.compute(current.unit().getPosition(),
					current.unit().getMovementPoints(), current.unit().getFuelCount(),
					gameMapContext, battleFieldContext, turnManagerContext.getCurrentPlayer().getFaction());

			TerrainProfile terrainProfile = gameMapContext.get(current.unit().getPosition()).getTerrainProfile();

			if (terrainProfile != null && terrainProfile.isProvidesSupply()) {
				this.supplyRangeOverlay = terrainProfile.getSupplyRangeOverlay();
			} else {
				this.supplyRangeOverlay = null;
			}
		}

		if (pendingAttacks.containsKey(unit)) {
			PendingAttack attackToPerform = pendingAttacks.get(unit);
			prepareAtttack(unit, attackToPerform);
		}

	}

	@Override
	public void onCombatFinished(AbstractUnit attacker) {
		PendingAttack attackToPerform = pendingAttacks.get(attacker);
		handleAttack(attacker, attackToPerform);
		pendingAttacks.remove(attacker);

	}

	private void prepareAtttack(AbstractUnit unit, PendingAttack attackToPerform) {
		AbstractUnit enemyUnit = battleFieldContext.unitAt(attackToPerform.newIntendedPosition());
		if (current.unit() != null) {
			effectSystem.spawnAnchoredTimed(effectSystem.getAnimationHandler().getInfantrySmgAnimation(),
					current.unit(), 2f, new Vector2(12f, 6f));
			effectSystem.spawnAnchoredTimed(effectSystem.getAnimationHandler().getInfantryBoltActionAnimation(),
					current.unit(), 2f, new Vector2(0f, -8f));
			effectSystem.spawnAnchoredTimed(effectSystem.getAnimationHandler().getInfantryBoltActionAnimationTwo(),
					current.unit(), 2f, new Vector2(-40f, 10f));
		}

		if (enemyUnit != null) {
			effectSystem.spawnAnchoredTimed(effectSystem.getAnimationHandler().getInfantrySmgAnimation(),
					enemyUnit, 2f, new Vector2(12f, 6f));
			effectSystem.spawnAnchoredTimed(effectSystem.getAnimationHandler().getInfantryBoltActionAnimation(),
					enemyUnit, 2f, new Vector2(0f, -8f));
			effectSystem.spawnAnchoredTimed(effectSystem.getAnimationHandler().getInfantryBoltActionAnimationTwo(),
					enemyUnit, 2f, new Vector2(-40f, 10f));
		}

		battleFieldContext.playUnitSounds(unit, attackToPerform.newIntendedPosition());

		System.out.println("STARTING ATTACK");
		combatSystem.startAttackTimer(unit);

	}

	private void handleAttack(AbstractUnit unit, PendingAttack attackToPerform) {

		System.out.println("ATTACK TIMER FINISHED");
		AttackResult result = battleFieldContext.attackUnit(unit, attackToPerform.newIntendedPosition(),
				attackToPerform.mpAfter(), attackToPerform.fuelAfter());

		System.out.println("ATTACK FINISHED");

		switch (result) {
			case FULLDEFEAT -> this.clear();
			case FULLVICTORY -> {
				this.current = new Selection(unit.getPosition(),
						gameMapContext.get(unit.getPosition()), current.unit());
				this.movementOverlay = MovementService.compute(current.unit().getPosition(),
						current.unit().getMovementPoints() + 1, current.unit().getFuelCount(),
						gameMapContext, battleFieldContext, turnManagerContext.getCurrentPlayer().getFaction());

				System.out
						.println("Constructing new path to occupy enemy hex, points are: " + unit.getMovementPoints());
				List<Axial> pathToNewPosition = reconstructPath(movementOverlay.parent(), movementOverlay.start(),
						attackToPerform.newIntendedPosition());

				movementSystem.move(current.unit(), pathToNewPosition);

			}
			default -> {
				if (this.current.unit() == unit) {
					this.movementOverlay = MovementService.compute(current.unit().getPosition(),
							current.unit().getMovementPoints(), current.unit().getFuelCount(), gameMapContext,
							battleFieldContext,
							turnManagerContext.getCurrentPlayer().getFaction());
				}
			}
		}
	}

	private void handleRightClick(ClickEvent clickEvent) {

		Axial newIntendedPosition = clickEvent.axial();
		Hex newIntendedHex = clickEvent.hex();
		HashMap<Axial, Integer> reachableHexes = movementOverlay.reachableCosts();
		HashMap<Axial, Integer> attackable = movementOverlay.attackableCosts();

		if (reachableHexes.containsKey(newIntendedPosition)) {
			moveToUnoccupiedHex(reachableHexes, newIntendedPosition);
		} else if (attackable.containsKey(newIntendedPosition)) {
			attackAndPotentiallyMove(attackable, newIntendedPosition, newIntendedHex);
		} else {
			soundManager.rightClickInvalidsound.play();
		}

	}

	private void attackAndPotentiallyMove(HashMap<Axial, Integer> attackable, Axial newIntendedPosition,
			Hex newIntendedHex) {
		Integer currentMovementPoints = current.unit().getMovementPoints();
		Integer costForMovementAndAttack = attackable.get(newIntendedPosition);

		Float currentFuelCount = current.unit().getFuelCount();
		Float fuelCostForMovement = current.unit().getFuelConsumption();
		Float newFuelCount = currentFuelCount - fuelCostForMovement;

		if (costForMovementAndAttack == null || newFuelCount == null) {
			return;
		}

		Integer newPointsAfterAttack = currentMovementPoints - costForMovementAndAttack;

		if (newPointsAfterAttack >= 0 && newFuelCount >= 0) {

			if (HexUtils.areNeighbors(current.unit().getPosition(), newIntendedPosition)
					&& !combatSystem.isUnitAttacking(current.unit())) {
				PendingAttack attackToPerform = new PendingAttack(newIntendedPosition, newPointsAfterAttack,
						newFuelCount);
				pendingAttacks.put(current.unit(), attackToPerform);
				prepareAtttack(current.unit(), attackToPerform);
				this.movementOverlay = null;
				this.supplyRangeOverlay = null;
			} else {

				Axial nearestEmptyHexToMoveTo = HexUtils.pickStaging(newIntendedPosition,
						movementOverlay.reachableCosts(),
						battleFieldContext);
				if (nearestEmptyHexToMoveTo == null)
					return;

				List<Axial> pathToNewPosition = reconstructPath(movementOverlay.parent(), movementOverlay.start(),
						nearestEmptyHexToMoveTo);

				if (!movementSystem.isUnitMoving(current.unit()) && !combatSystem.isUnitAttacking(current.unit())) {

					pendingAttacks.put(current.unit(),
							new PendingAttack(newIntendedPosition, newPointsAfterAttack, newFuelCount));

					soundManager.playMovement(current.unit().getUnitType());

					if (!pathToNewPosition.isEmpty()) {
						PendingAttack attackToPerform = new PendingAttack(newIntendedPosition, newPointsAfterAttack,
								newFuelCount);
						movementSystem.move(current.unit(), pathToNewPosition);
						this.movementOverlay = null;
						this.supplyRangeOverlay = null;
					}

				}
			}
			soundManager.rightClickSuccesSound.play();
		}
	}

	private void moveToUnoccupiedHex(HashMap<Axial, Integer> reachableHexes, Axial newIntendedPosition) {

		Integer currentMovementPoints = current.unit().getMovementPoints();
		Integer costForMovement = reachableHexes.get(newIntendedPosition);

		Float currentFuelCount = current.unit().getFuelCount();
		Float fuelCostForMovement = current.unit().getFuelConsumption();

		Float newFuelCount = currentFuelCount - fuelCostForMovement;

		if (costForMovement == null || newFuelCount == null) {
			return;
		}

		Integer newPoints = currentMovementPoints - costForMovement;

		if (!movementSystem.isUnitMoving(current.unit())) {
			List<Axial> pathToNewPosition = reconstructPath(movementOverlay.parent(), movementOverlay.start(),
					newIntendedPosition);

			soundManager.playMovement(current.unit().getUnitType());

			if (!pathToNewPosition.isEmpty()) {
				current.unit().setMovementPoints(newPoints);
				current.unit().setFuelCount(newFuelCount);
				movementSystem.move(current.unit(), pathToNewPosition);
				this.movementOverlay = null;
				this.supplyRangeOverlay = null;
			}

		}

	}

	private static List<Axial> reconstructPath(HashMap<Axial, Axial> parent, Axial start, Axial target) {
		if (start.equals(target))
			return java.util.Collections.emptyList();
		ArrayDeque<Axial> s = new ArrayDeque<>();
		Axial cur = target;
		while (true) {
			if (cur == null)
				return java.util.Collections.emptyList();
			if (cur.equals(start))
				break;
			s.push(cur);
			cur = parent.get(cur);
		}
		return new ArrayList<>(s);
	}

	public void clear() {
		current = null;
		movementOverlay = null;
	}

	public boolean has() {
		return current != null;
	}

	public Selection getCurrentSelection() {
		return current;
	}

	public boolean isSelected(Axial a) {
		return current != null && current.axial().equals(a);
	}

	public Selection getCurrent() {
		return current;
	}

	public void setCurrent(Selection current) {
		this.current = current;
	}

	public MovementOverlay getMovementOverlay() {
		return movementOverlay;
	}

	public void setMovementOverlay(MovementOverlay movementOverlay) {
		this.movementOverlay = movementOverlay;
	}

	public HashMap<Axial, Hex> getGameMapContext() {
		return gameMapContext;
	}

	public void setGameMapContext(HashMap<Axial, Hex> gameMapContext) {
		this.gameMapContext = gameMapContext;
	}

	public BattleField getBattleFieldContext() {
		return battleFieldContext;
	}

	public void setBattleFieldContext(BattleField battleFieldContext) {
		this.battleFieldContext = battleFieldContext;
	}

	public SupplyRangeOverlay getSupplyRangeOverlay() {
		return supplyRangeOverlay;
	}

	public void setSupplyRangeOverlay(SupplyRangeOverlay supplyRangeOverlay) {
		this.supplyRangeOverlay = supplyRangeOverlay;
	}

	public TurnManager getTurnManagerContext() {
		return turnManagerContext;
	}

	public void setTurnManagerContext(TurnManager turnManagerContext) {
		this.turnManagerContext = turnManagerContext;
	}

	public EffectSystem getEffectSystem() {
		return effectSystem;
	}

	public void setEffectSystem(EffectSystem effectSystem) {
		this.effectSystem = effectSystem;
	}

	public CombatSystem getCombatSystem() {
		return combatSystem;
	}

	public void setCombatSystem(CombatSystem combatSystem) {
		this.combatSystem = combatSystem;
	}

	public MovementSystem getMovementSystem() {
		return movementSystem;
	}

	public void setMovementSystem(MovementSystem movementSystem) {
		this.movementSystem = movementSystem;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	public ObjectMap<AbstractUnit, PendingAttack> getPendingAttacks() {
		return pendingAttacks;
	}

	public void setPendingAttacks(ObjectMap<AbstractUnit, PendingAttack> pendingAttacks) {
		this.pendingAttacks = pendingAttacks;
	}

}
