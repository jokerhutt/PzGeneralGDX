package jokerhut.main.systems.movement;

import java.util.ArrayDeque;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.model.hex.Axial;
import jokerhut.main.model.unit.AbstractUnit;
import jokerhut.main.systems.battlefield.BattleField;
import jokerhut.main.utils.HexUtils;

public final class MovementSequence {
    final AbstractUnit unit;
    final ArrayDeque<Axial> remainingTiles;
    Axial fromAx;
    Axial toAx;
    final Vector2 fromPx = new Vector2();
    final Vector2 toPx = new Vector2();
    float timeOnCurrentHex = 0f;
    float durationToNextHex = 0f;
    final float speed;

    MovementSequence(AbstractUnit unit, List<Axial> path, float pxPerSec) {
        this.unit = unit;
        this.remainingTiles = new ArrayDeque<>(path);
        this.speed = pxPerSec;
        this.fromAx = unit.getPosition();
        advanceToNextEdge();
        unit.setRenderPosPx(fromPx);
    }

    public float tick(float dt) {
        if (toAx == null)
            return dt;
        float remain = durationToNextHex - timeOnCurrentHex;
        float use = Math.min(dt, remain);
        timeOnCurrentHex += use;

        float a = (durationToNextHex <= 0f) ? 1f : timeOnCurrentHex / durationToNextHex;
        float x = MathUtils.lerp(fromPx.x, toPx.x, a);
        float y = MathUtils.lerp(fromPx.y, toPx.y, a);
        unit.setRenderPosPx(new Vector2(x, y));

        if (timeOnCurrentHex >= durationToNextHex) {
            unit.setRenderPosPx(toPx);
            return dt - use;
        }
        return -1f;
    }

    public boolean finishEdgeAndMaybeAdvance(BattleField bf) {
        bf.commitMove(unit, toAx);
        return advanceToNextEdge();
    }

    private boolean advanceToNextEdge() {
        if (remainingTiles.isEmpty()) {
            toAx = null;
            durationToNextHex = 0f;
            timeOnCurrentHex = 0f;
            unit.setRenderPosPx(HexUtils.axialToPixelCenter(unit.getPosition()));
            return false;
        }
        fromAx = unit.getPosition();
        toAx = remainingTiles.removeFirst();
        fromPx.set(HexUtils.axialToPixelCenter(fromAx));
        toPx.set(HexUtils.axialToPixelCenter(toAx));
        float dist = fromPx.dst(toPx);
        durationToNextHex = dist / Math.max(1e-6f, speed);
        timeOnCurrentHex = 0f;
        return true;
    }

    public boolean done() {
        return toAx == null;
    }
}
