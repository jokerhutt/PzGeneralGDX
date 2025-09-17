package jokerhut.main.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import jokerhut.main.enums.UnitType;

public class MovementSoundManager {

    private SoundManager soundManagerContext;

    private Sound moveWalkingSound = Gdx.audio.newSound(Gdx.files.internal("audio/leg.wav"));
    private Sound moveLightArmorSound = Gdx.audio.newSound(Gdx.files.internal("audio/tank_light.wav"));

    public MovementSoundManager() {
    }

    public void playMovementSound(UnitType unitType) {

        switch (unitType) {

            case INFANTRY -> moveWalkingSound.play();
            case LIGHT_ARMOR -> moveLightArmorSound.play();

        }

    }

}
