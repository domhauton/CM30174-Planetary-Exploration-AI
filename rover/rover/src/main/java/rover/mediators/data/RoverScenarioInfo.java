package rover.mediators.data;

/**
 * Created by dominic on 24/10/16.
 */
public class RoverScenarioInfo {
  private final Integer height;
  private final Integer width;
  private final Boolean competitive;
  private final Integer totalWorldResources;
  private final Integer scenario;

  public RoverScenarioInfo(Integer height,
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
}
