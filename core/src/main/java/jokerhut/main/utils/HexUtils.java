package jokerhut.main.utils;

import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;

public class HexUtils {



    //----- CONVERTING FROM 2D TO AXIAL -----//
    public static int isOdd(int number) {
        if (number % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public static Axial toAxialOddR(int row, int col) {
        int q = col - (row - isOdd(row)) / 2;
        int r = row;
        return new Axial(q, r);
    }

    public static Axial toAxialOddQ(int row, int col) {
        int q = col;
        int r = row - ((col - isOdd(col)) / 2);
        return new Axial(q, r);
    }



   public static Vector2 axialToPixelFlatTop(Axial axial, float width, float height) {
       float size = width * 0.5f;
       float centerX = width * 0.5f;
       float centerY = height * 0.5f;

       float x = centerX + (1.5f * size) * axial.q();
       float y = centerY + (float)(Math.sqrt(3) * size) * (axial.r() + axial.q() * 0.5f);

       return new Vector2(x, y);

   }

}
