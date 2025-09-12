package jokerhut.main.utils;

import java.util.HashMap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import jokerhut.main.DTs.Axial;

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

public static HashMap<Axial, Integer> generateAxialTiles (TiledMap map) {

    HashMap<Axial, Integer> axialTiles = new HashMap<>();

    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
    for (int y = 0; y < layer.getHeight(); y++) {
        for (int x = 0; x < layer.getWidth(); x++) {
            Cell cell = layer.getCell(x, y);
            if (cell == null) continue;
            axialTiles.put(HexUtils.toAxialOddQ(x, y), cell.getTile().getId());
        }
    }
    return axialTiles;

}


}
