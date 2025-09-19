package jokerhut.main.utils;

import java.util.HashMap;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.systems.battlefield.BattleField;

public class HexUtils {

    public static Vector2 axialToPixelCenter(Hex hex) {

        float x = (3f / 2f) * hex.getQ();
        float y = (float) (Math.sqrt(3) / 2 * hex.getQ() + Math.sqrt(3) * hex.getR());

        x = x * GameConstants.HEX_SIZE + GameConstants.HEX_SIZE;
        y = y * GameConstants.HEX_SIZE * GameConstants.HEX_Y_SCALE;

        return new Vector2(x, y);

    }

    public static Vector2 axialToPixelCenter(Axial axial) {

        float x = (3f / 2f) * axial.q();
        float y = (float) (Math.sqrt(3) / 2 * axial.q() + Math.sqrt(3) * axial.r());

        x = x * GameConstants.HEX_SIZE + GameConstants.HEX_SIZE;
        y = y * GameConstants.HEX_SIZE * GameConstants.HEX_Y_SCALE;

        return new Vector2(x, y);

    }

    public static Axial pickStaging(Axial target, HashMap<Axial, Integer> reachable, BattleField bf) {
        int[][] dirs = HEX_NEIGHBORS;
        Axial best = null;
        int bestCost = Integer.MAX_VALUE;
        for (int[] d : dirs) {
            Axial n = new Axial(target.q() + d[0], target.r() + d[1]);
            Integer c = reachable.get(n);
            if (c == null)
                continue; // not reachable
            if (bf.unitAt(n) != null)
                continue; // occupied
            if (c < bestCost) {
                best = n;
                bestCost = c;
            }
        }
        return best;
    }

    public static final int[][] HEX_NEIGHBORS = {
            { 1, 0 }, { 1, -1 }, { 0, -1 },
            { -1, 0 }, { -1, 1 }, { 0, 1 }
    };

    public static boolean areNeighbors(Axial a, Axial b) {
        int dq = b.q() - a.q();
        int dr = b.r() - a.r();
        for (int[] dir : HEX_NEIGHBORS) {
            if (dq == dir[0] && dr == dir[1])
                return true;
        }
        return false;
    }

    public static Axial pixelToNearestAxial(float px, float py, float size) {

        float X = (px - size) / size;
        float Y = py / (size * GameConstants.HEX_Y_SCALE);

        float qf = (2f / 3f) * X;
        float rf = (-1f / 3f) * X + (float) (1.0 / Math.sqrt(3.0)) * Y;
        return roundToAxial(qf, rf);

    }

    public static Axial roundToAxial(float qf, float rf) {
        float sf = -qf - rf;
        int q = Math.round(qf);
        int r = Math.round(rf);
        int s = Math.round(sf);

        float dq = Math.abs(q - qf);
        float dr = Math.abs(r - rf);
        float ds = Math.abs(s - sf);

        if (dq > dr && dq > ds)
            q = -r - s;
        else if (dr > ds)
            r = -q - s;

        return new Axial(q, r);
    }

    public static Axial offsetToAxial(int col, int row) {
        int parity = col & 1;

        int q = col;
        int r = row - (col - parity) / 2;

        return new Axial(q, r);

    }

    public static Axial offsetToAxial(int col, int row, boolean oddQ) {

        int q = col;
        int r = oddQ
                ? row - (col + (col & 1)) / 2
                : row - (col - (col & 1)) / 2;
        return new Axial(q, r + 1);

    }

    public static void drawHexOutline(ShapeRenderer shapeRenderer, Vector2 pixelCoordinates, float radius,
            float yScale) {

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

    public static void fillHex(ShapeRenderer shapeRenderer, Vector2 pixelCoordinates, float radius, float yScale) {
        float centerX = pixelCoordinates.x;
        float centerY = pixelCoordinates.y;

        float[] x = new float[6], y = new float[6];
        for (int i = 0; i < 6; i++) {
            double a = Math.toRadians(60 * i);
            x[i] = centerX + (float) (radius * Math.cos(a));
            y[i] = centerY + (float) (yScale * radius * Math.sin(a));
        }

        for (int i = 0; i < 6; i++) {
            int j = (i + 1) % 6;
            shapeRenderer.triangle(centerX, centerY, x[i], y[i], x[j], y[j]);
        }
    }

}
