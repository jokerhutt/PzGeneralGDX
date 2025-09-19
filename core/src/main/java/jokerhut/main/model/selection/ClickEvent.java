package jokerhut.main.model.selection;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.enums.SelectionType;

public record ClickEvent(
        SelectionType selectionType,
        Axial axial,
        Hex hex,
        AbstractUnit unit) {

}
