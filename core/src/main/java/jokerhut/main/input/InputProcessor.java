package jokerhut.main.input;

import java.util.HashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.ClickEvent;
import jokerhut.main.DTs.Hex;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.enums.SelectionType;
import jokerhut.main.screen.BattleField;
import jokerhut.main.selection.SelectionBroadcaster;
import jokerhut.main.stage.SidebarStage;
import jokerhut.main.utils.HexUtils;

public class InputProcessor extends InputAdapter {

    private final OrthographicCamera camera;
    private final HashMap<Axial, Hex> hexes;
    private final BattleField battlefield;
    private final SelectionBroadcaster broadcaster;
    private final SidebarStage sidebarUi;

    public InputProcessor(OrthographicCamera cam,
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
