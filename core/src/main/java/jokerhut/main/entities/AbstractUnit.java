package jokerhut.main.entities;

public abstract class AbstractUnit {

    private float health;
    private float defense;
    private float softAttack;
    private float hardAttack;


    public float getHealth() {
        return health;
    }
    public void setHealth(float health) {
        this.health = health;
    }
    public float getDefense() {
        return defense;
    }
    public void setDefense(float defense) {
        this.defense = defense;
    }
    public float getSoftAttack() {
        return softAttack;
    }
    public void setSoftAttack(float softAttack) {
        this.softAttack = softAttack;
    }
    public float getHardAttack() {
        return hardAttack;
    }
    public void setHardAttack(float hardAttack) {
        this.hardAttack = hardAttack;
    }




}
