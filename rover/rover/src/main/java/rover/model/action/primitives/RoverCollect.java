package rover.model.action.primitives;

import java.util.Objects;

import rover.mediators.RoverFacade;
import rover.model.communication.CommunicationManager;
import rover.model.communication.ontology.directive.ClearPlannedCollects;
import rover.model.communication.ontology.inform.CollectionComplete;
import rover.model.communication.ontology.inform.CollectionFailed;
import rover.model.communication.ontology.inform.CollectionPlanned;
import rover.model.roverinfo.RoverInfo;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;

/**
 * Created by dominic on 29/11/16.
 */
public class RoverCollect extends RoverAction {
  private Resource resource;
  private ItemManager itemManager;

  public RoverCollect(RoverInfo roverInfo, CommunicationManager communicationManager, Resource resource, ItemManager itemManager) {
    super(roverInfo, communicationManager);
    this.resource = resource;
    this.itemManager = itemManager;
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(5, 5);
  }

  @Override
  public void selected() {
    itemManager.itemCollectPlanned(resource.getCoordinate());
    String message = new CollectionPlanned()
            .generateCommand(resource.getCoordinate().getX(), resource.getCoordinate().getY());
    communicationManager.sendAll(message);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.collect();
  }

  @Override
  public void complete() {
    roverInfo.addPayload();
    itemManager.itemCollected(resource.getCoordinate());
    String message = new CollectionComplete()
            .generateCommand(resource.getCoordinate().getX(), resource.getCoordinate().getY());
    communicationManager.sendAll(message);
  }

  @Override
  public void failed() {
    logger.error("Failed to pick up payload. Scenario may have changed.");
    itemManager.itemNonExistent(resource.getCoordinate());
    String message = new CollectionFailed()
            .generateCommand(resource.getCoordinate().getX(), resource.getCoordinate().getY());
    communicationManager.sendAll(message);

    itemManager.revertPlannedCollections();
    String resetMessage = new ClearPlannedCollects().generateCommand();
    communicationManager.sendAll(resetMessage);
  }

  @Override
  public String getType() {
    return "COLLECT";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoverCollect)) return false;
    if (!super.equals(o)) return false;
    RoverCollect that = (RoverCollect) o;
    return Objects.equals(resource, that.resource) &&
            Objects.equals(itemManager, that.itemManager);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), resource, itemManager);
  }
}
