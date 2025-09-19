package jokerhut.main.model.selection;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.unit.AbstractUnit;

public record Selection(Axial axial, Hex hex, AbstractUnit unit) {

}
