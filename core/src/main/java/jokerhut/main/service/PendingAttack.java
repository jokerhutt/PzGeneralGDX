package jokerhut.main.service;

import jokerhut.main.DTs.Axial;

public record PendingAttack(
        Axial newIntendedPosition,
        Integer mpAfter) {

}
