package jokerhut.main.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import jokerhut.main.UI.sidebar.SidebarStage;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.enums.HexDebugType;
import jokerhut.main.model.geo.TerrainProps;
import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.player.Player;
import jokerhut.main.model.selection.Selection;
import jokerhut.main.renderer.GameRenderer;
import jokerhut.main.systems.audio.SoundManager;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.systems.camera.CameraController;
import jokerhut.main.systems.combat.CombatSystem;
import jokerhut.main.systems.effect.EffectSystem;
import jokerhut.main.systems.input.InputProcessorSystem;
import jokerhut.main.systems.movement.MovementSystem;
import jokerhut.main.systems.selection.DragEventBroadcaster;
import jokerhut.main.systems.selection.SelectionBroadcaster;
import jokerhut.main.systems.selection.SelectionController;
import jokerhut.main.systems.turn.TurnManager;
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
	private TiledMap map;
	private HexagonalTiledMapRenderer hexmapRenderer;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private HashMap<Axial, Hex> hexMap;
	private HashMap<Axial, Integer> supplyField = new HashMap<>();

	// ----- GAME LOGIC ----- //
	private BattleField battleField;
	private final SelectionBroadcaster selectionBroadcaster = new SelectionBroadcaster();
	private final DragEventBroadcaster dragEventBroadcaster = new DragEventBroadcaster();
	private InputProcessorSystem inputProcessorSystem;
	private SelectionController selectionController;

	// ----- TURN RELATED STUFF ----- //
	private Player axisPlayer;
	private Player alliedPlayer;
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

	private CameraController cameraController;
	private final int SIDEBAR_PX = 360;

	@Override
	public void create() {

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(2f);

		map = new TmxMapLoader().load("map/pzcmap.tmx");

		hexmapRenderer = new HexagonalTiledMapRenderer(map, 1f);
		shapeRenderer = new ShapeRenderer();

		this.cameraController = new CameraController(map, dragEventBroadcaster);

		this.axisPlayer = new Player(Faction.GERMAN, 500);
		this.alliedPlayer = new Player(Faction.BRITISH, 500);

		soundManager = new SoundManager();
		battleField = new BattleField(this, axisPlayer, alliedPlayer, soundManager);

		movementSystem = new MovementSystem(battleField, 600f);
		effectSystem = new EffectSystem();

		int[][] offsetGrid = TerrainUtils.generateBaseLayerWith2DCoordinates(map);
		IntMap<TerrainProps> tileProps = TerrainUtils.buildTileProps(map);
		hexMap = TerrainUtils.generateAxialMap(offsetGrid, tileProps);

		TerrainUtils.enrichHexesFromTiles(map, hexMap, supplyField);

		List<Player> players = new ArrayList<>();
		players.add(alliedPlayer);
		players.add(axisPlayer);

		this.combatSystem = new CombatSystem(battleField);

		turnManager = new TurnManager(players, combatSystem, movementSystem, soundManager, hexMap, supplyField);
		turnManager.startTurn();

		selectionController = new SelectionController(hexMap, battleField, turnManager, soundManager, movementSystem,
				effectSystem, combatSystem);
		selectionBroadcaster.subscribe(selectionController);

		sidebarStage = new SidebarStage(new ScreenViewport(), batch, turnManager, selectionController);
		inputProcessorSystem = new InputProcessorSystem(cameraController,
				cameraController.getWorldViewport(), hexMap, battleField,
				selectionBroadcaster, dragEventBroadcaster,
				sidebarStage);

		gameRenderer = new GameRenderer(hexmapRenderer, cameraController.getCamera(), shapeRenderer, batch, hexMap,
				battleField,
				selectionController, axisPlayer,
				alliedPlayer, movementSystem);

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(sidebarStage);
		inputMultiplexer.addProcessor(inputProcessorSystem);

		Gdx.input.setInputProcessor(inputMultiplexer);

		long id = soundManager.gameTheme.play();
		soundManager.gameTheme.setVolume(id, 0f);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float dt = Gdx.graphics.getDeltaTime();
		movementSystem.updateActiveMovements(dt);
		combatSystem.updateAttackTimer(dt);
		effectSystem.update(dt);

		cameraController.applyWorldViewport();
		cameraController.update(dt);

		gameRenderer.render();

		Selection currentSelection = selectionController.getCurrentSelection();
		if (currentSelection != null) {
			sidebarStage.updateState(currentSelection);
		}

		batch.setProjectionMatrix(cameraController.getCamera().combined);
		batch.begin();
		HexDebugUtils.renderHexInfo(HexDebugType.AXIAL, hexMap, batch, font);

		effectSystem.render(batch);
		batch.end();

		cameraController.applySidebarViewport(sidebarStage);
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
		cameraController.resize(width, height, sidebarStage);

	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public Texture getImage() {
		return image;
	}

	public void setImage(Texture image) {
		this.image = image;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public HexagonalTiledMapRenderer getHexmapRenderer() {
		return hexmapRenderer;
	}

	public void setHexmapRenderer(HexagonalTiledMapRenderer hexmapRenderer) {
		this.hexmapRenderer = hexmapRenderer;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public HashMap<Axial, Hex> getHexMap() {
		return hexMap;
	}

	public void setHexMap(HashMap<Axial, Hex> hexMap) {
		this.hexMap = hexMap;
	}

	public HashMap<Axial, Integer> getSupplyField() {
		return supplyField;
	}

	public void setSupplyField(HashMap<Axial, Integer> supplyField) {
		this.supplyField = supplyField;
	}

	public BattleField getBattleField() {
		return battleField;
	}

	public void setBattleField(BattleField battleField) {
		this.battleField = battleField;
	}

	public SelectionBroadcaster getSelectionBroadcaster() {
		return selectionBroadcaster;
	}

	public InputProcessorSystem getInputProcessorSystem() {
		return inputProcessorSystem;
	}

	public void setInputProcessorSystem(InputProcessorSystem inputProcessorSystem) {
		this.inputProcessorSystem = inputProcessorSystem;
	}

	public SelectionController getSelectionController() {
		return selectionController;
	}

	public void setSelectionController(SelectionController selectionController) {
		this.selectionController = selectionController;
	}

	public Player getAxisPlayer() {
		return axisPlayer;
	}

	public void setAxisPlayer(Player axisPlayer) {
		this.axisPlayer = axisPlayer;
	}

	public Player getAlliedPlayer() {
		return alliedPlayer;
	}

	public void setAlliedPlayer(Player alliedPlayer) {
		this.alliedPlayer = alliedPlayer;
	}

	public TurnManager getTurnManager() {
		return turnManager;
	}

	public void setTurnManager(TurnManager turnManager) {
		this.turnManager = turnManager;
	}

	public MovementSystem getMovementSystem() {
		return movementSystem;
	}

	public void setMovementSystem(MovementSystem movementSystem) {
		this.movementSystem = movementSystem;
	}

	public CombatSystem getCombatSystem() {
		return combatSystem;
	}

	public void setCombatSystem(CombatSystem combatSystem) {
		this.combatSystem = combatSystem;
	}

	public SidebarStage getSidebarStage() {
		return sidebarStage;
	}

	public void setSidebarStage(SidebarStage sidebarStage) {
		this.sidebarStage = sidebarStage;
	}

	public GameRenderer getGameRenderer() {
		return gameRenderer;
	}

	public void setGameRenderer(GameRenderer gameRenderer) {
		this.gameRenderer = gameRenderer;
	}

	public EffectSystem getEffectSystem() {
		return effectSystem;
	}

	public void setEffectSystem(EffectSystem effectSystem) {
		this.effectSystem = effectSystem;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	public CameraController getCameraController() {
		return cameraController;
	}

	public void setCameraController(CameraController cameraController) {
		this.cameraController = cameraController;
	}

	public int getSIDEBAR_PX() {
		return SIDEBAR_PX;
	}

}
