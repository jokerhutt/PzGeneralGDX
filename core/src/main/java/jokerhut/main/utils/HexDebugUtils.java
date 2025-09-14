package jokerhut.main.utils;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.enums.HexDebugType;

public class HexDebugUtils {

    public static void renderHexInfo (HexDebugType debugType, HashMap<Axial, Hex> hexMap, SpriteBatch batch, BitmapFont font) {

        if (debugType == null) {
            return;
        }

        for (Hex hex : hexMap.values()) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(hex);
            String label = getLabelByRenderType(hex, debugType);
            font.draw(batch, label == null ? "" : label, pixelCoordinates.x - GameConstants.PIXEL_X_DRAW_CORRECTION, pixelCoordinates.y + 8f);
        }

    }

    private static String getLabelByRenderType (Hex hex, HexDebugType debugType) {

        return switch (debugType) {

            case TERRAIN -> hex.getTerrain();
            case AXIAL -> hex.getQ() + ", " + hex.getR();
            case PIXELCOORDINATE -> {
                Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(hex);
                yield pixelCoordinates.x + ", " + pixelCoordinates.y;
            }
            case DEFENCE -> String.valueOf(hex.getDefence());
            case MOVECOST -> String.valueOf(hex.getMoveCost());


        };

    }

}
