package jokerhut.main.DTs;

public final class HexLayout {
    public final float tileWidth;
    public final float tileHeight;
    public final float radius;     // outer radius (corner → center)
    public final float root3;
    public final float verticalScale; // ky
    public final float originX;
    public final float originY;

    public HexLayout(float tileWidth, float tileHeight, float originX, float originY) {
        this.tileWidth  = tileWidth;
        this.tileHeight = tileHeight;
        this.radius     = tileWidth / 2f;
        this.root3      = (float) Math.sqrt(3);
        this.verticalScale = tileHeight / (root3 * radius);
        this.originX = originX;
        this.originY = originY;
    }

    public float centerX(int col) {
        return originX + 1.5f * radius * col;
    }

    public float centerY(int col, int row) {
        return originY + (root3 * radius * verticalScale) * (row + 0.5f * (col & 1));
    }
}
