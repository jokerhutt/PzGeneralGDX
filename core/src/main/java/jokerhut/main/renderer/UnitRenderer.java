package jokerhut.main.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import jokerhut.main.entities.AbstractUnit;
import jokerhut.main.screen.PlayerState;

public class UnitRenderer implements Renderer {

    private final PlayerState axisPlayer;
    private final PlayerState alliedPlayer;
    private final SpriteBatch batch;

    public UnitRenderer(SpriteBatch batch, PlayerState axisPlayer, PlayerState alliedPlayer) {
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
