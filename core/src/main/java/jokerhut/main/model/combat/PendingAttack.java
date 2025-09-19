package jokerhut.main.model.combat;

import jokerhut.main.model.hex.Axial;

public record PendingAttack(
        Axial newIntendedPosition,
        Integer mpAfter) {

}
