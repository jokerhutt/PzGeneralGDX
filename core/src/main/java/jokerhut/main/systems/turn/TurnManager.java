package jokerhut.main.systems.turn;

import java.util.List;

import jokerhut.main.model.player.Player;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.systems.combat.CombatSystem;
import jokerhut.main.systems.movement.MovementSystem;
import jokerhut.main.systems.audio.SoundManager;

public class TurnManager {

    private final List<Player> players;
    private int turnIndex = 0;
    private int turnNumber = 0;

    private CombatSystem combatSystemContext;
    private MovementSystem movementSystemContext;
    private SoundManager soundManager;

    public TurnManager(List<Player> players, CombatSystem combatSystemContext,
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

    public Player getCurrentPlayer() {
        return players.get(turnIndex);
    }

    public void startTurn() {

        Player player = getCurrentPlayer();
        for (AbstractUnit unit : player.getUnits()) {
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
