package jokerhut.main.systems.input;

import java.util.HashMap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import jokerhut.main.UI.sidebar.SidebarStage;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.model.enums.DragEventType;
import jokerhut.main.model.enums.SelectionType;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.selection.ClickEvent;
import jokerhut.main.model.selection.DragEvent;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.systems.camera.CameraController;
import jokerhut.main.systems.selection.DragEventBroadcaster;
import jokerhut.main.systems.selection.SelectionBroadcaster;
import jokerhut.main.utils.HexUtils;

public class InputProcessorSystem extends InputAdapter {

	private final CameraController cameraController;
	private final HashMap<Axial, Hex> hexes;
	private final BattleField battlefield;
	private final SelectionBroadcaster selectionBroadcaster;
	private final DragEventBroadcaster dragEventBroadcaster;
	private final SidebarStage sidebarUi;
	private final Viewport worldVp;

	public InputProcessorSystem(CameraController cameraController, Viewport worldVp,
			HashMap<Axial, Hex> hexes,
			BattleField battlefield,
			SelectionBroadcaster selectionBroadcaster, DragEventBroadcaster dragEventBroadcaster,
			SidebarStage sidebarUi) {
		this.hexes = hexes;
		this.cameraController = cameraController;
		this.worldVp = worldVp;
		this.battlefield = battlefield;
		this.selectionBroadcaster = selectionBroadcaster;
		this.dragEventBroadcaster = dragEventBroadcaster;
		this.sidebarUi = sidebarUi;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		if (button == Input.Buttons.MIDDLE) {
			dragEventBroadcaster.broadcastEvent(new DragEvent(x, y, pointer), DragEventType.TOUCHDOWN);
		}

		if (button != com.badlogic.gdx.Input.Buttons.LEFT && button != com.badlogic.gdx.Input.Buttons.RIGHT)
			return false;

		Vector2 position = new Vector2(x, y);
		sidebarUi.screenToStageCoordinates(position);
		if (sidebarUi.hit(position.x, position.y, true) != null)
			return false;

		Vector2 world = new Vector2(x, y);
		worldVp.unproject(world);

		SelectionType selectionType;
		if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
			selectionType = SelectionType.SELECTION;
		} else {
			selectionType = SelectionType.MOUSEACTION;
		}

		Axial axialCoordinates = HexUtils.pixelToNearestAxial(world.x, world.y,
				GameConstants.HEX_SIZE);
		Hex hexInstance = hexes.get(axialCoordinates);
		AbstractUnit unitInstance = battlefield.unitAt(axialCoordinates);
		selectionBroadcaster.broadcastEvent(new ClickEvent(selectionType, axialCoordinates, hexInstance, unitInstance));

		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		System.out.println(amountY);
		cameraController.onScroll(amountY);
		return true;
	}

	@Override
	public boolean touchDragged(int sx, int sy, int pointer) {
		dragEventBroadcaster.broadcastEvent(new DragEvent(sx, sy, pointer), DragEventType.TOUCHDRAGGED);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.MIDDLE) {
			dragEventBroadcaster.broadcastEvent(new DragEvent(screenX, screenY, pointer), DragEventType.TOUCHUP);
		}
		return false;
	}

}
