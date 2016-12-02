package rover.model.communication.ontology.directive;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.MessageParser;

/**
 * Created by dominic on 01/12/16.
 */
public class Directive implements MessageParser {
  private final static int REQUIRED_LENGTH = 1;
  public static String PHRASE = "DIRECTIVE";
  private List<MessageParser> furtherParsers;

  public Directive() {
    furtherParsers = Arrays.asList(
            new ClearPlannedScans(),
            new ClearPlannedCollects()
    );
  }

  public boolean isMessageType(String[] message) {
    return isMessageType(message, REQUIRED_LENGTH, PHRASE);
  }

  public Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver) {
    String[] cutMessage = Arrays.copyOfRange(message, REQUIRED_LENGTH, message.length);
    return furtherParsers.stream()
            .filter(messageParser -> messageParser.isMessageType(cutMessage))
            .map(messageParser -> messageParser.parse(cutMessage, messageReceiver))
            .map(Optional::get)
            .findFirst();
  }

  @Override
  public String getMessageRoot() {
    return PHRASE;
  }

}