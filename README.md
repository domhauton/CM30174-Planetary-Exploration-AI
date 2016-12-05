# IA-AgentScape-Rover

Scenarios are in the scenario folder and are numbered as required. Rover names are aliases for sets of scenarios. The rover will adapt to use its attributes to the best of it's ability.

## Brief overview

- Agent
- AgentDecorator (Abstracts the basic agent to make it more usable)
- Mediators (Polls the decorator and creates a pub/sub feed)
- RoverController - Helps communication between various managers. There is overlap between managers but it's avoided where possible.
- ActionController - Decides what the rover will do. This is fed from the BidCollector which in turn finds the most desired action using the IntentionManager.
- IntentionManager - Min maxes the best collection or scan with no look-ahead. Assigns this action a desire.
- RoverFacade - Allows interaction back with the bot to request actions and send messages.
- CommunicationManager - Handles all inter-rover communication.
