package jokerhut.main.systems.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import jokerhut.main.UI.sidebar.SidebarStage;
import jokerhut.main.systems.selection.DragEventBroadcaster;

public class CameraController {

	private Viewport worldViewport;
	private OrthographicCamera camera;
	private TiledMap map;
	private CameraPanSystem panSystem;

	private int cols;
	private int tileW;
	private int tileH;
	private int rows;
	private float stepX;
	private float stepY;
	private float worldW;
	private float worldH;

	private int w;
	private int h;
	private int sidebarWidth;
	private int mapWidth;

	private static final float MIN_ZOOM = 0.2f;
	private static final float MAX_ZOOM = 0.85f;
	private static final float CLAMP_PADDING = -40f;
	private static final float ZOOM_RESP = 12f;
	private float targetZoom;

	public CameraController(TiledMap map, DragEventBroadcaster dragEventBroadCaster) {

		this.map = map;
		setupWorldSizes(map);

		this.camera = new OrthographicCamera();
		setupCamera();
		targetZoom = camera.zoom;

		this.worldViewport = new FitViewport(worldW, worldH, this.camera);
		setupViewport();

		this.panSystem = new CameraPanSystem(this);
		dragEventBroadCaster.subscribe(panSystem);

	}

	public void setupWorldSizes(TiledMap map) {

		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		this.cols = layer.getWidth();
		this.rows = layer.getHeight();
		this.tileW = layer.getTileWidth();
		this.tileH = layer.getTileHeight();

		this.stepX = tileW * 0.75f;
		this.stepY = tileH;
		this.worldW = tileW + (cols - 1) * stepX;
		this.worldH = tileH + (rows - 1) * stepY;

		this.w = Gdx.graphics.getWidth();
		this.h = Gdx.graphics.getHeight();
		this.sidebarWidth = Math.max(120, (int) (w * 0.18f));
		this.mapWidth = w - sidebarWidth;

	}

	public void setupViewport() {
		worldViewport.apply();
	}

	public void zoomBy(float delta) {
		float newZoom = camera.zoom + delta;
		camera.zoom = MathUtils.clamp(newZoom, MIN_ZOOM, MAX_ZOOM);
		clampCameraForZoom();
		camera.update();
	}

	public void update(float dt) {
		updateZoom(dt);
		panSystem.updatePan(dt);
	}

	public void clampCamera() {
		float halfW = camera.viewportWidth * camera.zoom / 2f;
		float halfH = camera.viewportHeight * camera.zoom / 2f;

		camera.position.x = MathUtils.clamp(
				camera.position.x,
				halfW - CLAMP_PADDING,
				worldW - halfW + CLAMP_PADDING);

		camera.position.y = MathUtils.clamp(
				camera.position.y,
				halfH - CLAMP_PADDING,
				worldH - halfH + CLAMP_PADDING);
	}

	public void setupCamera() {
		camera.position.set(worldW / 2f, worldH / 2f, 0f);
		camera.zoom = MathUtils.clamp(0.8f, MIN_ZOOM, MAX_ZOOM);
		camera.update();

	}

	public void applyWorldViewport() {
		worldViewport.setScreenBounds(0, 0, mapWidth, h);
		worldViewport.apply();
	}

	public void applySidebarViewport(SidebarStage sidebarStage) {
		Viewport uiVp = sidebarStage.getViewport();
		uiVp.setScreenBounds(mapWidth, 0, sidebarWidth, h);
		uiVp.apply();
	}

	public void resize(int width, int height, SidebarStage sidebarStage) {
		this.w = width;
		this.h = height;
		this.sidebarWidth = Math.max(120, (int) (w * 0.18f));
		this.mapWidth = w - sidebarWidth;

		resizeWorld(width, height);
		resizeUI(sidebarStage, width, height);

	}

	public void onScroll(float amountY) {
		targetZoom = MathUtils.clamp(targetZoom + amountY * 0.02f, MIN_ZOOM, MAX_ZOOM); // sensitivity here
	}

	public void updateZoom(float dt) {
		float z = camera.zoom;
		float alpha = 1f - (float) Math.exp(-ZOOM_RESP * dt);
		camera.zoom = MathUtils.lerp(z, targetZoom, alpha);
		clampCameraForZoom();
		camera.update();
	}

	private void clampCameraForZoom() {
		float halfW = camera.viewportWidth * camera.zoom / 2f;
		float halfH = camera.viewportHeight * camera.zoom / 2f;

		if (halfW >= worldW * 0.5f) {
			camera.position.x = worldW * 0.5f;
		} else {
			camera.position.x = MathUtils.clamp(
					camera.position.x,
					halfW - CLAMP_PADDING,
					worldW - halfW + CLAMP_PADDING);
		}

		if (halfH >= worldH * 0.5f) {
			camera.position.y = worldH * 0.5f;
		} else {
			camera.position.y = MathUtils.clamp(
					camera.position.y,
					halfH - CLAMP_PADDING,
					worldH - halfH + CLAMP_PADDING);
		}
	}

	public void resizeWorld(int width, int height) {
		int sidebarWidth = Math.max(120, (int) (width * 0.18f));
		int mapWidth = width - sidebarWidth;
		worldViewport.setScreenBounds(0, 0, mapWidth, height);
		worldViewport.update(mapWidth, height, false);
	}

	public void resizeUI(SidebarStage sidebarStage, int width, int height) {
		int sidebarWidth = Math.max(120, (int) (width * 0.18f));
		int mapWidth = width - sidebarWidth;
		Viewport uiVp = sidebarStage.getViewport();
		uiVp.setScreenBounds(mapWidth, 0, sidebarWidth, height);
		uiVp.update(sidebarWidth, height, true);
		sidebarStage.layoutSidebar();
	}

	public Viewport getWorldViewport() {
		return worldViewport;
	}

	public void setWorldViewport(Viewport worldViewport) {
		this.worldViewport = worldViewport;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getTileW() {
		return tileW;
	}

	public void setTileW(int tileW) {
		this.tileW = tileW;
	}

	public int getTileH() {
		return tileH;
	}

	public void setTileH(int tileH) {
		this.tileH = tileH;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public float getStepX() {
		return stepX;
	}

	public void setStepX(float stepX) {
		this.stepX = stepX;
	}

	public float getStepY() {
		return stepY;
	}

	public void setStepY(float stepY) {
		this.stepY = stepY;
	}

	public float getWorldW() {
		return worldW;
	}

	public void setWorldW(float worldW) {
		this.worldW = worldW;
	}

	public float getWorldH() {
		return worldH;
	}

	public void setWorldH(float worldH) {
		this.worldH = worldH;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getSidebarWidth() {
		return sidebarWidth;
	}

	public void setSidebarWidth(int sidebarWidth) {
		this.sidebarWidth = sidebarWidth;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

}
