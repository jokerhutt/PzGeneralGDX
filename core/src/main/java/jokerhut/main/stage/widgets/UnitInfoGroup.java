package jokerhut.main.stage.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import jokerhut.main.entities.AbstractUnit;

public class UnitInfoGroup extends VerticalGroup {

    private final Label unitName;
    private final Label unitType;
    private final Skin skin;

    public UnitInfoGroup() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        unitName = new Label("----", skin);
        unitType = new Label("----", skin);

        unitName.setFontScale(0.7f);
        unitType.setFontScale(0.7f);

        this.space(5f);
        this.addActor(unitName);
        this.addActor(unitType);

    }

    public void updateInfo(AbstractUnit unit) {
        if (unit == null || unit.getName() == null) {
            unitName.setText("---");
            unitType.setText("---");
        } else {
            unitName.setText(unit.getName());
            unitType.setText(unit.getName());
        }

    }

}
