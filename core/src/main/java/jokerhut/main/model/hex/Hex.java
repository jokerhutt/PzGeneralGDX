package jokerhut.main.model.hex;

import jokerhut.main.model.geo.TerrainProfile;

public class Hex {

    private int q;
    private int r;

    private String terrain;

    private int defence;
    private int moveCost;

    private TerrainProfile terrainProfile;

    public Hex(int q, int r, String terrain, int defence, int moveCost) {
        this.q = q;
        this.r = r;
        this.terrain = terrain;
        this.defence = defence;
        this.moveCost = moveCost;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(int moveCost) {
        this.moveCost = moveCost;
    }

    public TerrainProfile getTerrainProfile() {
        return terrainProfile;
    }

    public void setTerrainProfile(TerrainProfile terrainProfile) {
        this.terrainProfile = terrainProfile;
    }

}
