package jokerhut.main.UI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.enums.Faction;

public class UnitInfoTable extends Table {

    private final Texture bgTex;
    private UnitInfoGroup unitInfoGroup;
    private UnitStatsGroup unitStatsGroup;
    private Image factionImage;
    private final TextureRegionDrawable axisImage;
    private final TextureRegionDrawable alliesImage;

    public UnitInfoTable() {

        float w = 100f;

        axisImage = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("icons/flag_DE.png"))));
        alliesImage = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("icons/flag_UK.png"))));

        bgTex = new Texture(Gdx.files.internal("ui/verticalNote.png"));
        this.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));

        factionImage = new Image();
        factionImage.setScaling(Scaling.fit);
        factionImage.setSize(16, 16);
        factionImage.setDrawable(null);
        this.add(factionImage).size(16, 16).row();

        unitInfoGroup = new UnitInfoGroup();
        this.add(unitInfoGroup).expand().top().pad(10).row();

        unitStatsGroup = new UnitStatsGroup();
        this.add(unitStatsGroup).expand().top().pad(10).row();

    }

    public void resizeFonts(float s) {
        unitInfoGroup.setFontScale(s);
        unitStatsGroup.setFontScale(s);
        getCell(factionImage).size(16 * s, 16 * s);
    }

    public void update(AbstractUnit unit) {

        if (unit == null) {
            factionImage.setDrawable(null);
        } else if (unit.getFaction() == Faction.GERMAN) {
            factionImage.setDrawable(axisImage);
        } else {
            factionImage.setDrawable(alliesImage);
        }

        unitInfoGroup.updateInfo(unit);
        unitStatsGroup.updateInfo(unit);

    }

}
