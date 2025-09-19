package jokerhut.main.systems.input;

import java.util.HashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.selection.ClickEvent;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.enums.SelectionType;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.systems.selection.SelectionBroadcaster;
import jokerhut.main.UI.sidebar.SidebarStage;
import jokerhut.main.utils.HexUtils;

public class InputProcessorSystem extends InputAdapter {

    private final OrthographicCamera camera;
    private final HashMap<Axial, Hex> hexes;
    private final BattleField battlefield;
    private final SelectionBroadcaster broadcaster;
    private final SidebarStage sidebarUi;

    public InputProcessorSystem(OrthographicCamera cam,
                                HashMap<Axial, Hex> hexes,
                                BattleField battlefield,
                                SelectionBroadcaster broadcaster, SidebarStage sidebarUi) {
        this.camera = cam;
        this.hexes = hexes;
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

        SelectionType selectionType;
        if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
            selectionType = SelectionType.SELECTION;
        } else {
            selectionType = SelectionType.MOUSEACTION;
        }

        Vector3 selectedPixels = camera.unproject(new Vector3(x, y, 0));
        Axial axialCoordinates = HexUtils.pixelToNearestAxial(selectedPixels.x, selectedPixels.y,
                GameConstants.HEX_SIZE);
        Hex hexInstance = hexes.get(axialCoordinates);
        AbstractUnit unitInstance = battlefield.unitAt(axialCoordinates);
        broadcaster.broadcastEvent(new ClickEvent(selectionType, axialCoordinates, hexInstance, unitInstance));

        return true;
    }

}
