package jokerhut.main.renderer;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.player.Player;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.systems.movement.MovementSystem;
import jokerhut.main.systems.selection.SelectionController;

public class GameRenderer implements Renderer {

	private final OrthographicCamera camera;
	private final ShapeRenderer shapeRenderer;
	private final SpriteBatch batch;
	private final MovementOverlayRenderer movementOverlayRenderer;
	private final OutlineRenderer outlineRenderer;
	private final UnitRenderer unitRenderer;
	private final HexagonalTiledMapRenderer hexmapRenderer;
	private final MovementSystem movementSystem;

	public GameRenderer(HexagonalTiledMapRenderer hexmapRenderer, OrthographicCamera camera,
			ShapeRenderer shapeRenderer, SpriteBatch batch,
			HashMap<Axial, Hex> hexMap, BattleField battleField, SelectionController selectionController,
			Player axisPlayer,
			Player alliedPlayer, MovementSystem movementSystem) {
		this.camera = camera;
		this.shapeRenderer = shapeRenderer;
		this.batch = batch;
		this.hexmapRenderer = hexmapRenderer;

		this.movementOverlayRenderer = new MovementOverlayRenderer(shapeRenderer, selectionController, hexMap,
				battleField,
				movementSystem);
		this.outlineRenderer = new OutlineRenderer(shapeRenderer, hexMap);
		this.unitRenderer = new UnitRenderer(batch, axisPlayer, alliedPlayer);
		this.movementSystem = movementSystem;
	}

	public void render() {

		hexmapRenderer.setView(camera);
		hexmapRenderer.render();

		// movement overlay
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		movementOverlayRenderer.render();
		shapeRenderer.end();

		// outlines
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		outlineRenderer.render();
		shapeRenderer.end();

		//// sprites
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		unitRenderer.render();

		batch.end();

	}
}
