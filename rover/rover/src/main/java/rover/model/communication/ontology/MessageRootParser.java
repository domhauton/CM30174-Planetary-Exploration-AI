package rover.model.communication.ontology;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.directive.Directive;
import rover.model.communication.ontology.inform.FoundLiquidResource;
import rover.model.communication.ontology.inform.FoundSolidResource;
import rover.model.communication.ontology.inform.Inform;
import rover.model.communication.ontology.inform.ScanComplete;
import rover.model.communication.ontology.inform.ScanPlanned;

/**
 * Created by dominic on 01/12/16.
 */
/**
 * Created by dominic on 01/12/16.
 */
public class MessageRootParser implements MessageParser {
  private List<MessageParser> speechActs;

  public MessageRootParser() {
    speechActs = Arrays.asList(
            new Inform(),
            new Directive()
    );
  }

  public boolean isMessageType(String[] message) {
    return true;
  }

  public Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver) {
    String[] cutMessage = Arrays.copyOfRange(message, 1, message.length);
    System.out.println(Arrays.toString(cutMessage));
    return speechActs.stream()
            .filter(messageParser -> messageParser.isMessageType(cutMessage))
            .map(messageParser -> messageParser.parse(cutMessage, messageReceiver))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
  }

  @Override
  public String getMessageRoot() {
    return "";
  }
}
