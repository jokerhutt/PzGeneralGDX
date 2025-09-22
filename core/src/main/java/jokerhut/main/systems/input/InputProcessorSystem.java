package jokerhut.main.systems.input;

import java.util.HashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import jokerhut.main.UI.sidebar.SidebarStage;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.model.enums.SelectionType;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.selection.ClickEvent;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.systems.camera.CameraController;
import jokerhut.main.systems.selection.SelectionBroadcaster;
import jokerhut.main.utils.HexUtils;

public class InputProcessorSystem extends InputAdapter {

	private final CameraController cameraController;
	private final HashMap<Axial, Hex> hexes;
	private final BattleField battlefield;
	private final SelectionBroadcaster broadcaster;
	private final SidebarStage sidebarUi;
	private final Viewport worldVp;

	public InputProcessorSystem(CameraController cameraController, Viewport worldVp,
			HashMap<Axial, Hex> hexes,
			BattleField battlefield,
			SelectionBroadcaster broadcaster, SidebarStage sidebarUi) {
		this.hexes = hexes;
		this.cameraController = cameraController;
		this.worldVp = worldVp;
		this.battlefield = battlefield;
		this.broadcaster = broadcaster;
		this.sidebarUi = sidebarUi;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
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
		broadcaster.broadcastEvent(new ClickEvent(selectionType, axialCoordinates, hexInstance, unitInstance));

		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		System.out.println(amountY);
		cameraController.zoomBy(amountY * 0.02f);
		return true;
	}

}
