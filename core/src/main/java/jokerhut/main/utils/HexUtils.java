package jokerhut.main.utils;

import com.badlogic.gdx.math.Vector2;
import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.constants.GameConstants;

public class HexUtils {

    public static Vector2 axialToPixelCenter(Hex hex) {


        float x = (3f / 2f) * hex.q();
        float y = (float) (Math.sqrt(3) / 2 * hex.q() + Math.sqrt(3) * hex.r());

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

}
