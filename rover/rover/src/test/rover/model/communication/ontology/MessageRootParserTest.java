package rover.model.communication.ontology;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.directive.ClearPlannedCollects;
import rover.model.communication.ontology.directive.ClearPlannedScans;
import rover.model.communication.ontology.inform.FoundLiquidResource;

import static org.junit.Assert.*;

/**
 * Created by dominic on 01/12/16.
 */
public class MessageRootParserTest {

  private MessageRootParser messageRootParser;
  private MessageReceiver messageReceiver;

  @Before
  public void setUp() throws Exception {
    messageRootParser = new MessageRootParser();
    messageReceiver = new MessageReceiver(null, null, null);
  }

  @Test
  public void testBasicInform() throws Exception {
    String message = "DH499-2 INFORM SCAN_COMPLETE 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }


  @Test
  public void testBasicInform6() throws Exception {
    String message = "DH499-2 INFORM COLLECTION_PLANNED 0.353251 3.160129";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testBasicInform2() throws Exception {
    String message = "DH499-2 INFORM SCAN_PLANNED 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testBasicInform3() throws Exception {
    String message = "DH499-2 INFORM FOUND_LIQUID_RESOURCE 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testBasicInform4() throws Exception {
    String message = "DH499-2 INFORM FOUND_SOLID_RESOURCE 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testBasicIncomplete() throws Exception {
    String message = "DH499-2 INFORM SCAN_COMPLETE";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertFalse(optionalAction.isPresent());
  }

  @Test
  public void testBasicIncompleteNonNum() throws Exception {
    String message = "DH499-2 INFORM SCAN_COMPLETE 1 1.56 -2srat.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertFalse(optionalAction.isPresent());
  }

  @Test
  public void testBasicIncompleteNotEnoughArgs() throws Exception {
    String message = "DH499-2 INFORM SCAN_COMPLETE 1 1.56";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertFalse(optionalAction.isPresent());
  }

  @Test
  public void testBasicInformFail() throws Exception {
    String message = "DH499-2 INFORM SCAN_STDRATS 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertFalse(optionalAction.isPresent());
  }

  @Test
  public void testBasicDirective() throws Exception {
    String message = "DH499-2 DIRECTIVE CLEAR_PLANNED_COLLECTS 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testBasicDirective2() throws Exception {
    String message = "DH499-2 DIRECTIVE CLEAR_PLANNED_SCANS 1 1.56 -2.23";
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testMessageRebound1() throws Exception {
    String message = "DH499-2 " + new ClearPlannedScans().generateCommand();
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testMessageRebound2() throws Exception {
    String message = "DH499-2 " +new ClearPlannedCollects().generateCommand();
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }

  @Test
  public void testMessageRebound3() throws Exception {
    String message = "DH499-2 " + new FoundLiquidResource().generateCommand(3, 2.0, 3.0);
    String[] splitMessage = message.split(" ");
    Optional<Runnable> optionalAction = messageRootParser.parse(splitMessage, messageReceiver);
    Assert.assertTrue(optionalAction.isPresent());
  }
}