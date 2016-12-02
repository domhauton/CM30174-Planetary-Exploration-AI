package rover.model.communication.ontology.directive;

import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.MessageParser;

/**
 * Created by dominic on 01/12/16.
 */
public class ClearPlannedCollects implements MessageParser{
  private static int REQUIRED_LENGTH = 1;
  public static String PHRASE = "CLEAR_PLANNED_COLLECTS";

  public boolean isMessageType(String[] message) {
    return isMessageType(message, REQUIRED_LENGTH, PHRASE);
  }

  public Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver) {
    return Optional.of(messageReceiver::clearPlannedCollects);
  }

  @Override
  public String getMessageRoot() {
    return Directive.PHRASE + " " + PHRASE;
  }

  public String generateCommand() {
    return getMessageRoot();
  }
}