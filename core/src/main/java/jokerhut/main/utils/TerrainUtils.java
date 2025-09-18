package jokerhut.main.utils;

import java.util.HashMap;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.utils.IntMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.TerrainProps;
import jokerhut.main.terrain.TerrainProfile;

public class TerrainUtils {

    public static int[][] generateBaseLayerWith2DCoordinates(TiledMap map) {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        int width = layer.getWidth();
        int height = layer.getHeight();

        System.out.println("WIDTH OF LAYER 0: " + width + " HEIGHT OF LAYER 0: " + height);

        int[][] terrain = new int[width][height];

        int count = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    terrain[x][y] = cell.getTile().getId();
                } else {
                    terrain[x][y] = -1;
                }
                count++;
            }
        }

        System.out.println("LAYER 0 TRAVERSED: " + count);
        return terrain;

    }

    public static void enrichHexesFromTiles(TiledMap map, HashMap<Axial, Hex> axialMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        int w = layer.getWidth(), h = layer.getHeight();

        int count = 0;
        System.out.println("WIDTH OF LAYER 1: " + w + " HEIGHT OF LAYER 1: " + h);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                count++;
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null)
                    continue;

                Axial a = HexUtils.offsetToAxial(x, y, true);
                Hex hex = axialMap.get(a);
                if (hex == null)
                    continue;

                MapProperties p = cell.getTile().getProperties();

                TerrainProfile terrainProfile = new TerrainProfile();

                terrainProfile.setTerrainType(p.get("type", (String) null, String.class));
                terrainProfile.setProvidesSupply(p.get("providesSupply", false, Boolean.class));
                terrainProfile.setSupplyRange(p.get("supplyRange", 0, Integer.class));
                terrainProfile.setEntrenchCap(p.get("entrenchCap", 0, Integer.class));
                terrainProfile.setVictoryLocation(p.get("isVictoryLocation", false, Boolean.class));

                hex.setTerrainProfile(terrainProfile);

            }

        }

        System.out.println("LAYER 1 TRAVERSED: " + count);
    }

    public static HashMap<Axial, Hex> generateAxialMap(int[][] offsetGrid,
            IntMap<TerrainProps> tileProps) {

        HashMap<Axial, Hex> axialMap = new HashMap<>();

        for (int col = 0; col < offsetGrid.length; col++) {
            for (int row = 0; row < offsetGrid[col].length; row++) {

                int tileId = offsetGrid[col][row];
                if (tileId == -1)
                    continue;

                Axial axialCoordinates = HexUtils.offsetToAxial(col, row, true);
                TerrainProps terrainProps = tileProps.get(tileId);
                axialMap.put(axialCoordinates, new Hex(axialCoordinates.q(), axialCoordinates.r(),
                        terrainProps.terrain(), terrainProps.defense(), terrainProps.moveCost()));

            }
        }

        return axialMap;

    }

    public static IntMap<TerrainProps> buildTileProps(TiledMap map) {
        IntMap<TerrainProps> out = new IntMap<>();
        for (TiledMapTileSet tileSet : map.getTileSets()) {
            for (TiledMapTile mapTile : tileSet) {
                MapProperties props = mapTile.getProperties();
                String terr = props.get("terrain", (String) null, String.class);
                if (terr == null)
                    continue;

                int def = props.get("defence", 0, Integer.class);
                int move = props.get("moveCost", 1, Integer.class);

                out.put(mapTile.getId(), new TerrainProps(terr, def, move));
            }
        }
        return out;
    }

}
