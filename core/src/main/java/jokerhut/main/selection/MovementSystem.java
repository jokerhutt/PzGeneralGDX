package jokerhut.main.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.ObjectMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.screen.BattleField;

public class MovementSystem {
    private final ObjectMap<AbstractUnit, Motion> active = new ObjectMap<>();
    private final ArrayList<MovementListener> listeners = new ArrayList<>();

    private final BattleField battleField;
    private final float speedPxPerSec;

    public void addListener(MovementListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MovementListener listener) {
        listeners.remove(listener);
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
        active.put(unit, new Motion(unit, path, speedPxPerSec));
    }

    public boolean isUnitMoving(AbstractUnit unit) {
        return active.containsKey(unit);
    }

    public void updateActiveMovements(float dt) {

        ObjectMap.Entries<AbstractUnit, Motion> activeMotions = active.entries();
        ObjectMap.Entry<AbstractUnit, Motion> entry;
        Iterator<ObjectMap.Entry<AbstractUnit, Motion>> iterator = activeMotions.iterator();

        while (iterator.hasNext()) {

            entry = iterator.next();
            Motion motion = entry.value;

            float dtToCarryOver = motion.tick(dt);
            while (dtToCarryOver >= 0f) {
                boolean hasMore = motion.finishEdgeAndMaybeAdvance(battleField);
                if (!hasMore) {
                    iterator.remove();
                    notifyFinished(motion.unit);
                    break;
                }
                dtToCarryOver = motion.tick(dtToCarryOver);
            }

        }
    }
}
