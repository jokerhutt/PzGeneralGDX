package jokerhut.main.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import jokerhut.main.DTs.Axial;
import jokerhut.main.enums.UnitType;

public abstract class AbstractUnit {

    private float health;
    private float defense;
    private float softAttack;
    private float hardAttack;
    private String name;
    private UnitType unitType;
    private Axial position;
    private Integer movementPoints;
    private Integer startingMovementPoints;

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

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Integer getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(Integer movementPoints) {
        this.movementPoints = movementPoints;
    }

    public Integer getStartingMovementPoints() {
        return startingMovementPoints;
    }

    public void setStartingMovementPoints(Integer startingMovementPoints) {
        this.startingMovementPoints = startingMovementPoints;
    }

}
