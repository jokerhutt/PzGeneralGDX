package jokerhut.main.listeners;

import jokerhut.main.model.unit.AbstractUnit;

public interface MovementListener {

    void onMotionFinished(AbstractUnit unit);

}
