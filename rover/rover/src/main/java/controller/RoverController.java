package controller;

import rover.mediators.RoverActionService;
import rover.mediators.RoverMessageService;
import rover.mediators.RoverUpdateBus;

/**
 * Created by dominic on 26/10/16.
 */
public class RoverController {
  private final RoverActionService roverActionService;
  private final RoverUpdateBus roverUpdateBus;
  private final RoverMessageService roverMessageService;
}
