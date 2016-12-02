package rover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.model.action.ActionController;
import rover.model.action.routine.RoverRoutine;

/**
 * Created by dominic on 01/12/16.
 */
public class BidCollector {
  private Logger logger;
  private int totalRovers;
  private final ActionController actionController;

  private RoverRoutine personalBid;
  private double personalBidValue;
  private double bestExternalBidValue;
  private int totalBids;
  private int round;


  BidCollector(int totalRovers, ActionController actionController) {
    logger = LoggerFactory.getLogger("AGENT");
    round = 0;
    this.totalRovers = totalRovers;
    this.actionController = actionController;
  }

  public synchronized void acceptExternalBid(double value) {
    if(totalBids == totalRovers) {
      reset();
    }
    totalBids++;
    bestExternalBidValue = Math.max(bestExternalBidValue, value);
    checkComplete();
  }

  synchronized void acceptPersonalBid(double value, RoverRoutine roverRoutine) {
    if(totalBids == totalRovers) {
      reset();
    }
    totalBids++;
    personalBidValue = value;
    personalBid = roverRoutine;
    checkComplete();
  }

  private void checkComplete() {
    logger.info("Bid Collector checking {} completion {} of {}", round, totalBids, totalRovers);
    if(totalBids == totalRovers) {
      if(personalBidValue >= bestExternalBidValue) {
        logger.info("Bid Collector won bid {} at {} of {}", round, personalBidValue, bestExternalBidValue);
        RoverRoutine winningRoutine = personalBid;
        actionController.setRoutineToExecute(winningRoutine);
      } else {
        logger.info("Bid Collector lost bid {} at {} of {}", round, personalBidValue, bestExternalBidValue);
      }
      actionController.executeAction();
    }
  }

  private void reset() {
    logger.info("Resetting bid collector!");
    personalBid = null;
    round++;
    bestExternalBidValue = 0;
    totalBids = 0;
    personalBidValue = 0;
  }

  synchronized void setTotalRovers(int totalRovers) {
    this.totalRovers = totalRovers;
  }
}
