package jokerhut.main.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import jokerhut.main.DTs.Hex;
import jokerhut.main.DTs.Selection;
import jokerhut.main.screen.TurnManager;
import jokerhut.main.selection.SelectionState;
import jokerhut.main.stage.widgets.HexInfoGroup;
import jokerhut.main.stage.widgets.UnitInfoTable;

public class SidebarStage extends Stage {

    private final Table sidebar;
    private final Texture bgTex;
    private HexInfoGroup hexInfoGroup;
    private TurnManager turnManagerContext;
    private UnitInfoTable unitInfoTable;
    private SelectionState selectionState;
    private final float baseSidebarPx = 120f;
    private final float sidebarFrac = 0.18f;
    private float fontScale = 0.4f;

    public SidebarStage(Viewport viewport, SpriteBatch batch, TurnManager turnManagerContext,
            SelectionState selectionState) {
        super(viewport, batch);

        sidebar = new Table();
        bgTex = new Texture(Gdx.files.internal("ui/pzUiBorder.png"));
        sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(bgTex)));
        sidebar.setTouchable(Touchable.enabled);
        sidebar.defaults().expandX().fillX().pad(10);
        addActor(sidebar);

        hexInfoGroup = new HexInfoGroup(turnManagerContext);
        sidebar.add(hexInfoGroup).top().pad(10).row();

        unitInfoTable = new UnitInfoTable();
        sidebar.add(unitInfoTable).top().pad(20).growY().row();

        this.turnManagerContext = turnManagerContext;
        this.selectionState = selectionState;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton endTurnButton = new TextButton("end turn", skin);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                System.out.println("clicked");
                turnManagerContext.endTurn();
                selectionState.clear();
                updateState(null);
                hexInfoGroup.updateTurnState(turnManagerContext);
            }
        });

        sidebar.add(endTurnButton).height(32).pad(8).padBottom(40).row();

        layoutSidebar();

    }

    private void rescaleFonts(float s) {
        hexInfoGroup.setFontScale(s);
        unitInfoTable.resizeFonts(s);
    }

    public void updateState(Selection selection) {

        if (selection == null) {
            hexInfoGroup.updateInfo(null, null);
            unitInfoTable.update(null);
            return;
        }

        Hex h = selection.hex();
        if (h == null)
            h = selectionState.getGameMapContext().get(selection.axial());
        if (h == null) { // off-map or cleared
            hexInfoGroup.updateInfo(null, selection.axial());
            unitInfoTable.update(selection.unit());
            return;
        }
        hexInfoGroup.updateInfo(h.getTerrain(), selection.axial());
        unitInfoTable.update(selection.unit());

    }

    public void layoutSidebar() {
        float vw = getViewport().getWorldWidth();
        float w = Math.max(120f, vw * 0.18f);
        sidebar.setBounds(vw - w, 0, w, getViewport().getWorldHeight());
        float s = w / baseSidebarPx;
        if (Math.abs(s - fontScale) > 0.01f) {
            fontScale = s;
            rescaleFonts(fontScale);
        }
        sidebar.invalidateHierarchy();
    }

    public Table getSideBar() {
        return sidebar;
    }

}
