package jokerhut.main.utils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.constants.GameConstants;

public class HexUtils {

    public static Vector2 axialToPixelCenter(Hex hex) {


        float x = (3f / 2f) * hex.getQ();
        float y = (float) (Math.sqrt(3) / 2 * hex.getQ() + Math.sqrt(3) * hex.getR());

        x = x * GameConstants.HEX_SIZE + GameConstants.HEX_SIZE;
        y = y * GameConstants.HEX_SIZE * GameConstants.HEX_Y_SCALE;

        return new Vector2(x, y);

    }

    public static float pixelToNearestAxial() {

        return 1f;

    }

    public static Axial offsetToAxial(int col, int row) {

        int parity = col & 1;

        int q = col;
        int r = row - (col - parity) / 2;

        return new Axial(q, r);

    }


    public static void drawHexOutline(ShapeRenderer shapeRenderer, Vector2 pixelCoordinates, float radius, float yScale) {


        float centerX = pixelCoordinates.x;
        float centerY = pixelCoordinates.y;

        float firstX = 0f, firstY = 0f;
        float prevX = 0f, prevY = 0f;

        for (int cornerIndex = 0; cornerIndex < 6; cornerIndex++) {
            double angleRad = Math.toRadians(60 * cornerIndex);

            float cornerX = centerX + (float) (radius * Math.cos(angleRad));
            float cornerY = centerY + (float) (yScale * radius * Math.sin(angleRad));

            if (cornerIndex == 0) {
                firstX = cornerX;
                firstY = cornerY;
            } else {
                shapeRenderer.line(prevX, prevY, cornerX, cornerY);
            }

            prevX = cornerX;
            prevY = cornerY;

        }

        shapeRenderer.line(prevX, prevY, firstX, firstY);
    }

}
