package jokerhut.main.systems.selection;

import java.util.ArrayList;
import java.util.List;

import jokerhut.main.model.selection.ClickEvent;
import jokerhut.main.listeners.SelectionListener;

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
