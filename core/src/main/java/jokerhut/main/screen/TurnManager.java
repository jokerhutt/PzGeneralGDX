package jokerhut.main.screen;

import java.util.List;

import jokerhut.main.entities.AbstractUnit;

public class TurnManager {

    private final List<PlayerState> players;
    private int turnIndex = 0;
    private int turnNumber = 0;

    public TurnManager(List<PlayerState> players) {
        this.players = players;
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
        turnIndex = (turnIndex + 1) % players.size();
        if (turnIndex == 0) {
            turnNumber++;
        }
        startTurn();
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
