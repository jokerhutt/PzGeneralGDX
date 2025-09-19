package jokerhut.main.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.model.player.Player;

public class UnitRenderer implements Renderer {

    private final Player axisPlayer;
    private final Player alliedPlayer;
    private final SpriteBatch batch;

    public UnitRenderer(SpriteBatch batch, Player axisPlayer, Player alliedPlayer) {
        this.batch = batch;
        this.axisPlayer = axisPlayer;
        this.alliedPlayer = alliedPlayer;

    }

    public void render() {

        for (AbstractUnit unit : axisPlayer.getUnits()) {
            unit.render(batch);
        }
        for (AbstractUnit unit : alliedPlayer.getUnits()) {
            unit.render(batch);
        }

    }

}
