package rover.model.communication.ontology;

import org.apache.commons.lang.math.NumberUtils;

import java.util.Optional;
import java.util.stream.IntStream;

import rover.model.communication.MessageReceiver;

/**
 * Created by dominic on 01/12/16.
 */
public interface MessageParser {

  default boolean isMessageType(String[] message, Integer requiredLength, String phrase) {
    return message.length >= requiredLength && message[0].equals(phrase)
            && IntStream.range(1, requiredLength).allMatch(x -> NumberUtils.isNumber(message[x]));
  }

  boolean isMessageType(String[] message);

  Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver);

  String getMessageRoot();
}
