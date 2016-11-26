package rover.mediators.data;

/**
 * Created by dominic on 24/10/16.
 */
public class ScenarioInfo {
  private final Integer height;
  private final Integer width;
  private final Boolean competitive;
  private final Integer totalWorldResources;
  private final Integer scenario;

  public ScenarioInfo(Integer height,
                      Integer width,
                      Boolean competitive,
                      Integer totalWorldResources,
                      Integer scenario) {
    this.height = height;
    this.width = width;
    this.competitive = competitive;
    this.totalWorldResources = totalWorldResources;
    this.scenario = scenario;
  }

  public Integer getHeight() {
    return height;
  }

  public Integer getWidth() {
    return width;
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
            "height=" + height +
            ", width=" + width +
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

    if (getHeight() != null ? !getHeight().equals(that.getHeight()) : that.getHeight() != null)
      return false;
    if (getWidth() != null ? !getWidth().equals(that.getWidth()) : that.getWidth() != null)
      return false;
    if (getCompetitive() != null ? !getCompetitive().equals(that.getCompetitive()) : that.getCompetitive() != null)
      return false;
    if (getTotalWorldResources() != null ? !getTotalWorldResources().equals(that.getTotalWorldResources()) : that.getTotalWorldResources() != null)
      return false;
    return getScenario() != null ? getScenario().equals(that.getScenario()) : that.getScenario() == null;

  }

  @Override
  public int hashCode() {
    int result = getHeight() != null ? getHeight().hashCode() : 0;
    result = 31 * result + (getWidth() != null ? getWidth().hashCode() : 0);
    result = 31 * result + (getCompetitive() != null ? getCompetitive().hashCode() : 0);
    result = 31 * result + (getTotalWorldResources() != null ? getTotalWorldResources().hashCode() : 0);
    result = 31 * result + (getScenario() != null ? getScenario().hashCode() : 0);
    return result;
  }
}
