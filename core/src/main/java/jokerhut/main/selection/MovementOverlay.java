package jokerhut.main.selection;

import java.util.HashMap;

import jokerhut.main.model.hex.Axial;

public record MovementOverlay(
        // Starting Hex
        Axial start,

        // Snapshot of the units movement points at time of computation
        int movementPointsLeft,

        // Movement costs of all reachable Hexes
        HashMap<Axial, Integer> reachableCosts,

        HashMap<Axial, Integer> attackableCosts,

        // Breadcrumb trail for units (Mostly for AI, will not use much for now)
        HashMap<Axial, Axial> parent) {

}
