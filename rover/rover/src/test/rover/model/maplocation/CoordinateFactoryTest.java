package rover.model.maplocation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by dominic on 30/10/16.
 */
public class CoordinateFactoryTest {

  private Coordinate polLoc1;
  private Coordinate polLoc2;
  private Coordinate polLoc3;
  private Coordinate polLoc4;

  private Coordinate cartLoc1;
  private Coordinate cartLoc2;
  private Coordinate cartLoc3;
  private Coordinate cartLoc4;

  @Before
  public void setUp() throws Exception {
    polLoc1 = CoordinateFactory.buildPolar(0d, 0d);
    polLoc2 = CoordinateFactory.buildPolar(1d, 0d);
    polLoc3 = CoordinateFactory.buildPolar(1d, 0.5d*Math.PI);
    polLoc4 = CoordinateFactory.buildPolar(Math.sqrt(2d), 0.75d*Math.PI);

    cartLoc1 = CoordinateFactory.buildCartesian(0d, 0d);
    cartLoc2 = CoordinateFactory.buildCartesian(1d, 0d);
    cartLoc3 = CoordinateFactory.buildCartesian(0d, 1d);
    cartLoc4 = CoordinateFactory.buildCartesian(-1d, 1d);
  }

  @Test
  public void getCartesianLocationOrigin() throws Exception {
    Assert.assertEquals(cartLoc1, polLoc1);
  }

  @Test
  public void getCartesianLocationSimple() throws Exception {
    Assert.assertEquals(cartLoc2, polLoc2);
  }

  @Test
  public void getCartesianLocationAngleDirectionTest() throws Exception {
    Assert.assertEquals(cartLoc3, polLoc3);
  }

  @Test
  public void getCartesianLocationBothPlanes() throws Exception {
    Assert.assertEquals(cartLoc4, polLoc4);
  }

}