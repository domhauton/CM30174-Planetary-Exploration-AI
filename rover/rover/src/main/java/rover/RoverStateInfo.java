package rover;

import com.google.common.base.Objects;

import java.util.Optional;

/**
 * Created by dominic on 26/10/16.
 */
public class RoverStateInfo {
  private final Optional<Integer> load;
  private final Optional<Double> energy;
  private final Boolean isStarted;
  private final String teamName;
  private final String id;

  public RoverStateInfo(Optional<Integer> load,
                        Optional<Double> energy,
                        Boolean isStarted,
                        String teamName,
                        String id) {
    this.load = load;
    this.energy = energy;
    this.isStarted = isStarted;
    this.teamName = teamName;
    this.id = id;
  }

  public Optional<Integer> getLoad() {
    return load;
  }

  public Optional<Double> getEnergy() {
    return energy;
  }

  public Boolean getStarted() {
    return isStarted;
  }

  public String getTeamName() {
    return teamName;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoverStateInfo)) return false;
    RoverStateInfo that = (RoverStateInfo) o;
    return Objects.equal(load, that.load) &&
            Objects.equal(energy, that.energy) &&
            Objects.equal(isStarted, that.isStarted) &&
            Objects.equal(teamName, that.teamName) &&
            Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(load, energy, isStarted, teamName, id);
  }
}
