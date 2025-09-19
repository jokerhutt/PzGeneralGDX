package jokerhut.main.systems.effect;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.model.unit.AbstractUnit;

public class EffectSequence {

    private Vector2 pos = new Vector2();
    private AbstractUnit anchorPosition;
    private Animation<TextureRegion> animation;
    private float timeElapsed = 0f;
    private boolean loop = false;
    private float maxDuration = -1f;
    private Vector2 offset = new Vector2();

    public EffectSequence set(Animation<TextureRegion> a, Vector2 at, boolean loop, float maxDuration, Vector2 offset) {
        animation = a;
        pos.set(at).add(offset);
        anchorPosition = null;
        timeElapsed = 0;
        this.loop = loop;
        this.offset.set(offset);
        this.maxDuration = maxDuration;
        return this;
    }

    public EffectSequence setAnchored(Animation<TextureRegion> animation, AbstractUnit unit, boolean loop, float maxDuration,
                                      Vector2 offset) {
        this.animation = animation;
        anchorPosition = unit;
        timeElapsed = 0;
        this.loop = loop;
        this.offset.set(offset);
        this.maxDuration = maxDuration;
        return this;
    }

    public boolean update(float dt) {
        timeElapsed += dt;

        if (anchorPosition != null) {
            pos.set(anchorPosition.getRenderPosPx()).add(offset);
        }

        if (maxDuration > 0f && timeElapsed >= maxDuration)
            return false;

        if (!loop)
            return !animation.isAnimationFinished(timeElapsed);

        return true;
    }

    public void render(SpriteBatch b) {
        TextureRegion f = animation.getKeyFrame(timeElapsed, loop);
        b.draw(f, pos.x - f.getRegionWidth() / 2f, pos.y - f.getRegionHeight() / 2f);
    }

    public AbstractUnit getAnchorPosition() {
        return anchorPosition;
    }

    public void setAnchorPosition(AbstractUnit anchorPosition) {
        this.anchorPosition = anchorPosition;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public float getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(float timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 p) {
        this.pos.set(p);
    } // do not assign the reference

}
