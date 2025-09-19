package jokerhut.main.systems.turn;

import java.util.HashMap;
import java.util.List;

import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.geo.TerrainProfile;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.player.Player;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.systems.audio.SoundManager;
import jokerhut.main.systems.combat.CombatSystem;
import jokerhut.main.systems.movement.MovementSystem;

public class TurnManager {

	private final List<Player> players;
	private int turnIndex = 0;
	private int turnNumber = 0;

	private CombatSystem combatSystemContext;
	private MovementSystem movementSystemContext;
	private SoundManager soundManager;
	private HashMap<Axial, Hex> gameMapContext;
	private HashMap<Axial, Integer> supplyFieldContext;

	public TurnManager(List<Player> players, CombatSystem combatSystemContext,
			MovementSystem movementSystemContext, SoundManager soundManager, HashMap<Axial, Hex> gameMapContext,
			HashMap<Axial, Integer> supplyFieldContext) {

		this.combatSystemContext = combatSystemContext;
		this.movementSystemContext = movementSystemContext;
		this.soundManager = soundManager;
		this.gameMapContext = gameMapContext;
		this.supplyFieldContext = supplyFieldContext;

		this.players = players;
		if (getCurrentPlayer().getFaction() == Faction.BRITISH) {

			System.out.println("Current player is: British");
		} else {

			System.out.println("Current player is: German");
		}
	}

	public Player getCurrentPlayer() {
		return players.get(turnIndex);
	}

	public void startTurn() {

		Player player = getCurrentPlayer();
		for (AbstractUnit unit : player.getUnits()) {
			unit.setMovementPoints(unit.getStartingMovementPoints());
			unit.setIdleFor(unit.getIdleFor() + 1);

			if (unit.getIdleFor() > 1) {

				Hex hexOfUnit = gameMapContext.get(unit.getPosition());

				if (hexOfUnit != null) {

					TerrainProfile terrainProfile = hexOfUnit.getTerrainProfile();

					if (supplyFieldContext.containsKey(unit.getPosition())) {
						System.out.println("In supply");

						float fuelAdded = supplyFieldContext.get(unit.getPosition());
						float newIntendedFuelCount = unit.getFuelCount() + fuelAdded;

						if (newIntendedFuelCount <= unit.getMaxFuelCount()) {
							unit.setFuelCount(newIntendedFuelCount);
						} else {
							unit.setFuelCount(unit.getMaxFuelCount());
						}

					} else {
						System.out.println("not in supply");
					}

					if (terrainProfile != null && terrainProfile.getEntrenchCap() > unit.getEntrenchment()) {
						unit.setEntrenchment(unit.getEntrenchment() + 1);
					}

				}

			} else {
				System.out.println("Not idle");
			}

		}

	}

	public void endTurn() {

		if (combatSystemContext.isEmpty() && movementSystemContext.isEmpty()) {

			turnIndex = (turnIndex + 1) % players.size();
			if (turnIndex == 0) {
				turnNumber++;
			}
			startTurn();

		} else {
			soundManager.rightClickInvalidsound.play();
		}

	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getTurnIndex() {
		return turnIndex;
	}

	public void setTurnIndex(int turnIndex) {
		this.turnIndex = turnIndex;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

}
