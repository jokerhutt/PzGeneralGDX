package jokerhut.main.systems.combat;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.ObjectMap;

import jokerhut.main.listeners.CombatListener;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.systems.battlefield.BattleField;

public class CombatSystem {

    BattleField battleField;

    ObjectMap<AbstractUnit, CombatSequence> activeAttacks = new ObjectMap<>();
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

    public boolean isEmpty() {
        return this.activeAttacks.isEmpty();
    }

    public void startAttackTimer(AbstractUnit attacker) {
        if (activeAttacks.containsKey(attacker)) {
            return;
        }
        activeAttacks.put(attacker, new CombatSequence(attacker, attacker));
    }

    public boolean isUnitAttacking(AbstractUnit unit) {
        return activeAttacks.containsKey(unit);
    }

    public void updateAttackTimer(float dt) {

        ObjectMap.Entries<AbstractUnit, CombatSequence> activeCombats = activeAttacks.entries();
        Iterator<ObjectMap.Entry<AbstractUnit, CombatSequence>> iterator = activeCombats.iterator();

        while (iterator.hasNext()) {

            ObjectMap.Entry<AbstractUnit, CombatSequence> entry = iterator.next();
            CombatSequence combatSequence = entry.value;

            if (combatSequence.tick(dt)) {
                iterator.remove();
                notifyCombatFinished(combatSequence.attacker);
            }

        }

    }

    public CombatSystem(BattleField battleField) {
        this.battleField = battleField;
    }

}
