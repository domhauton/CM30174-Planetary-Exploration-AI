package rover.model.auction.bid;

/**
 * Created by dominic on 30/11/16.
 */
public class AuctionBid {
  private final boolean localBid;
  private final BidType bidType;
  private final Double actionEfficiency;
  private final Double processSpeedup;

  public AuctionBid(BidType bidType, Double actionEfficiency, Double processSpeedup, boolean localBid) {
    this.bidType = bidType;
    this.actionEfficiency = actionEfficiency;
    this.processSpeedup = processSpeedup;
    this.localBid = localBid;
  }

  public boolean isLocalBid() {
    return isLocalBid();
  }

  public BidType getBidType() {
    return bidType;
  }

  public Double getActionEfficiency() {
    return actionEfficiency;
  }

  public Double getProcessSpeedup() {
    return processSpeedup;
  }
}
