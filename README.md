# PzCorpsGDX

This is a personal project to make a PanzerCorps 1 style game in LibGDX.

## Map setup and Coordinates

### Map setup

The hex map comes as a TMX and is flat top with an odd stagger index
The map is then parsed into a Hashmap<Axial, Hex> with axial coordinates.

### Rendering

When rendering, axial coordinates are converted into pixel ones using the following formula:

```java

public static Vector2 axialToPixelCenter(Axial axial) {

    float x = (3f / 2f) * axial.q();
    float y = (float) (Math.sqrt(3) / 2 * axial.q() + Math.sqrt(3) * axial.r());

    x = x * GameConstants.HEX_SIZE + GameConstants.HEX_SIZE;
    y = y * GameConstants.HEX_SIZE * GameConstants.HEX_Y_SCALE;

    return new Vector2(x, y);

}

```
