package jokerhut.main.stage.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import jokerhut.main.entities.AbstractUnit;

public class UnitStatsGroup extends VerticalGroup {

    private final StatsGroupLabel defenceRow;
    private final StatsGroupLabel healthRow;
    private final StatsGroupLabel softRow;
    private final StatsGroupLabel hardRow;
    private final StatsGroupLabel movementLeftRow;

    private final Skin skin;

    public UnitStatsGroup() {

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        defenceRow = new StatsGroupLabel(skin, "icons/icon_defence.png");
        healthRow = new StatsGroupLabel(skin, "icons/icon_health.png");
        softRow = new StatsGroupLabel(skin, "icons/icon_softAttack.png");
        hardRow = new StatsGroupLabel(skin, "icons/icon_hardAttack.png");
        movementLeftRow = new StatsGroupLabel(skin, "icons/icon_movePoints.png");

        this.space(5f);
        this.addActor(movementLeftRow);
        this.addActor(defenceRow);
        this.addActor(healthRow);
        this.addActor(softRow);
        this.addActor(hardRow);

    }

    public void updateInfo(AbstractUnit unit) {
        if (unit == null) {
            defenceRow.setValue("---");
            healthRow.setValue("---");
            softRow.setValue("---");
            hardRow.setValue("---");
            movementLeftRow.setValue("- / -");
        } else {
            defenceRow.setValue(String.valueOf(unit.getDefense()));
            healthRow.setValue(String.valueOf(unit.getHealth()));
            softRow.setValue(String.valueOf(unit.getSoftAttack()));
            hardRow.setValue(String.valueOf(unit.getHardAttack()));
            movementLeftRow.setValue(String.valueOf(unit.getMovementPoints() + "/" + unit.getMovementPoints()));
        }
    }

}
