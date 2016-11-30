package rover.model.auction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

import rover.mediators.data.ScenarioInfo;
import rover.model.auction.bid.AuctionBid;
import rover.model.auction.bid.LocalAuctionBid;
import rover.model.communication.CommunicationManager;
import rover.model.planner.RoverPlanner;
import rover.model.planner.PlannedAction;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;

/**
 * Created by dominic on 30/11/16.
 */
public class AuctionManager {
  private final Logger log;
  private final int totalRemoteAuctionParticipants;
  private final RoverPlanner roverPlanner;
  private final CommunicationManager communicationManager;

  private AuctionRound auctionRound;

  private RoverInfo roverInfo;
  private ScanManager scanManager;
  private ScenarioInfo scenarioInfo;

  public AuctionManager(CommunicationManager communicationManager, int totalRovers) {
    log = LoggerFactory.getLogger("AGENT");
    this.communicationManager = communicationManager;
    totalRemoteAuctionParticipants = totalRovers - 1;
    auctionRound = new AuctionRound(totalRemoteAuctionParticipants);
    roverPlanner = new RoverPlanner();
  }

  public synchronized void executeBidRequest() {
    if(auctionRound.localBidSubmitted()) {
      log.info("Did not submit local bid. Already submitted.");
    } else {
      LocalAuctionBid localAuctionBid = getBestAuctionBid();
      submitBid(localAuctionBid);
    }
  }

  public synchronized void submitBid(LocalAuctionBid localAuctionBid) {
    auctionRound.addLocalBid(localAuctionBid);
    communicationManager.sendAllBid(localAuctionBid);
  }

  public LocalAuctionBid getBestAuctionBid() {
    //TODO    Find best worthwhile collectAction OR Pass
    //TODO    Find best worthwhile searchAction OR Pass
  }

  public synchronized void recieveRemoteBid(AuctionBid auctionBid) {
    auctionRound.addRemoteBid(auctionBid);
    checkEndAuctionRound();
  }

  public void checkEndAuctionRound() {
    if(auctionRound.hasReceivedAllBids()) {
      Optional<AuctionBid> winningBidOptional = auctionRound.getWinningBid();
      if(winningBidOptional.isPresent()){
        AuctionBid winningBid = winningBidOptional.get();
        if(winningBid.isLocalBid()) {
          String roverId = roverInfo.getRoverStateInfo().getId();
          LocalAuctionBid winningLocalBid = auctionRound.getLocalBid();
          Set<PlannedAction> plannedActions = roverPlanner.createPlanFromRoutine(roverId, winningLocalBid.getRoverRoutine());
          plannedActions.forEach(roverPlanner::addAction);
          //TODO Send planned actions to all rovers.
          //TODO Kick off action if it should.
        } else {
          log.info("Waiting for winning rover to confirm end of bidding process.");
        }
      } else {
        log.info("No-one won auction. Rovers should wait until current active rovers run out of actions.");
      }
      confirmEndOfAuction();
      communicationManager.sendAllEndOfAuction();
    } else {
      log.info("Waiting for more bids.");
    }
  }

  public void confirmEndOfAuction() {
    auctionRound.addEndOfAuctionConfirmation();
    if(auctionRound.hasAuctionEnded()) {
      auctionRound = new AuctionRound(totalRemoteAuctionParticipants);
    }
  }
}
