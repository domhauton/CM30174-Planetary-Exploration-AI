package rover.model.action.primitives;

/**
 * Created by dominic on 23/11/16.
 */
public class ActionCost {
  private final double energy;
  private final double time;

  public ActionCost(double energy, double time) {
    this.energy = energy;
    this.time = time;
  }

  public double getEnergy() {
    return energy;
  }

  public double getTime() {
    return time;
  }
}
