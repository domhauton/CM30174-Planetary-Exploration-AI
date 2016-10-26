package data.world;

/**
 * Created by dominic on 21/10/16.
 *
 * Contains a map location.
 */
class MapLocation {
    private final Double x;
    private final Double y;

    MapLocation(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
