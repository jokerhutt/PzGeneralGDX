package jokerhut.main.selection;

import java.util.ArrayList;
import java.util.List;

import jokerhut.main.DTs.ClickEvent;
import jokerhut.main.DTs.SelectionListener;

public class SelectionBroadcaster {

    private final List<SelectionListener> listeners = new ArrayList<>();

    public void subscribe(SelectionListener selectionListener) {
        listeners.add(selectionListener);
    }

    public void unsubscribe(SelectionListener selectionListener) {
        listeners.remove(selectionListener);
    }

    public void broadcastEvent(ClickEvent selection) {
        for (SelectionListener selectionListener : listeners) {
            selectionListener.onSelect(selection);
        }
    }

}
