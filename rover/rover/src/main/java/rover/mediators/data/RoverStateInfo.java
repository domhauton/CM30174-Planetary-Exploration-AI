package rover.mediators.data;

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

  public Integer getLoad() {
    return load.orElse(0);
  }

  public Double getEnergy() {
    return energy.orElse(0.0);
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
  public String toString() {
    return "RoverStateInfo{" +
            "load=" + load +
            ", energy=" + energy +
            ", isStarted=" + isStarted +
            ", teamName='" + teamName + '\'' +
            ", id='" + id + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoverStateInfo)) return false;

    RoverStateInfo that = (RoverStateInfo) o;

    if (getLoad() != null ? !getLoad().equals(that.getLoad()) : that.getLoad() != null)
      return false;
    if (getEnergy() != null ? !getEnergy().equals(that.getEnergy()) : that.getEnergy() != null)
      return false;
    if (isStarted != null ? !isStarted.equals(that.isStarted) : that.isStarted != null)
      return false;
    if (getTeamName() != null ? !getTeamName().equals(that.getTeamName()) : that.getTeamName() != null)
      return false;
    return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

  }

  @Override
  public int hashCode() {
    int result = getLoad() != null ? getLoad().hashCode() : 0;
    result = 31 * result + (getEnergy() != null ? getEnergy().hashCode() : 0);
    result = 31 * result + (isStarted != null ? isStarted.hashCode() : 0);
    result = 31 * result + (getTeamName() != null ? getTeamName().hashCode() : 0);
    result = 31 * result + (getId() != null ? getId().hashCode() : 0);
    return result;
  }
}
