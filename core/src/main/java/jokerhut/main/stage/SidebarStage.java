package jokerhut.main.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SidebarStage extends Stage {

    private final Table sidebar;
    private final Texture bgTex;

    public SidebarStage(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);

        sidebar = new Table();
        float w = 100f;
        float vh = viewport.getWorldHeight();
        float vw = viewport.getWorldWidth();
        sidebar.setBounds(vw - w, 0, w, vh);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.DARK_GRAY);
        pm.fill();
        bgTex = new Texture(pm);
        pm.dispose();

        sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));
        sidebar.setTouchable(Touchable.enabled);
        sidebar.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                return true;
            }
        });

        addActor(sidebar);
    }

    public Table getSideBar() {
        return sidebar;
    }

}
