package jokerhut.main;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.utils.HexUtils;
import jokerhut.main.utils.TerrainUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    private OrthographicCamera camera;
    private TiledMap map;
    private HexagonalTiledMapRenderer hexmapRenderer;
    private int[][] terrain;
    private ShapeRenderer shapeRenderer;
private BitmapFont font;
    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 2f;
        font = new BitmapFont();

        map = new TmxMapLoader().load("map/pzcorpsdesert.tmx");
        hexmapRenderer = new HexagonalTiledMapRenderer(map, 1f);
        shapeRenderer = new ShapeRenderer();
        terrain = TerrainUtils.generateTerrainWith2DCoordinates(map);


    }

    public void drawHex(ShapeRenderer shapeRenderer, float pixelX, float pixelY, float radius) {
        for (int i = 0; i < 6; i++) {
            double a1 = Math.toRadians(60 * i);
            double a2 = Math.toRadians(60 * (i + 1) % 6);

            float y1 = pixelX + (float) (radius * Math.cos(a1));
            float y2 = pixelY + (float) (radius * Math.sin(a1));

            float x1 = pixelX + (float) (radius * Math.cos(a2));
            float x2 = pixelY + (float) (radius * Math.cos(a2));

            shapeRenderer.line(x1, y1, x2, y2);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        hexmapRenderer.setView(camera);
        hexmapRenderer.render();

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        shapeRenderer.setProjectionMatrix(camera.combined);

        HashMap<Axial, Integer> axialTiles = TerrainUtils.generateAxialTiles(map);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        float width = layer.getTileWidth();
        float height = layer.getTileHeight();

        for (Axial axial : axialTiles.keySet()) {
            Vector2 pixelPosition = HexUtils.axialToPixelFlatTop(axial, width, height);
            float radius = width * 0.5f;
            drawHex(shapeRenderer, pixelPosition.x, pixelPosition.y, radius);
        }

        shapeRenderer.end();

    }




    @Override
    public void dispose() {
        hexmapRenderer.dispose();
        map.dispose();
    }
}
