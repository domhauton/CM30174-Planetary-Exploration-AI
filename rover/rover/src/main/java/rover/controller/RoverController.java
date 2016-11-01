package rover.controller;


import org.slf4j.Logger;

import rover.mediators.RoverFacade;
import rover.mediators.bus.RoverBusSubProvider;
import rover.mediators.data.RoverScenarioInfo;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.message.InboundMessage;
import rover.mediators.data.update.UpdateEvent;

/**
 * Created by dominic on 26/10/16.
 */
public class RoverController {
  private final RoverFacade roverFacade;
  private final RoverBusSubProvider<UpdateEvent> updateSubService;
  private final RoverBusSubProvider<InboundMessage> messageSubService;
  private final RoverBusSubProvider<RoverScenarioInfo> scenarioSubService;
  private final RoverBusSubProvider<RoverStateInfo> stateSubService;
  private final Logger logger;

  public RoverController(RoverFacade roverFacade,
                         RoverBusSubProvider<UpdateEvent> updateSubService,
                         RoverBusSubProvider<InboundMessage> messageSubService,
                         RoverBusSubProvider<RoverScenarioInfo> scenarioSubService,
                         RoverBusSubProvider<RoverStateInfo> stateSubService,
                         Logger logger) {
    this.roverFacade = roverFacade;
    this.updateSubService = updateSubService;
    this.messageSubService = messageSubService;
    this.scenarioSubService = scenarioSubService;
    this.stateSubService = stateSubService;
    this.logger = logger;
  }
}
