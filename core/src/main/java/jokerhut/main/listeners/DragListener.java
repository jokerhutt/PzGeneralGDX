package jokerhut.main.listeners;

import jokerhut.main.model.selection.DragEvent;

public interface DragListener {

	boolean onTouchDragged(DragEvent dragevent);

	boolean onTouchDown(DragEvent dragEvent);

	boolean onTouchUp(DragEvent dragEvent);

}
