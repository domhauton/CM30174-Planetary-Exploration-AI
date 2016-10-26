package data.world;

/**
 * Created by dominic on 21/10/16.
 *
 * Represents an object found on the map from a scan.
 */
abstract class WorldObject {
    private final MapLocation mapLocation;

    public WorldObject(MapLocation mapLocation) {
        this.mapLocation = mapLocation;
    }

    public MapLocation getMapLocation() {
        return mapLocation;
    }
}
