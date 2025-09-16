package jokerhut.main.selection;

import java.util.HashMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.ClickEvent;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.DTs.SelectionListener;
import jokerhut.main.enums.AttackResult;
import jokerhut.main.enums.Faction;
import jokerhut.main.enums.SelectionType;
import jokerhut.main.screen.BattleField;
import jokerhut.main.screen.TurnManager;
import jokerhut.main.service.MovementService;
import jokerhut.main.sound.SoundManager;

public class SelectionState implements SelectionListener {

    private Selection current;
    private MovementOverlay movementOverlay;

    private HashMap<Axial, Hex> gameMapContext;
    private BattleField battleFieldContext;
    private TurnManager turnManagerContext;

    private SoundManager soundManager;

    public SelectionState(HashMap<Axial, Hex> gameMapContext, BattleField battleFieldContext,
            TurnManager turnManagerContext, SoundManager soundManager) {
        this.gameMapContext = gameMapContext;
        this.battleFieldContext = battleFieldContext;
        this.turnManagerContext = turnManagerContext;
        this.soundManager = soundManager;
    }

    public void onSelect(ClickEvent clickEvent) {

        Faction playerFaction = turnManagerContext.getCurrentPlayer().getFaction();

        if (clickEvent == null) {
            this.movementOverlay = null;
            return;
        }

        if (clickEvent.selectionType() == SelectionType.SELECTION) {
            if (clickEvent.unit() != null
                    && clickEvent.unit().getFaction() == turnManagerContext.getCurrentPlayer().getFaction()) {
                this.movementOverlay = MovementService.compute(clickEvent.axial(),
                        clickEvent.unit().getMovementPoints(),
                        gameMapContext, battleFieldContext, playerFaction);
            } else if (clickEvent != null) {
                this.movementOverlay = null;
            }

            this.current = new Selection(clickEvent.axial(), clickEvent.hex(), clickEvent.unit());

            soundManager.leftClickSound.play();

        } else if (this.current != null && this.movementOverlay != null
                && clickEvent.selectionType() == SelectionType.MOUSEACTION) {
            Axial newIntendedPosition = clickEvent.axial();
            HashMap<Axial, Integer> reachableHexes = movementOverlay.reachableCosts();
            HashMap<Axial, Integer> attackable = movementOverlay.attackableCosts();

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
                                gameMapContext, battleFieldContext, playerFaction);

                        soundManager.rightClickSuccesSound.play();
                    }

                }
            } else if (attackable.containsKey(newIntendedPosition)) {

                Integer currentMovementPoints = current.unit().getMovementPoints();
                Integer costForMovementAndAttack = attackable.get(newIntendedPosition);
                if (costForMovementAndAttack == null) {
                    return;
                }

                Integer newPointsAfterAttack = currentMovementPoints - costForMovementAndAttack;

                if (newPointsAfterAttack >= 0) {
                    AttackResult result = battleFieldContext.attackUnit(current.unit(), newIntendedPosition,
                            newPointsAfterAttack);

                    switch (result) {
                        case FULLDEFEAT -> this.clear();
                        case FULLVICTORY -> {
                            this.current = new Selection(clickEvent.axial(), clickEvent.hex(), current.unit());
                            this.movementOverlay = MovementService.compute(current.unit().getPosition(),
                                    current.unit().getMovementPoints(),
                                    gameMapContext, battleFieldContext, playerFaction);
                        }
                        default -> System.out.println("Hello world");
                    }

                    soundManager.rightClickSuccesSound.play();

                }

            } else {
                soundManager.rightClickInvalidsound.play();
            }

        }

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

}
