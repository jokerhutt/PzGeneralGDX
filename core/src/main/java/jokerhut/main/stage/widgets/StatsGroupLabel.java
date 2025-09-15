package jokerhut.main.stage.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class StatsGroupLabel extends Table {

    private final Image icon;
    private final Label label;

    public StatsGroupLabel(Skin skin, String iconPath) {
        icon = new Image(new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal(iconPath)))));
        label = new Label("----", skin);
        label.setColor(Color.BLACK);
        label.setFontScale(0.4f);

        this.add(icon).size(10, 10).padRight(5);
        this.add(label).left();
    }

    public void setValue(String value) {
        label.setText(value);
    }

}
