package jokerhut.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.TerrainProps;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.entities.InfantryUnit;
import jokerhut.main.utils.HexDebugUtils;
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

    private ArrayList<AbstractUnit> unitList;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 6f;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        map = new TmxMapLoader().load("map/pzcmap.tmx");
        hexmapRenderer = new HexagonalTiledMapRenderer(map, 1f);
        shapeRenderer = new ShapeRenderer();


        int[][] offsetGrid = TerrainUtils.generateTerrainWith2DCoordinates(map);
        IntMap<TerrainProps> tileProps = TerrainUtils.buildTileProps(map);
        hexMap = TerrainUtils.generateAxialMap(offsetGrid, tileProps);

        unitList = spawnUnits();

    }

    private ArrayList<AbstractUnit> spawnUnits () {

        // InfantryUnit unitOne = new InfantryUnit();
        // 2,14 and 1,13

        ArrayList<AbstractUnit> unitList = new ArrayList<>();

        InfantryUnit unitOne = new InfantryUnit(new Axial(2, 14), new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))));
        InfantryUnit unitTwo = new InfantryUnit(new Axial(1, 13), new TextureRegion(new Texture(Gdx.files.internal("units/UK_INF.png"))));

        unitList.add(unitOne);
        unitList.add(unitTwo);

        return unitList;

    }



    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(16 * GameConstants.HEX_SIZE, 16 * GameConstants.HEX_SIZE, 0);

        camera.update();
        hexmapRenderer.setView(camera);
        hexmapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Hex hex : hexMap.values()) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(hex);
            HexUtils.drawHexOutline(shapeRenderer, pixelCoordinates, GameConstants.HEX_SIZE, GameConstants.HEX_Y_SCALE);
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (AbstractUnit unit : unitList) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(unit.getPosition());
            batch.draw(unit.getSprite(), pixelCoordinates.x + GameConstants.PIXEL_X_DRAW_CORRECTION, pixelCoordinates.y, GameConstants.HEX_WIDTH, GameConstants.HEX_HEIGHT);
        }

        HexDebugUtils.renderHexInfo(null, hexMap, batch, font);


        batch.end();

    }




    @Override
    public void dispose() {
        hexmapRenderer.dispose();
        map.dispose();
        shapeRenderer.dispose();
    }
}





