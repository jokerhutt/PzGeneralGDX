package jokerhut.main.systems.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.ObjectMap;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.listeners.MovementListener;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.systems.battlefield.BattleField;

public class MovementSystem {
    private final ObjectMap<AbstractUnit, MovementSequence> active = new ObjectMap<>();
    private final ArrayList<MovementListener> listeners = new ArrayList<>();

    private final BattleField battleField;
    private final float speedPxPerSec;

    public void addListener(MovementListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MovementListener listener) {
        listeners.remove(listener);
    }

    public boolean isEmpty() {
        return this.active.isEmpty();
    }

    private void notifyFinished(AbstractUnit u) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMotionFinished(u);
        }
    }

    public MovementSystem(BattleField battleField, float speedPxPerSec) {
        this.battleField = battleField;
        this.speedPxPerSec = speedPxPerSec;
    }

    public void move(AbstractUnit unit, List<Axial> path) {
        if (active.containsKey(unit) || path == null || path.isEmpty()) {
            return;
        }
        active.put(unit, new MovementSequence(unit, path, speedPxPerSec));
    }

    public boolean isUnitMoving(AbstractUnit unit) {
        return active.containsKey(unit);
    }

    public void updateActiveMovements(float dt) {

        ObjectMap.Entries<AbstractUnit, MovementSequence> activeMotions = active.entries();
        ObjectMap.Entry<AbstractUnit, MovementSequence> entry;
        Iterator<ObjectMap.Entry<AbstractUnit, MovementSequence>> iterator = activeMotions.iterator();

        while (iterator.hasNext()) {

            entry = iterator.next();
            MovementSequence movementSequence = entry.value;

            float dtToCarryOver = movementSequence.tick(dt);
            while (dtToCarryOver >= 0f) {
                boolean hasMore = movementSequence.finishEdgeAndMaybeAdvance(battleField);
                if (!hasMore) {
                    iterator.remove();
                    notifyFinished(movementSequence.unit);
                    break;
                }
                dtToCarryOver = movementSequence.tick(dtToCarryOver);
            }

        }
    }
}
