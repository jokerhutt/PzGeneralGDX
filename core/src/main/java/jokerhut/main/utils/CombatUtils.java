package jokerhut.main.utils;

public class CombatUtils {

	final float MITIG = 0.6f; // how much DEF reduces incoming fire
	final float K = 1.0f; // global damage scale
	final float MIN_DMG = 0.5f; // chip damage floor
	final float ORG_DMG = 0.7f; // org lost per HP taken
	final float ORG_FIRE = 0.1f; // org lost per shot fired (small fatigue)
	final float ORG_EXP = 0.8f; // attack scales with org^EXP (softens extremes)

	float effAtk(float softAttack, float org, float maxOrg) {
		float orgFactor = (float) Math.pow(Math.max(0f, org) / Math.max(1f, maxOrg), ORG_EXP);
		return softAttack * orgFactor;
	}

	float dmg(float atkSoft, float defDef, float defOrg, float defMaxOrg) {
		float orgShield = 0.15f * (defOrg / Math.max(1f, defMaxOrg));
		float mitig = MITIG * (1f + orgShield); // 0.6..~0.69
		float raw = K * (atkSoft - mitig * defDef);
		return Math.max(MIN_DMG, raw);
	}

}
