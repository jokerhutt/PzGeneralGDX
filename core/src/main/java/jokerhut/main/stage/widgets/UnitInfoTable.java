package jokerhut.main.stage.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import jokerhut.main.entities.AbstractUnit;

public class UnitInfoTable extends Table {

    private final Texture bgTex;
    private UnitInfoGroup unitInfoGroup;
    private UnitStatsGroup unitStatsGroup;

    public UnitInfoTable() {

        float w = 100f;
        bgTex = new Texture(Gdx.files.internal("ui/verticalNote.png"));
        this.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));
        unitInfoGroup = new UnitInfoGroup();
        this.add(unitInfoGroup).expand().top().pad(10).row();

        unitStatsGroup = new UnitStatsGroup();
        this.add(unitStatsGroup).expand().top().pad(10).row();

    }

    public void update(AbstractUnit unit) {

        unitInfoGroup.updateInfo(unit);
        unitStatsGroup.updateInfo(unit);

    }

}
