package jokerhut.main.selection;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Selection;
import jokerhut.main.DTs.SelectionListener;

public class SelectionState implements SelectionListener {

    private Selection current;
    public void onSelect(Selection s){ current = s; }

    public void clear(){ current = null; }

    public boolean has(){ return current != null; }

    public Selection getCurrentSelection(){ return current; }

    public boolean isSelected(Axial a){
    return current != null && current.axial().equals(a);
  }

}
