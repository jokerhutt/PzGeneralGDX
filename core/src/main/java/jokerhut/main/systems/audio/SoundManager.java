package jokerhut.main.systems.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import jokerhut.main.model.enums.UnitType;

public class SoundManager {

    private final MovementSoundManager movementSoundManager = new MovementSoundManager();

    public void playMovement(UnitType unitType) {
        movementSoundManager.playMovementSound(unitType);
    }

    public Sound leftClickSound = Gdx.audio.newSound(Gdx.files.internal("audio/leftClick.ogg"));
    public Sound rightClickSuccesSound = Gdx.audio.newSound(Gdx.files.internal("audio/rightClickSuccess.ogg"));
    public Sound rightClickInvalidsound = Gdx.audio.newSound(Gdx.files.internal("audio/rightClickFailTwo.ogg"));

    public Sound infantryToInfantryCombat = Gdx.audio.newSound(Gdx.files.internal("audio/INF_INF_COMBAT.ogg"));
    public Sound infantryToTankCombat = Gdx.audio.newSound(Gdx.files.internal("audio/armour_attack_0.wav"));
    public Sound gameTheme = Gdx.audio.newSound(Gdx.files.internal("audio/gameTheme.ogg"));

}
