package rover.model.planner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import rover.model.action.primitives.RoverAction;
import rover.model.action.routine.RoverRoutine;
import rover.model.collection.ItemManager;
import rover.model.communication.CommunicationManager;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;
import util.UIDSupplier;

/**
 * Created by dominic on 30/11/16.
 */
public class RoverPlanner {
  private final Logger logger;
  private final UIDSupplier localUIDSupplier;
  private final CommunicationManager communicationManager;

  public LinkedHashMap<String, PlannedAction> actionHistory;

  public RoverPlanner(CommunicationManager communicationManager) {
    logger = LoggerFactory.getLogger("AGENT");
    this.communicationManager = communicationManager;
    actionHistory = new LinkedHashMap<>();
    localUIDSupplier = new UIDSupplier();
  }

  public Set<PlannedAction> createPlanFromRoutine(String roverId, final RoverRoutine roverRoutine) {
    return roverRoutine.getActionList()
            .stream()
            .map(roverAction -> getNewPlannedAction(roverId, roverAction, roverRoutine))
            .collect(Collectors.toSet());
  }

  private PlannedAction getNewPlannedAction(String roverId, RoverAction roverAction, RoverRoutine parentRoutine) {
    Integer actionSequenceNumber = localUIDSupplier.next();
    String actionUID = roverId + "-" +  actionSequenceNumber;
    return new PlannedAction(roverAction, parentRoutine, StoredActionState.PENDING, StoredActionType.LOCAL, actionSequenceNumber, actionUID);
  }

  public synchronized void addAction(final PlannedAction plannedAction) {
    actionHistory.put(plannedAction.getUid(), plannedAction);
    communicationManager.sendAllPlannedAction(plannedAction);
  }

  public synchronized void moveToComplete(String uid) {
    moveState(uid, StoredActionState.PROCESSED);
    communicationManager.sendAllActionComplete(uid);
  }

  public synchronized void moveToProcessing(String uid) {
    moveState(uid, StoredActionState.PROCESSING);
    communicationManager.sendAllActionProcessing(uid);
  }

  public synchronized void moveToPending(String uid) {
    moveState(uid, StoredActionState.PENDING);
    communicationManager.sendAllActionPending(uid);
  }

  private void moveState(String uid, StoredActionState state) {
    PlannedAction plannedAction = actionHistory.get(uid);
    if(plannedAction == null) {
      logger.error("Attempted to modify non-existent action.");
    } else {
      PlannedAction modifiedAction = plannedAction.clone(StoredActionState.PROCESSING);
      actionHistory.put(uid, modifiedAction);
    }
  }

  /**
   * Clears all pending action and resets model to previous confirmed action.
   * @param scanManager
   * @param itemManager
   * @param roverInfo
   */
  public synchronized void clearPending(ScanManager scanManager, ItemManager itemManager, RoverInfo roverInfo) {
    LinkedHashMap<String, PlannedAction> newHistory = new LinkedHashMap<>();
    actionHistory.entrySet().stream()
            .filter(entry -> entry.getValue().getState() != StoredActionState.PENDING)
            .forEachOrdered(entry -> newHistory.put(entry.getKey(), entry.getValue()));
    actionHistory = newHistory;
    scanManager.clearScanMap();
    itemManager.clearItemManager();
    roverInfo.resetRoverInfo();
    actionHistory.values()
            .stream()
            .map(PlannedAction::getRoverAction)
            .forEach(RoverAction::complete);
  }

  public synchronized Optional<PlannedAction> getNextLocalAction() {
    return actionHistory.values()
            .stream()
            .filter(plannedAction -> plannedAction.getType() == StoredActionType.LOCAL)
            .filter(plannedAction -> plannedAction.getState() == StoredActionState.PENDING)
            .sorted((o1, o2) -> o1.getActionSequenceNumber().compareTo(o2.getActionSequenceNumber()))
            .findFirst();
  }

  public synchronized boolean hasNextAction() {
    return actionHistory.values()
            .stream()
            .filter(plannedAction -> plannedAction.getType() == StoredActionType.LOCAL)
            .anyMatch(plannedAction -> plannedAction.getState() != StoredActionState.PROCESSED);
  }
}
