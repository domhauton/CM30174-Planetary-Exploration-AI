package rover;

import javax.swing.SwingUtilities;

import org.iids.aos.agent.Agent;
import org.iids.aos.service.ServiceBroker;

import org.iids.aos.service.ServiceException;
import rover.MonitorInfo.Resource;
import rover.MonitorInfo.Rover;
import rover.MonitorInfo.Team;



public class MonitorAgent extends Agent  {
	
	private static final long serialVersionUID = 1L;

	private IRoverService service;
	private ServiceBroker sb;
	private RoverDisplay display;
	
	public MonitorAgent() {
		service = null;
		sb = null;
		display = null;
		
		//preload cause of agentscape bug;
		MonitorInfo mi = new MonitorInfo(0, 0);
		Rover r = mi.new Rover(0, 0, "");
		Team t = mi.new Team(0, 0, "", 0);
		Resource rs = mi.new Resource(0, 0, 0);
		
	}

	@Override
	public void run() {
		sb = getServiceBroker();
        display = new RoverDisplay(sb);
        display.setVisible(true);
		runMonitor();
	}

	private void runMonitor() {
                boolean connected = true;
                Runnable updateDisplay = () -> display.UpdateDisplay(service.getWorldInfo());

				while(display.isVisible() && connected) {

                    try {
                        service = sb.bind(IRoverService.class);
                        connected = true;

                    } catch (ServiceException s) {
                        connected = false;
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    SwingUtilities.invokeLater(updateDisplay);

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
	}
	

}
