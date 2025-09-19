package jokerhut.main.UI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import jokerhut.main.UI.label.StatsGroupLabel;
import jokerhut.main.model.unit.AbstractUnit;

public class UnitStatsGroup extends VerticalGroup {

	private final StatsGroupLabel defenceRow;
	private final StatsGroupLabel healthRow;
	private final StatsGroupLabel softRow;
	private final StatsGroupLabel hardRow;
	private final StatsGroupLabel movementLeftRow;

	private final StatsGroupLabel fuelRow;

	private final StatsGroupLabel organizationRow;
	private final Skin skin;

	public UnitStatsGroup() {

		skin = new Skin(Gdx.files.internal("uiskin.json"));

		defenceRow = new StatsGroupLabel(skin, "icons/icon_defence.png");
		healthRow = new StatsGroupLabel(skin, "icons/icon_health.png");
		softRow = new StatsGroupLabel(skin, "icons/icon_softAttack.png");
		hardRow = new StatsGroupLabel(skin, "icons/icon_hardAttack.png");
		movementLeftRow = new StatsGroupLabel(skin, "icons/icon_movePoints.png");
		fuelRow = new StatsGroupLabel(skin, "icons/icon_fuel.png");
		organizationRow = new StatsGroupLabel(skin, "icons/icon_targettype.png");

		this.space(5f);
		this.addActor(movementLeftRow);
		this.addActor(defenceRow);
		this.addActor(healthRow);
		this.addActor(softRow);
		this.addActor(hardRow);
		this.addActor(fuelRow);
		this.addActor(organizationRow);

	}

	public void updateInfo(AbstractUnit unit) {
		if (unit == null) {
			defenceRow.setValue("---");
			healthRow.setValue("---");
			softRow.setValue("---");
			hardRow.setValue("---");
			movementLeftRow.setValue("- / -");
			fuelRow.setValue("- / -");
			organizationRow.setValue("- / -");
		} else {
			defenceRow.setValue(String.format("%.1f", unit.getDefense()));
			healthRow.setValue(String.format("%.1f", unit.getHealth()));
			softRow.setValue(String.format("%.1f", unit.getSoftAttack()));
			hardRow.setValue(String.format("%.1f", unit.getHardAttack()));

			movementLeftRow.setValue(
					unit.getMovementPoints() + "/" + unit.getStartingMovementPoints());

			fuelRow.setValue(String.format("%.1f/%.1f",
					unit.getFuelCount(), unit.getMaxFuelCount()));

			organizationRow.setValue(String.format("%.1f/%.1f",
					unit.getOrganization(), unit.getMaxOrganization()));
		}
	}

	public void setFontScale(float s) {
		defenceRow.setFontScale(s);
		healthRow.setFontScale(s);
		softRow.setFontScale(s);
		hardRow.setFontScale(s);
		movementLeftRow.setFontScale(s);
		fuelRow.setFontScale(s);
		organizationRow.setFontScale(s);

	}

}
