package jokerhut.main.selection;

import java.util.HashMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.DTs.SelectionListener;
import jokerhut.main.screen.BattleField;
import jokerhut.main.service.MovementService;

public class SelectionState implements SelectionListener {

    private Selection current;
    private MovementOverlay movementOverlay;

    private HashMap<Axial, Hex> gameMapContext;
    private BattleField battleFieldContext;

    public SelectionState(HashMap<Axial, Hex> gameMapContext, BattleField battleFieldContext) {
        this.gameMapContext = gameMapContext;
        this.battleFieldContext = battleFieldContext;
    }

    public void onSelect(Selection selection) {
        this.current = selection;

        if (selection == null || selection.unit() == null) {
            this.movementOverlay = null;
        } else {
            this.movementOverlay = MovementService.compute(current.axial(), current.unit().getMovementPoints(),
                    gameMapContext, battleFieldContext);
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
