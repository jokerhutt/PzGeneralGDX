package jokerhut.main.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    public Sound leftClickSound = Gdx.audio.newSound(Gdx.files.internal("audio/leftClick.ogg"));
    public Sound rightClickSuccesSound = Gdx.audio.newSound(Gdx.files.internal("audio/rightClickSuccess.ogg"));
    public Sound rightClickInvalidsound = Gdx.audio.newSound(Gdx.files.internal("audio/rightClickFailTwo.ogg"));

    public Sound infantryToInfantryCombat = Gdx.audio.newSound(Gdx.files.internal("audio/INF_INF_COMBAT.ogg"));
    public Sound infantryToTankCombat = Gdx.audio.newSound(Gdx.files.internal("audio/INF_TANK_COMBAT.ogg"));
    public Sound gameTheme = Gdx.audio.newSound(Gdx.files.internal("audio/gameTheme.ogg"));

}
