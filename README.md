# PzCorpsGDX

This is a personal project to make a PanzerCorps 1 style game in LibGDX.

## Map setup and Coordinates

### Map setup

The hex map comes as a TMX and is flat top with an odd stagger index
The map is then parsed into a `Hashmap<Axial, Hex>` with axial coordinates.

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

## Movement

### Movement animations

When a unit is selected, the MovementService computes a hashmap of all reachable tiles,
and for each tile computes the shortest path to reach that unit.

When the user right clicks a reachable tile, the path for that tile its reconstructed into a queue.
This reconstructed path is then passed to MovementSystem, which adds the unit and a new instance of the `Motion` class to an ObjectMap tracking active motions.

In each frame, the MovementSystem iterates over the ObjectMap and updates the render position of the unit.

Render wise, in each frame, the unit then renders its sprite based on the render position.

When a unit reaches the center of the hex it is moving towards, it is removed from the ObjectMap.
