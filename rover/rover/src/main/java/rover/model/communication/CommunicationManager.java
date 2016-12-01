package rover.model.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import rover.mediators.RoverFacade;
import rover.mediators.data.message.OutboundTeamMessage;
import rover.model.collection.ItemManager;
import rover.model.communication.ontology.MessageRootParser;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;

/**
 * Created by dominic on 30/11/16.
 */
public class CommunicationManager {
  private final Logger logger;
  private final RoverFacade roverFacade;
  private final MessageReceiver messageReceiver;
  private final MessageRootParser messageRootParser;


  public CommunicationManager(RoverFacade roverFacade, RoverInfo roverInfo, ScanManager scanManager, ItemManager itemManager) {
    this.roverFacade = roverFacade;
    messageReceiver = new MessageReceiver(scanManager, itemManager, roverInfo);
    messageRootParser = new MessageRootParser();
    logger = LoggerFactory.getLogger("AGENT");
  }

  public void receiveMessage(String message) {
    String[] splitMessage = message.split(" ");
    Optional<Runnable> resultantAction = messageRootParser.parse(splitMessage, messageReceiver);
    if(resultantAction.isPresent()) {
      resultantAction.get().run();
    } else {
      logger.error("Received unknown message.");
    }
  }

  public void sendAll(String message) {
    System.out.println("Sending to all: " + message);
    OutboundTeamMessage outboundTeamMessage = new OutboundTeamMessage(message);
    roverFacade.sendMessage(outboundTeamMessage);
  }
}
