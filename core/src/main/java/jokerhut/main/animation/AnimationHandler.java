package jokerhut.main.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationHandler {

    private final Animation<TextureRegion> infantryBoltActionAnimation;
    private final Animation<TextureRegion> infantryBoltActionAnimationTwo;
    private final Animation<TextureRegion> infantrySmgAnimation;

    public AnimationHandler() {
        Texture infantryActionSheet = new Texture(
                Gdx.files.internal("animations/infantryMuzzleSheet.png"));

        TextureRegion[][] infantryActionSlicedSheet = TextureRegion.split(infantryActionSheet, 148, 148);

        TextureRegion[] infantrySmg = infantryActionSlicedSheet[2];
        TextureRegion[] infantryBoltAction = infantryActionSlicedSheet[0];
        TextureRegion[] infantryBoltActionTwo = infantryActionSlicedSheet[3];

        infantryBoltActionAnimation = new Animation<>(0.2f, infantryBoltAction);
        infantrySmgAnimation = new Animation<>(0.2f, infantrySmg);
        infantryBoltActionAnimationTwo = new Animation<>(0.2f, infantryBoltActionTwo);
    }

    public Animation<TextureRegion> getInfantryBoltActionAnimation() {
        return infantryBoltActionAnimation;
    }

    public Animation<TextureRegion> getInfantrySmgAnimation() {
        return infantrySmgAnimation;
    }

    public Animation<TextureRegion> getInfantryBoltActionAnimationTwo() {
        return infantryBoltActionAnimationTwo;
    }

}
