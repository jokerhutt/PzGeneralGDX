package jokerhut.main.renderer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.screen.BattleField;
import jokerhut.main.selection.MovementOverlay;
import jokerhut.main.selection.MovementSystem;
import jokerhut.main.selection.SelectionState;
import jokerhut.main.utils.HexUtils;

public class MovementOverlayRenderer implements Renderer {

    private final ShapeRenderer shapeRenderer;
    private final SelectionState selectionState;
    private final HashMap<Axial, Hex> hexMap;
    private final MovementSystem movementSystem;
    private final BattleField battleField;

    public MovementOverlayRenderer(ShapeRenderer shapeRenderer, SelectionState selectionState,
            HashMap<Axial, Hex> hexMap, BattleField battleField, MovementSystem movementSystem) {

        this.shapeRenderer = shapeRenderer;
        this.selectionState = selectionState;
        this.hexMap = hexMap;
        this.battleField = battleField;
        this.movementSystem = movementSystem;

    }

    public void render() {

        Selection currentSelection = selectionState.getCurrentSelection();

        if (currentSelection == null)
            return;

        shapeRenderer.setColor(Color.BLUE);

        Vector2 currentPosition = HexUtils.axialToPixelCenter(currentSelection.axial());

        Color hexFillColor = new Color(Color.BLUE);
        hexFillColor.a = 0.5f;

        shapeRenderer.setColor(hexFillColor);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        if (currentSelection.unit() == null || !movementSystem.isUnitMoving(currentSelection.unit())) {
            HexUtils.fillHex(shapeRenderer, currentPosition, GameConstants.HEX_SIZE, GameConstants.HEX_Y_SCALE);
        }

        MovementOverlay movementOverlay = selectionState.getMovementOverlay();

        if (movementOverlay != null && currentSelection.unit() != null) {

            HashMap<Axial, Integer> reachableCosts = movementOverlay.reachableCosts();
            HashMap<Axial, Integer> attackable = movementOverlay.attackableCosts();

            for (Map.Entry<Axial, Hex> entry : hexMap.entrySet()) {

                Hex currentHex = entry.getValue();
                if (currentHex == null)
                    continue;
                Axial currentAxial = entry.getKey();
                Vector2 currentPixelPosition = HexUtils.axialToPixelCenter(currentHex);

                if (currentHex.getQ() == currentSelection.axial().q()
                        && currentHex.getR() == currentSelection.axial().r()) {
                    continue;
                } else if (attackable.containsKey(currentAxial)) {
                    hexFillColor.set(Color.RED);
                    hexFillColor.a = 0.5f;
                    shapeRenderer.setColor(hexFillColor);
                    HexUtils.fillHex(shapeRenderer, currentPixelPosition, GameConstants.HEX_SIZE,
                            GameConstants.HEX_Y_SCALE);
                } else if (reachableCosts.containsKey(currentAxial)) {
                    hexFillColor.set(Color.LIGHT_GRAY);
                    hexFillColor.a = 0.5f;
                    shapeRenderer.setColor(hexFillColor);
                    HexUtils.fillHex(shapeRenderer, currentPixelPosition, GameConstants.HEX_SIZE,
                            GameConstants.HEX_Y_SCALE);
                } else {
                    hexFillColor.set(Color.GRAY);
                    hexFillColor.a = 0.5f;
                    shapeRenderer.setColor(hexFillColor);
                    HexUtils.fillHex(shapeRenderer, currentPixelPosition, GameConstants.HEX_SIZE,
                            GameConstants.HEX_Y_SCALE);
                }

            }

        }

    }

}
