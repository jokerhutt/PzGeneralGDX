package jokerhut.main.screen;

import java.util.List;

import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.enums.Faction;
import jokerhut.main.selection.CombatSystem;
import jokerhut.main.selection.MovementSystem;
import jokerhut.main.sound.SoundManager;

public class TurnManager {

    private final List<PlayerState> players;
    private int turnIndex = 0;
    private int turnNumber = 0;

    private CombatSystem combatSystemContext;
    private MovementSystem movementSystemContext;
    private SoundManager soundManager;

    public TurnManager(List<PlayerState> players, CombatSystem combatSystemContext,
            MovementSystem movementSystemContext, SoundManager soundManager) {

        this.combatSystemContext = combatSystemContext;
        this.movementSystemContext = movementSystemContext;
        this.soundManager = soundManager;

        this.players = players;
        if (getCurrentPlayer().getFaction() == Faction.BRITISH) {

            System.out.println("Current player is: British");
        } else {

            System.out.println("Current player is: German");
        }
    }

    public PlayerState getCurrentPlayer() {
        return players.get(turnIndex);
    }

    public void startTurn() {

        PlayerState playerState = getCurrentPlayer();
        for (AbstractUnit unit : playerState.getUnits()) {
            unit.setMovementPoints(unit.getStartingMovementPoints());
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

    public List<PlayerState> getPlayers() {
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
