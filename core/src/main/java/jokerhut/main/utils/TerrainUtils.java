package jokerhut.main.utils;

import java.util.HashMap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;

public class TerrainUtils {

    public static int[][] generateTerrainWith2DCoordinates (TiledMap map) {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        int width = layer.getWidth();
        int height = layer.getHeight();

        int[][] terrain = new int[width][height];


for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell != null) {
            terrain[x][y] = cell.getTile().getId();
        } else {
            terrain[x][y] = -1;
        }
    }
}

return terrain;

    }

public static HashMap<Axial, Hex> generateAxialMap (int[][] offsetGrid) {

    HashMap<Axial, Hex> axialMap = new HashMap<>();

    for (int col = 0; col < offsetGrid.length; col++) {
        for (int row = 0; row < offsetGrid[col].length; row++) {

            int tileId = offsetGrid[col][row];
            if (tileId == -1) continue;

            Axial axialCoordinates = HexUtils.offsetToAxial(col, row);
            axialMap.put(axialCoordinates, new Hex(axialCoordinates.q(), axialCoordinates.r()));

        }
    }

    return axialMap;


}


}
