package jokerhut.main.stage.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import jokerhut.main.DTs.Axial;

public class HexInfoGroup extends VerticalGroup {

    private final Label terrainLabel;
    private final Label axialLabel;
    private final Skin skin;

    public HexInfoGroup() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        terrainLabel = new Label("Terrain: --", skin);
        axialLabel = new Label("( , )", skin);

        terrainLabel.setFontScale(0.7f);
        axialLabel.setFontScale(0.7f);

        this.space(5f);
        this.addActor(terrainLabel);
        this.addActor(axialLabel);

    }

    public void updateInfo(String terrain, Axial axial) {
        terrainLabel.setText("Terrain: " + terrain);
        String formattedAxial = "(" + axial.q() + ", " + axial.r() + ")";
        axialLabel.setText(formattedAxial);
    }

}
