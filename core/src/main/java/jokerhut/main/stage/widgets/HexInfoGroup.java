package jokerhut.main.stage.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import jokerhut.main.DTs.Axial;
import jokerhut.main.enums.Faction;
import jokerhut.main.screen.PlayerState;
import jokerhut.main.screen.TurnManager;

public class HexInfoGroup extends VerticalGroup {

    private final Label terrainLabel;
    private final Label axialLabel;
    private final Skin skin;
    private final Label turnCount;
    private final Label currentFaction;

    public HexInfoGroup(TurnManager turnManagerContext) {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        terrainLabel = new Label("Terrain: --", skin);
        axialLabel = new Label("( , )", skin);
        turnCount = new Label(turnManagerContext.getTurnNumber() + "/100", skin);
        currentFaction = new Label("", skin);
        if (turnManagerContext.getCurrentPlayer().getFaction() == Faction.BRITISH) {
            currentFaction.setText("UKF");
        } else {
            currentFaction.setText("AXIS");
        }

        terrainLabel.setFontScale(0.7f);
        axialLabel.setFontScale(0.7f);
        turnCount.setFontScale(0.7f);
        currentFaction.setFontScale(0.7f);

        this.space(5f);
        this.addActor(terrainLabel);
        this.addActor(axialLabel);
        this.addActor(turnCount);
        this.addActor(currentFaction);

    }

    public void updateInfo(String terrain, Axial axial) {
        if (terrain == null || axial == null)
            return;
        terrainLabel.setText("Terrain: " + terrain);
        String formattedAxial = "(" + axial.q() + ", " + axial.r() + ")";
        axialLabel.setText(formattedAxial);

    }

    public void updateTurnState(TurnManager turnManager) {

        PlayerState currentPlayer = turnManager.getCurrentPlayer();
        if (currentPlayer.getFaction() == Faction.BRITISH) {
            currentFaction.setText("UKF");
        } else {
            currentFaction.setText("AXIS");
        }
        turnCount.setText(turnManager.getTurnNumber() + "/" + "100");

    }

    public void setFontScale(float s) {
        terrainLabel.setFontScale(0.7f * s);
        axialLabel.setFontScale(0.7f * s);
        turnCount.setFontScale(0.7f * s);
        currentFaction.setFontScale(0.7f * s);
    }

}
