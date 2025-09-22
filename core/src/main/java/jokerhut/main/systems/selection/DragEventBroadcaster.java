package jokerhut.main.systems.selection;

import java.util.ArrayList;
import java.util.List;

import jokerhut.main.listeners.DragListener;
import jokerhut.main.model.enums.DragEventType;
import jokerhut.main.model.selection.DragEvent;

public class DragEventBroadcaster {

	private final List<DragListener> listeners = new ArrayList<>();

	public void subscribe(DragListener dragEventListener) {
		listeners.add(dragEventListener);
	}

	public void unsubscribe(DragListener dragEventListener) {
		listeners.remove(dragEventListener);
	}

	public void broadcastEvent(DragEvent dragEvent, DragEventType type) {

		if (type == DragEventType.TOUCHDOWN) {
			for (DragListener dragEventListener : listeners) {
				dragEventListener.onTouchDown(dragEvent);
			}
		} else if (type == DragEventType.TOUCHDRAGGED) {
			for (DragListener dragEventListener : listeners) {
				dragEventListener.onTouchDragged(dragEvent);
			}
		} else if (type == DragEventType.TOUCHUP) {
			for (DragListener dragEventListener : listeners) {
				dragEventListener.onTouchUp(dragEvent);
			}
		}

	}
}
