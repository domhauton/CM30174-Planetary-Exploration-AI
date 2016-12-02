package rover.model.communication.ontology.inform;

import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.MessageParser;

/**
 * Created by dominic on 01/12/16.
 */
public class CollectionPlanned implements MessageParser {
  private static int REQUIRED_LENGTH = 3;
  public static String PHRASE = "COLLECTION_PLANNED";

  public boolean isMessageType(String[] message) {
    return isMessageType(message, REQUIRED_LENGTH, PHRASE);
  }

  public Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver) {
    return Optional.of(() -> messageReceiver.collectionPlanned(
            Double.parseDouble(message[1]),
            Double.parseDouble(message[2])));
  }

  @Override
  public String getMessageRoot() {
    return Inform.PHRASE + " " + PHRASE;
  }

  public String generateCommand(double x, double y) {
    return String.format("%s %f %f", getMessageRoot(), x, y);
  }
}