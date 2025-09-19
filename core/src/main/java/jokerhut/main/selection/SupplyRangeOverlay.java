package jokerhut.main.selection;

import java.util.HashMap;

import jokerhut.main.model.hex.Axial;

public record SupplyRangeOverlay(

		Axial start,
		int supplyRange,
		HashMap<Axial, Integer> supplyableHexCosts,
		HashMap<Axial, Axial> parent

) {

}
