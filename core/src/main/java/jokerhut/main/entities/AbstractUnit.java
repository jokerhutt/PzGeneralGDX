package jokerhut.main.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.DTs.Axial;

public abstract class AbstractUnit {

    private float health;
    private float defense;
    private float softAttack;
    private float hardAttack;
    private String name;
    private Axial position;

    private TextureRegion sprite;


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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Axial getPosition() {
        return position;
    }
    public void setPosition(Axial position) {
        this.position = position;
    }
    public TextureRegion getSprite() {
        return sprite;
    }
    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }







}
