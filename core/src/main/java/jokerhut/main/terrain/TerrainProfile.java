package jokerhut.main.terrain;

public class TerrainProfile {
    private String terrainType;
    private boolean isVictoryLocation;
    private boolean providesSupply;
    private int supplyRange;
    private int entrenchCap;

    public TerrainProfile() {

    }

    public TerrainProfile(String terrainType, boolean isVictoryLocation, boolean providesSupply, int supplyRange,
            int entrenchCap) {

        this.terrainType = terrainType;
        this.isVictoryLocation = isVictoryLocation;
        this.providesSupply = providesSupply;
        this.supplyRange = supplyRange;
        this.entrenchCap = entrenchCap;

    }

    public String getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(String terrainType) {
        this.terrainType = terrainType;
    }

    public boolean isVictoryLocation() {
        return isVictoryLocation;
    }

    public void setVictoryLocation(boolean isVictoryLocation) {
        this.isVictoryLocation = isVictoryLocation;
    }

    public boolean isProvidesSupply() {
        return providesSupply;
    }

    public void setProvidesSupply(boolean providesSupply) {
        this.providesSupply = providesSupply;
    }

    public int getSupplyRange() {
        return supplyRange;
    }

    public void setSupplyRange(int supplyRange) {
        this.supplyRange = supplyRange;
    }

    public int getEntrenchCap() {
        return entrenchCap;
    }

    public void setEntrenchCap(int entrenchCap) {
        this.entrenchCap = entrenchCap;
    }

}
