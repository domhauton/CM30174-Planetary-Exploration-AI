package rover.mediators.data;

/**
 * Created by dominic on 24/10/16.
 */
public class ScenarioInfo {
  private final Integer size;
  private final Boolean competitive;
  private final Integer totalWorldResources;
  private final Integer scenario;

  public ScenarioInfo(Integer size,
                      Boolean competitive,
                      Integer totalWorldResources,
                      Integer scenario) {
    this.size = size;
    this.competitive = competitive;
    this.totalWorldResources = totalWorldResources;
    this.scenario = scenario;
  }


  public Integer getSize() {
    return size;
  }

  public Boolean getCompetitive() {
    return competitive;
  }

  public Integer getTotalWorldResources() {
    return totalWorldResources;
  }

  public Integer getScenario() {
    return scenario;
  }

  @Override
  public String toString() {
    return "ScenarioInfo{" +
            ", size=" + size +
            ", competitive=" + competitive +
            ", totalWorldResources=" + totalWorldResources +
            ", scenario=" + scenario +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScenarioInfo)) return false;

    ScenarioInfo that = (ScenarioInfo) o;

    return getSize() != null ? getSize().equals(that.getSize()) : that.getSize() == null
            && (getCompetitive() != null ? getCompetitive().equals(that.getCompetitive()) : that.getCompetitive() == null
            && (getTotalWorldResources() != null ? getTotalWorldResources().equals(that.getTotalWorldResources()) : that.getTotalWorldResources() == null
            && (getScenario() != null ? getScenario().equals(that.getScenario()) : that.getScenario() == null)));

  }

  @Override
  public int hashCode() {
    int result = 31 * (getSize() != null ? getSize().hashCode() : 0);
    result = 31 * result + (getCompetitive() != null ? getCompetitive().hashCode() : 0);
    result = 31 * result + (getTotalWorldResources() != null ? getTotalWorldResources().hashCode() : 0);
    result = 31 * result + (getScenario() != null ? getScenario().hashCode() : 0);
    return result;
  }
}
