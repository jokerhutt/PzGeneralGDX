package jokerhut.main.systems.effect;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import jokerhut.main.model.unit.AbstractUnit;

public class EffectSystem {

    private final Array<EffectSequence> active = new Array<>();
    private final Pool<EffectSequence> pool = new Pool<>() {
        protected EffectSequence newObject() {
            return new EffectSequence();
        }
    };

    private EffectHandler effectHandler = new EffectHandler();

    public void spawn(Animation<TextureRegion> anim, Vector2 at, Vector2 offset) {
        active.add(pool.obtain().set(anim, at, false, -1, offset));
    }

    public EffectSequence spawnLoop(Animation<TextureRegion> anim, Vector2 at, Vector2 offset) {
        EffectSequence e = pool.obtain().set(anim, at, true, -1, offset);
        active.add(e);
        return e;
    }

    public void spawnAnchored(Animation<TextureRegion> anim, AbstractUnit u, Vector2 offset) {
        active.add(pool.obtain().setAnchored(anim, u, false, -1, offset));
    }

    public EffectSequence spawnAnchoredLoop(Animation<TextureRegion> anim, AbstractUnit u, Vector2 offset) {
        EffectSequence e = pool.obtain().setAnchored(anim, u, true, -1, offset);
        active.add(e);
        return e;
    }

    public void spawnAnchoredTimed(Animation<TextureRegion> anim, AbstractUnit u, float seconds, Vector2 offset) {
        active.add(pool.obtain().setAnchored(anim, u, true, seconds, offset));
    }

    public void update(float dt) {
        for (int i = active.size - 1; i >= 0; i--)
            if (!active.get(i).update(dt))
                pool.free(active.removeIndex(i));
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < active.size; i++)
            active.get(i).render(batch);
    }

    public void stop(EffectSequence e) {
        if (e == null)
            return;
        int idx = active.indexOf(e, true);
        if (idx >= 0)
            pool.free(active.removeIndex(idx));
    }

    public Array<EffectSequence> getActive() {
        return active;
    }

    public Pool<EffectSequence> getPool() {
        return pool;
    }

    public EffectHandler getAnimationHandler() {
        return effectHandler;
    }

    public void setAnimationHandler(EffectHandler effectHandler) {
        this.effectHandler = effectHandler;
    }

}
