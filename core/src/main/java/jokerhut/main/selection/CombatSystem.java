package jokerhut.main.selection;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.ObjectMap;

import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.screen.BattleField;

public class CombatSystem {

    BattleField battleField;

    ObjectMap<AbstractUnit, Combat> activeAttacks = new ObjectMap<>();
    private final ArrayList<CombatListener> listeners = new ArrayList<>();

    public void addListener(CombatListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CombatListener listener) {
        listeners.remove(listener);
    }

    public void notifyCombatFinished(AbstractUnit attacker) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onCombatFinished(attacker);
        }
    }

    public void startAttackTimer(AbstractUnit attacker) {
        if (activeAttacks.containsKey(attacker)) {
            return;
        }
        activeAttacks.put(attacker, new Combat(attacker, attacker));
    }

    public boolean isUnitAttacking(AbstractUnit unit) {
        return activeAttacks.containsKey(unit);
    }

    public void updateAttackTimer(float dt) {

        ObjectMap.Entries<AbstractUnit, Combat> activeCombats = activeAttacks.entries();
        Iterator<ObjectMap.Entry<AbstractUnit, Combat>> iterator = activeCombats.iterator();

        while (iterator.hasNext()) {

            ObjectMap.Entry<AbstractUnit, Combat> entry = iterator.next();
            Combat combat = entry.value;

            if (combat.tick(dt)) {
                iterator.remove();
                notifyCombatFinished(combat.attacker);
            }

        }

    }

    public CombatSystem(BattleField battleField) {
        this.battleField = battleField;
    }

}
