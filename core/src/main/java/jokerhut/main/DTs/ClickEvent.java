package jokerhut.main.DTs;

import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.enums.SelectionType;

public record ClickEvent(
        SelectionType selectionType,
        Axial axial,
        Hex hex,
        AbstractUnit unit) {

}
