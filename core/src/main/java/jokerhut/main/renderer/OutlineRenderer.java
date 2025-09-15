package jokerhut.main.renderer;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.utils.HexUtils;

public class OutlineRenderer {

    private final ShapeRenderer shapeRenderer;
    private final HashMap<Axial, Hex> hexMap;

    public OutlineRenderer(ShapeRenderer shapeRenderer, HashMap<Axial, Hex> hexMap) {
        this.shapeRenderer = shapeRenderer;
        this.hexMap = hexMap;
    }

    public void render() {

        shapeRenderer.setColor(Color.BLACK);

        for (Hex hex : hexMap.values()) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(hex);
            HexUtils.drawHexOutline(shapeRenderer, pixelCoordinates, GameConstants.HEX_SIZE, GameConstants.HEX_Y_SCALE);
        }
    }

}
