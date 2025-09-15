package jokerhut.main.selection;

import java.util.HashMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.ClickEvent;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.DTs.SelectionListener;
import jokerhut.main.enums.SelectionType;
import jokerhut.main.screen.BattleField;
import jokerhut.main.screen.TurnManager;
import jokerhut.main.service.MovementService;

public class SelectionState implements SelectionListener {

    private Selection current;
    private MovementOverlay movementOverlay;

    private HashMap<Axial, Hex> gameMapContext;
    private BattleField battleFieldContext;
    private TurnManager turnManagerContext;

    public SelectionState(HashMap<Axial, Hex> gameMapContext, BattleField battleFieldContext,
            TurnManager turnManagerContext) {
        this.gameMapContext = gameMapContext;
        this.battleFieldContext = battleFieldContext;
        this.turnManagerContext = turnManagerContext;
    }

    public void onSelect(ClickEvent clickEvent) {

        if (clickEvent == null) {
            this.movementOverlay = null;
            return;
        }

        if (clickEvent.selectionType() == SelectionType.SELECTION) {
            if (clickEvent.unit() != null
                    && clickEvent.unit().getFaction() == turnManagerContext.getCurrentPlayer().getFaction()) {
                this.movementOverlay = MovementService.compute(clickEvent.axial(),
                        clickEvent.unit().getMovementPoints(),
                        gameMapContext, battleFieldContext);
            } else if (clickEvent != null) {
                this.movementOverlay = null;
            }

            this.current = new Selection(clickEvent.axial(), clickEvent.hex(), clickEvent.unit());
        } else if (this.current != null && this.movementOverlay != null
                && clickEvent.selectionType() == SelectionType.MOUSEACTION) {
            Axial newIntendedPosition = clickEvent.axial();
            HashMap<Axial, Integer> reachableHexes = movementOverlay.reachableCosts();

            if (reachableHexes.containsKey(newIntendedPosition)) {
                Integer currentMovementPoints = current.unit().getMovementPoints();
                Integer costForMovement = reachableHexes.get(newIntendedPosition);
                if (costForMovement == null)
                    return;
                Integer newPoints = currentMovementPoints - costForMovement;

                if (newPoints >= 0) {
                    if (battleFieldContext.moveUnit(current.unit(), newIntendedPosition, newPoints)) {
                        this.current = new Selection(clickEvent.axial(), clickEvent.hex(), current.unit());
                        this.movementOverlay = MovementService.compute(current.unit().getPosition(),
                                current.unit().getMovementPoints(),
                                gameMapContext, battleFieldContext);
                    }

                }
            }

        }

    }

    public void clear() {
        current = null;
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

}
