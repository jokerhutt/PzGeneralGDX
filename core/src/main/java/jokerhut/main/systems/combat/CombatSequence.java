package jokerhut.main.systems.combat;

import jokerhut.main.model.unit.AbstractUnit;

public final class CombatSequence {

    AbstractUnit defender;
    AbstractUnit attacker;

    float maxDuration = 2f;
    float elapsedTime = 0f;

    CombatSequence(AbstractUnit attacker, AbstractUnit defender) {

        this.defender = defender;
        this.attacker = attacker;

    }

    public boolean tick(float dt) {
        elapsedTime += dt;
        return elapsedTime >= maxDuration;
    }

}
