package jokerhut.main.listeners;

import jokerhut.main.model.unit.AbstractUnit;

public interface CombatListener {

    void onCombatFinished(AbstractUnit attacker);

}
