package jokerhut.main.input;

import java.util.HashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.screen.BattleField;
import jokerhut.main.selection.SelectionBroadcaster;
import jokerhut.main.utils.HexUtils;

public class InputProcessor extends InputAdapter {


    private final OrthographicCamera camera;
    private final HashMap<Axial, Hex> hexes;
    private final BattleField battlefield;
    private final SelectionBroadcaster broadcaster;

        public InputProcessor(OrthographicCamera cam,
                                   HashMap<Axial, Hex> hexes,
                                   BattleField battlefield,
                                   SelectionBroadcaster broadcaster) {
        this.camera = cam;
        this.hexes = hexes;
        this.battlefield = battlefield;
        this.broadcaster = broadcaster;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (button != com.badlogic.gdx.Input.Buttons.LEFT) return false;

        Vector3 w = camera.unproject(new Vector3(x, y, 0));
        Axial a = HexUtils.pixelToNearestAxial(w.x, w.y, GameConstants.HEX_SIZE);
        Hex h = hexes.get(a);
        AbstractUnit u = battlefield.unitAt(a);
        System.out.println(a.q() + ", " + a.r());
        broadcaster.broadcastEvent(new Selection(a, h, u));
        return true;
    }



}
