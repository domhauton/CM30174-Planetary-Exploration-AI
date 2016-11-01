package rover.model;

import java.util.Collection;

import rover.model.maplocation.LocationManager;
import rover.model.maplocation.Resource;

/**
 * Created by dominic on 27/10/16.
 */
public class WorldManager {
  private RoverInfo roverInfo;
  private LocationManager locationManager;


  public WorldManager(RoverInfo roverInfo) {
    this.roverInfo = roverInfo;
    this.locationManager = new LocationManager();
  }

  public Integer getCurrentPayloadCount() {
    return roverInfo.getPayloadCount();
  }

  public Integer getPayloadLocationCount() {
    return locationManager.getResourceCount();
  }

  public Integer getRemainingStorageSpace() {
    return roverInfo.getPayloadSpace();
  }
}
