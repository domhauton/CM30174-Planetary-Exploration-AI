package rover.model.communication.ontology.inform;

import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.MessageParser;

/**
 * Created by dominic on 01/12/16.
 */
public class ScanComplete implements MessageParser {
  private static int REQUIRED_LENGTH = 4;
  public static String PHRASE = "SCAN_COMPLETE";

  public boolean isMessageType(String[] message) {
    return isMessageType(message, REQUIRED_LENGTH, PHRASE);
  }

  public Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver) {
    return Optional.of(() -> messageReceiver.applyScanComplete(
            (int) Double.parseDouble(message[1]),
            Double.parseDouble(message[2]),
            Double.parseDouble(message[3])));
  }

  @Override
  public String getMessageRoot() {
    return Inform.PHRASE + " " + PHRASE;
  }

  public String generateCommand(int range, double x, double y) {
    return String.format("%s %d %f %f", getMessageRoot(), range, x, y);
  }
}