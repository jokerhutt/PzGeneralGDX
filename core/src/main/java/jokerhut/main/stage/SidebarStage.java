package jokerhut.main.stage;

import com.badlogic.gdx.Gdx;
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

import jokerhut.main.DTs.Axial;
import jokerhut.main.DTs.Selection;
import jokerhut.main.stage.widgets.HexInfoGroup;
import jokerhut.main.stage.widgets.UnitInfoTable;

public class SidebarStage extends Stage {

    private final Table sidebar;
    private final Texture bgTex;
    private HexInfoGroup hexInfoGroup;

    private UnitInfoTable unitInfoTable;

    public SidebarStage(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);

        sidebar = new Table();
        float w = 120f;
        float vh = viewport.getWorldHeight();
        float vw = viewport.getWorldWidth();
        sidebar.setBounds(vw - w, 0, w, vh);

        bgTex = new Texture(Gdx.files.internal("ui/pzUiBorder.png"));
        sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));
        sidebar.setTouchable(Touchable.enabled);
        sidebar.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
                return true;
            }
        });

        hexInfoGroup = new HexInfoGroup();
        sidebar.add(hexInfoGroup).expand().top().pad(10).row();

        unitInfoTable = new UnitInfoTable();
        sidebar.add(unitInfoTable).size(90, 200).pad(10).row();

        addActor(sidebar);

    }

    public void updateState(Selection selection) {

        String terrain = selection.hex().getTerrain();
        Axial axial = selection.axial();

        hexInfoGroup.updateInfo(terrain, axial);
        unitInfoTable.update(selection.unit());

    }

    public Table getSideBar() {
        return sidebar;
    }

}
