package rover.model.auction.bid;

import rover.model.action.routine.RoverRoutine;

/**
 * Created by dominic on 30/11/16.
 */
public class LocalAuctionBid extends AuctionBid {

  private final RoverRoutine roverRoutine;

  public LocalAuctionBid(BidType bidType, Double actionEfficiency, Double processSpeedup, RoverRoutine roverRoutine) {
    super(bidType, actionEfficiency, processSpeedup, true);
    this.roverRoutine = roverRoutine;
  }

  public RoverRoutine getRoverRoutine() {
    return roverRoutine;
  }
}
