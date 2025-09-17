package jokerhut.main.selection;

import jokerhut.main.entities.AbstractUnit;

public interface CombatListener {

    void onCombatFinished(AbstractUnit attacker);

}
