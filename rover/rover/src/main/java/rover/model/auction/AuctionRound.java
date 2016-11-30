package rover.model.auction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import rover.model.auction.bid.AuctionBid;
import rover.model.auction.bid.BidType;
import rover.model.auction.bid.LocalAuctionBid;

/**
 * Created by dominic on 30/11/16.
 */
public class AuctionRound {
  private final int totalExpectedRemoteBids;
  private final Collection<AuctionBid> remoteAuctionBids;
  private LocalAuctionBid localBid;
  private int endOfAuctionConfirmations;

  AuctionRound(int totalExpectedRemoteBids) {
    this.totalExpectedRemoteBids = totalExpectedRemoteBids;
    remoteAuctionBids = new LinkedList<>();
  }

  public synchronized void addLocalBid(LocalAuctionBid localBid) {
    this.localBid = localBid;
  }

  public synchronized void addRemoteBid(AuctionBid auctionBid) {
    remoteAuctionBids.add(auctionBid);
  }

  public synchronized boolean hasReceivedAllBids() {
    return localBidSubmitted() && remoteAuctionBids.size() >= totalExpectedRemoteBids;
  }

  public boolean localBidSubmitted() {
    return localBid != null;
  }

  public LocalAuctionBid getLocalBid() {
    return localBid;
  }

  public synchronized Optional<AuctionBid> getWinningBid() {
    Optional<AuctionBid> bestCollectAction = getWinningBid(BidType.COLLECT);
    if(bestCollectAction.isPresent()){
      return bestCollectAction;
    } else {
      return getWinningBid(BidType.SCAN);
    }
  }

  private Optional<AuctionBid> getWinningBid(BidType bidType) {
    LinkedList<AuctionBid> tmpList = new LinkedList<>();
    tmpList.addAll(remoteAuctionBids);
    tmpList.add(localBid);
    return tmpList.stream()
            .filter(auctionBid -> auctionBid.getBidType() == bidType)
            .sorted((o1, o2) -> -o1.getProcessSpeedup().compareTo(o2.getProcessSpeedup()))
            .findFirst();
  }

  void addEndOfAuctionConfirmation() {
    endOfAuctionConfirmations++;
  }

  boolean hasAuctionEnded() {
    return endOfAuctionConfirmations > totalExpectedRemoteBids;
  }
}
