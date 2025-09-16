package jokerhut.main.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import jokerhut.main.DTs.Axial;
import jokerhut.main.constants.GameConstants;
import jokerhut.main.enums.Faction;
import jokerhut.main.enums.UnitType;
import jokerhut.main.renderer.Gfx;
import jokerhut.main.utils.HexUtils;

public abstract class AbstractUnit {

    private float health;
    private float maxHealth;
    private float defense;
    private float softAttack;
    private float hardAttack;
    private String name;
    private UnitType unitType;
    private Axial position;
    private Integer movementPoints;
    private Integer startingMovementPoints;
    private Faction faction;
    private float fuelCount;

    private TextureRegion sprite;

    public void render(SpriteBatch batch) {

        Vector2 pixelCoordinates = HexUtils.axialToPixelCenter(this.getPosition());
        batch.draw(this.getSprite(), pixelCoordinates.x - GameConstants.HEX_WIDTH / 2,
                pixelCoordinates.y - GameConstants.HEX_HEIGHT / 2,
                GameConstants.HEX_WIDTH, GameConstants.HEX_HEIGHT);

        float x = pixelCoordinates.x - GameConstants.HEX_WIDTH / 2f;
        float y = pixelCoordinates.y - GameConstants.HEX_HEIGHT / 2f;

        drawHpBar(batch, x, y - 6f, GameConstants.HEX_WIDTH, this.getHealth(), this.getMaxHealth());

    }

    private void drawHpBar(SpriteBatch batch, float x, float y, float spriteW,
            float hp, float maxHp) {
        float pad = 6f;
        float w = spriteW - pad;
        float h = 16f;
        float bx = x + pad;
        float by = y;

        float pct = Math.max(0f, Math.min(1f, hp / (float) maxHp));

        Color prev = batch.getColor();

        batch.setColor(0f, 0f, 0f, 0.6f);
        batch.draw(Gfx.PIXEL, bx, by, w, h);

        float r = (1f - pct);
        float g = pct;
        batch.setColor(r, g, 0f, 1f);
        batch.draw(Gfx.PIXEL, bx + 1, by + 1, (w - 2) * pct, h - 2);

        batch.setColor(1f, 1f, 1f, 0.8f);
        batch.draw(Gfx.PIXEL, bx, by, w, 1);
        batch.draw(Gfx.PIXEL, bx, by + h - 1, w, 1);
        batch.draw(Gfx.PIXEL, bx, by, 1, h);
        batch.draw(Gfx.PIXEL, bx + w - 1, by, 1, h);

        batch.setColor(prev);
    }

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

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public float getFuelCount() {
        return fuelCount;
    }

    public void setFuelCount(float fuelCount) {
        this.fuelCount = fuelCount;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

}
