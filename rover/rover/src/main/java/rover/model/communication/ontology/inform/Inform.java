package rover.model.communication.ontology.inform;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.MessageParser;

/**
 * Created by dominic on 01/12/16.
 */
public class Inform implements MessageParser {
  private final static int REQUIRED_LENGTH = 1;
  public static String PHRASE = "INFORM";
  private List<MessageParser> furtherParsers;

  public Inform() {
    furtherParsers = Arrays.asList(
            new FoundLiquidResource(),
            new FoundSolidResource(),
            new ScanComplete(),
            new ScanPlanned()
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
