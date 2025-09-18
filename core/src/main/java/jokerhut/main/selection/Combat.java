package jokerhut.main.selection;

import jokerhut.main.entities.AbstractUnit;

public final class Combat {

    AbstractUnit defender;
    AbstractUnit attacker;

    float maxDuration = 2f;
    float elapsedTime = 0f;

    Combat(AbstractUnit attacker, AbstractUnit defender) {

        this.defender = defender;
        this.attacker = attacker;

    }

    boolean tick(float dt) {
        elapsedTime += dt;
        return elapsedTime >= maxDuration;
    }

}
