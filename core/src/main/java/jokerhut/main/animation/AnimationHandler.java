package jokerhut.main.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationHandler {

    private final Animation<TextureRegion> infantryActionAnimation;

    public AnimationHandler() {
        Texture infantryActionSheet = new Texture(
                Gdx.files.internal("animations/infantryMuzzleSheet.png"));

        TextureRegion[][] infantryActionSlicedSheet = TextureRegion.split(infantryActionSheet, 148, 148);

        TextureRegion[] infantryActionSlice = infantryActionSlicedSheet[3];

        infantryActionAnimation = new Animation<>(0.2f, infantryActionSlice);
    }

    public Animation<TextureRegion> getInfantryActionAnimation() {
        return infantryActionAnimation;
    }

}
