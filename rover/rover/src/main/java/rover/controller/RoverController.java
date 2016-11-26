package rover.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;

/**
 * Created by dominic on 26/10/16.
 *
 * Main controller for the rover
 */
public class RoverController {
  private final RoverFacade roverFacade;
  private final Logger logger;
  private boolean first = true;

  public RoverController(RoverFacade roverFacade,
                         RoverBusSubProvider<UpdateEvent> updateSubService,
                         RoverBusSubProvider<InboundMessage> messageSubService,
                         RoverBusSubProvider<ScenarioInfo> scenarioSubService,
                         RoverBusSubProvider<RoverStateInfo> stateSubService) {
    logger = LoggerFactory.getLogger("AGENT");
    roverFacade.setErrorReporter(this::processUpdate);
    this.roverFacade = roverFacade;
    // ROVER FACADE MUST BE INITIALIZED FIRST. CAUTION!
    updateSubService.subscribe(this::processUpdate);
    messageSubService.subscribe(this::processMessage);
    scenarioSubService.subscribe(this::processScenarioUpdate);
    stateSubService.subscribe(this::processRoverUpdate);
    logger.info("Initiated Rover");
    // DO NOT CALL FACADE FROM CONSTRUCTOR!
  }

  private void processUpdate(UpdateEvent updateEvent) {
    logger.info("ROVER RECEIVED - {}", updateEvent.toString());
    if(first){
      first = false;
      roverFacade.move(9.0, 12.0, 2.0);
    }
  }

  private void processMessage(InboundMessage inboundMessage) {
    logger.info("ROVER RECEIVED - {}", inboundMessage.toString());
  }

  private void processScenarioUpdate(ScenarioInfo scenarioInfo) {
    logger.info("ROVER RECEIVED - {}", scenarioInfo.toString());
  }

  private void processRoverUpdate(RoverStateInfo stateInfo) {
    logger.info("ROVER RECEIVED - {}", stateInfo.toString());
  }

  public void end() {
    logger.info("Shutting down agent Controller");
  }

}
