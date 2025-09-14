package jokerhut.main.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SidebarStage extends Stage {

    private final Table sidebar;
    private final Texture bgTex;
    private final Skin skin;
    private final Label terrainLabel;
    private final Label axialLabel;

    public SidebarStage(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);

        sidebar = new Table();
        float w = 120f;
        float vh = viewport.getWorldHeight();
        float vw = viewport.getWorldWidth();
        sidebar.setBounds(vw - w, 0, w, vh);

        bgTex = new Texture(Gdx.files.internal("ui/pzUiBorder.png"));
        sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));

        sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));
        sidebar.setTouchable(Touchable.enabled);
        sidebar.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                return true;
            }
        });

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        terrainLabel = new Label("Terrain: ", skin);
        axialLabel = new Label("", skin);

        sidebar.add(terrainLabel).left().pad(5).row();
        sidebar.add(axialLabel).left().pad(5).row();

        Texture scrollPage = new Texture(Gdx.files.internal("ui/verticalNote.png"));
        Image scrollPageImage = new Image(scrollPage);
        sidebar.add(scrollPageImage).size(90, 200).pad(10).row();

        addActor(sidebar);

    }

    public void updateInfo(String terrain, String axial) {
        terrainLabel.setText("Terrain: " + terrain);
        axialLabel.setText("Axial: " + axial);
    }

    public Table getSideBar() {
        return sidebar;
    }

}
