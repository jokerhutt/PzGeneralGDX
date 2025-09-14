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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.DTs.TerrainProps;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.enums.HexDebugType;
import jokerhut.main.input.InputProcessor;
import jokerhut.main.screen.BattleField;
import jokerhut.main.selection.SelectionBroadcaster;
import jokerhut.main.selection.SelectionState;
import jokerhut.main.utils.HexDebugUtils;
import jokerhut.main.utils.HexUtils;
import jokerhut.main.utils.TerrainUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class MainGame extends ApplicationAdapter {

    // ----- LIBGDX OBJECTS ----- //
    private SpriteBatch batch;
    private Texture image;
    private OrthographicCamera camera;
    private TiledMap map;
    private HexagonalTiledMapRenderer hexmapRenderer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private HashMap<Axial, Hex> hexMap;

    // ----- GAME LOGIC ----- //
    private BattleField battleField;
    private final SelectionBroadcaster broadcaster = new SelectionBroadcaster();
    private InputProcessor inputProcessor;
    private SelectionState selectionState;

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
        battleField = new BattleField(this);


        int[][] offsetGrid = TerrainUtils.generateTerrainWith2DCoordinates(map);
        IntMap<TerrainProps> tileProps = TerrainUtils.buildTileProps(map);
        hexMap = TerrainUtils.generateAxialMap(offsetGrid, tileProps);

        selectionState = new SelectionState();
        broadcaster.subscribe(selectionState);

        inputProcessor = new InputProcessor(camera, hexMap, battleField, broadcaster);
        Gdx.input.setInputProcessor(inputProcessor);

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

        Selection currentSelection = selectionState.getCurrentSelection();
        if (currentSelection != null) {

            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Hex axialPosition = currentSelection.hex();
            Vector2 currentPosition = HexUtils.axialToPixelCenter(axialPosition);
            HexUtils.fillHex(shapeRenderer, currentPosition, GameConstants.HEX_SIZE, GameConstants.HEX_Y_SCALE);

            shapeRenderer.end();

        }

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Hex hex : hexMap.values()) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(hex);
            HexUtils.drawHexOutline(shapeRenderer, pixelCoordinates, GameConstants.HEX_SIZE, GameConstants.HEX_Y_SCALE);

        }

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (AbstractUnit unit : battleField.getUnitList()) {
            Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(unit.getPosition());
            batch.draw(unit.getSprite(), pixelCoordinates.x + GameConstants.PIXEL_X_DRAW_CORRECTION, pixelCoordinates.y, GameConstants.HEX_WIDTH, GameConstants.HEX_HEIGHT);
        }

        HexDebugUtils.renderHexInfo(HexDebugType.AXIAL, hexMap, batch, font);


        batch.end();

    }




    @Override
    public void dispose() {
        hexmapRenderer.dispose();
        map.dispose();
        shapeRenderer.dispose();
    }
}





