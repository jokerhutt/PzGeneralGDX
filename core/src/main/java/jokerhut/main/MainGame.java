package jokerhut.main;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.utils.HexUtils;
import jokerhut.main.utils.TerrainUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    private OrthographicCamera camera;
    private TiledMap map;
    private HexagonalTiledMapRenderer hexmapRenderer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private HashMap<Axial, Hex> hexMap;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 2f;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(0.5f); // optional: smaller Texture
        map = new TmxMapLoader().load("map/pzcorpsdesert.tmx");
        hexmapRenderer = new HexagonalTiledMapRenderer(map, 1f);
        shapeRenderer = new ShapeRenderer();


        int[][] offsetGrid = TerrainUtils.generateTerrainWith2DCoordinates(map);
        hexMap = TerrainUtils.generateAxialMap(offsetGrid);


    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        hexmapRenderer.setView(camera);
        hexmapRenderer.render();


        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int cols = layer.getWidth();
        int rows = layer.getHeight();

        MapProperties p = map.getProperties();
        float tileW = p.get("tilewidth", Integer.class);     // 32
        float tileH = p.get("tileheight", Integer.class);    // 32

        final float size = tileW / 2f;
        final float root3 = (float) Math.sqrt(3);

        final float ky = tileH / (root3 * size);

        final float originX = size, originY = 0f;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Hex hex : hexMap.values()) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(hex);
            drawHex(shapeRenderer, pixelCoordinates, GameConstants.HEX_SIZE, GameConstants.HEX_Y_SCALE);
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                float cx = originX + 1.5f * size * col;
                float cy = originY + (root3 * size * ky) * (row + 0.5f * (col & 1));

                int q = col;
                int r = row - ((col - (col & 1)) / 2);
                font.draw(batch, q + "," + r, cx - 8f, cy + 4f);
            }
        }
        batch.end();

    }


    private static void drawHex(ShapeRenderer shapeRenderer, Vector2 pixelCoordinates, float radius, float yScale) {


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


    @Override
    public void dispose() {
        hexmapRenderer.dispose();
        map.dispose();
        shapeRenderer.dispose();
    }
}





