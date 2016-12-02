package rover.model.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import rover.controller.BidCollector;
import rover.mediators.RoverFacade;
import rover.mediators.data.message.OutboundTeamMessage;
import rover.mediators.data.update.item.ResourceType;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.communication.ontology.MessageRootParser;
import rover.model.communication.ontology.inform.FoundLiquidResource;
import rover.model.communication.ontology.inform.FoundSolidResource;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;

/**
 * Created by dominic on 30/11/16.
 */
public class CommunicationManager {
  private final Logger logger;
  private final RoverFacade roverFacade;
  private RoverInfo roverInfo;
  private final MessageReceiver messageReceiver;
  private final MessageRootParser messageRootParser;
  private long receivedMessageCounter;


  public CommunicationManager(RoverFacade roverFacade, RoverInfo roverInfo, ScanManager scanManager, ItemManager itemManager, BidCollector bidCollector) {
    this.roverFacade = roverFacade;
    this.roverInfo = roverInfo;
    messageReceiver = new MessageReceiver(scanManager, itemManager, roverInfo, bidCollector);
    messageRootParser = new MessageRootParser();
    receivedMessageCounter = 0;
    logger = LoggerFactory.getLogger("AGENT");
  }

  public synchronized void receiveMessage(String message) {
    logger.info("<< RCV: " + message);
    String[] splitMessage = message.split(" ");
    Optional<Runnable> resultantAction = messageRootParser.parse(splitMessage, messageReceiver);
    if (resultantAction.isPresent()) {
      receivedMessageCounter++;
      resultantAction.get().run();
    } else {
      logger.error("Received unknown message.");
    }
  }

  public void informOthersAboutNewResource(Resource resource) {
    String messageToSend;
    if (resource.getResourceType() == ResourceType.LIQUID) {
      messageToSend = new FoundLiquidResource().generateCommand(
              resource.getCount(),
              resource.getCoordinate().getX(),
              resource.getCoordinate().getY());
    } else {
      messageToSend = new FoundSolidResource().generateCommand(
              resource.getCount(),
              resource.getCoordinate().getX(),
              resource.getCoordinate().getY());
    }
    sendAll(messageToSend);
  }

  public void sendAll(String message) {
    message = roverInfo.getRoverStateInfo().getId().toUpperCase() + " " + message;
    logger.info("> TEAM: " + message);
    OutboundTeamMessage outboundTeamMessage = new OutboundTeamMessage(message);
    roverFacade.sendMessage(outboundTeamMessage);
  }

  public void setScanManager(ScanManager scanManager) {
    messageReceiver.setScanManager(scanManager);
  }

  public void setRoverInfo(RoverInfo roverInfo) {
    this.roverInfo = roverInfo;
    messageReceiver.setRoverInfo(roverInfo);
  }

  public long getReceivedMessageCounter() {
    return receivedMessageCounter;
  }
}
