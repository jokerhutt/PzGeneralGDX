package jokerhut.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.DTs.TerrainProps;
import jokerhut.main.animation.EffectSystem;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.enums.Faction;
import jokerhut.main.input.InputProcessor;
import jokerhut.main.renderer.GameRenderer;
import jokerhut.main.screen.BattleField;
import jokerhut.main.screen.PlayerState;
import jokerhut.main.screen.TurnManager;
import jokerhut.main.selection.CombatSystem;
import jokerhut.main.selection.MovementSystem;
import jokerhut.main.selection.SelectionBroadcaster;
import jokerhut.main.selection.SelectionState;
import jokerhut.main.sound.SoundManager;
import jokerhut.main.stage.SidebarStage;
import jokerhut.main.utils.HexDebugUtils;
import jokerhut.main.utils.TerrainUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
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

    // ----- TURN RELATED STUFF ----- //
    private PlayerState axisPlayer;
    private PlayerState alliedPlayer;
    private TurnManager turnManager;

    private MovementSystem movementSystem;
    private CombatSystem combatSystem;
    // ----- HUD STUFF ----- //
    private SidebarStage sidebarStage;

    // ----- RENDER STUFF ----- //
    private GameRenderer gameRenderer;
    private EffectSystem effectSystem;

    // ----- AUDIO STUFF ----- //
    private SoundManager soundManager;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 4f;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        map = new TmxMapLoader().load("map/pzcmap.tmx");
        hexmapRenderer = new HexagonalTiledMapRenderer(map, 1f);
        shapeRenderer = new ShapeRenderer();

        this.axisPlayer = new PlayerState(Faction.GERMAN, 500);
        this.alliedPlayer = new PlayerState(Faction.BRITISH, 500);

        soundManager = new SoundManager();
        battleField = new BattleField(this, axisPlayer, alliedPlayer, soundManager);

        movementSystem = new MovementSystem(battleField, 600f);
        effectSystem = new EffectSystem();

        int[][] offsetGrid = TerrainUtils.generateBaseLayerWith2DCoordinates(map);
        IntMap<TerrainProps> tileProps = TerrainUtils.buildTileProps(map);
        hexMap = TerrainUtils.generateAxialMap(offsetGrid, tileProps);

        List<PlayerState> players = new ArrayList<>();
        players.add(alliedPlayer);
        players.add(axisPlayer);

        this.combatSystem = new CombatSystem(battleField);

        turnManager = new TurnManager(players, combatSystem, movementSystem, soundManager);
        turnManager.startTurn();

        selectionState = new SelectionState(hexMap, battleField, turnManager, soundManager, movementSystem,
                effectSystem, combatSystem);
        broadcaster.subscribe(selectionState);

        gameRenderer = new GameRenderer(hexmapRenderer, camera, shapeRenderer, batch, hexMap, battleField,
                selectionState, axisPlayer,
                alliedPlayer, movementSystem);

        sidebarStage = new SidebarStage(new ScreenViewport(), batch, turnManager, selectionState);
        inputProcessor = new InputProcessor(camera, hexMap, battleField, broadcaster, sidebarStage);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(sidebarStage);
        inputMultiplexer.addProcessor(inputProcessor);

        Gdx.input.setInputProcessor(inputMultiplexer);

        long id = soundManager.gameTheme.play();
        soundManager.gameTheme.setVolume(id, 0f);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(32 * GameConstants.HEX_SIZE, 32 * GameConstants.HEX_SIZE, 0);

        camera.update();

        float dt = Gdx.graphics.getDeltaTime();
        movementSystem.updateActiveMovements(dt);
        combatSystem.updateAttackTimer(dt);
        effectSystem.update(dt);

        gameRenderer.render();

        Selection currentSelection = selectionState.getCurrentSelection();
        if (currentSelection != null) {
            sidebarStage.updateState(currentSelection);
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        HexDebugUtils.renderHexInfo(null, hexMap, batch, font);

        effectSystem.render(batch);
        batch.end();

        sidebarStage.act(Gdx.graphics.getDeltaTime());
        sidebarStage.draw();

    }

    @Override
    public void dispose() {
        hexmapRenderer.dispose();
        map.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        sidebarStage.getViewport().update(width, height, true);
        sidebarStage.layoutSidebar();
    }

}
