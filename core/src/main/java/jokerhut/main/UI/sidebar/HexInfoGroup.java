package jokerhut.main.UI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import jokerhut.main.model.hex.Hex;
import jokerhut.main.model.enums.Faction;
import jokerhut.main.model.player.Player;
import jokerhut.main.systems.turn.TurnManager;

public class HexInfoGroup extends VerticalGroup {

    private final Label terrainLabel;
    private final Label axialLabel;
    private final Skin skin;
    private final Label turnCount;
    private final Label currentFaction;
    private final Label hexTerrainType;

    public HexInfoGroup(TurnManager turnManagerContext) {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        terrainLabel = new Label("Terrain: --", skin);
        axialLabel = new Label("( , )", skin);
        turnCount = new Label(turnManagerContext.getTurnNumber() + "/100", skin);
        currentFaction = new Label("", skin);
        hexTerrainType = new Label("", skin);
        if (turnManagerContext.getCurrentPlayer().getFaction() == Faction.BRITISH) {
            currentFaction.setText("UKF");
        } else {
            currentFaction.setText("AXIS");
        }

        terrainLabel.setFontScale(0.7f);
        axialLabel.setFontScale(0.7f);
        turnCount.setFontScale(0.7f);
        currentFaction.setFontScale(0.7f);
        hexTerrainType.setFontScale(0.7f);

        this.space(5f);
        this.addActor(terrainLabel);
        this.addActor(hexTerrainType);
        this.addActor(axialLabel);
        this.addActor(turnCount);
        this.addActor(currentFaction);

    }

    public void updateInfo(Hex hex) {
        if (hex == null) {

            terrainLabel.setText("Terrain: --");
            axialLabel.setText("( , )");
            hexTerrainType.setText("");
            return;

        }
        terrainLabel.setText("Terrain: " + hex.getTerrain());
        String formattedAxial = "(" + hex.getQ() + ", " + hex.getR() + ")";
        axialLabel.setText(formattedAxial);

        if (hex.getTerrainProfile() != null && hex.getTerrainProfile().getTerrainType() != "DEFAULT") {
            hexTerrainType.setText(hex.getTerrainProfile().getTerrainType());
        } else {
            hexTerrainType.setText("");
        }

    }

    public void updateTurnState(TurnManager turnManager) {

        Player currentPlayer = turnManager.getCurrentPlayer();
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
        hexTerrainType.setFontScale(0.7f * s);
    }

}
