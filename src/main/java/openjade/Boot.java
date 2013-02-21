package openjade;

import openjade.setting.Settings;

public class Boot {

	public static void main(String[] args) {
		jade.Boot.main(args);
		if (!ifArgsContains("-container", args)) {
			String host = getHost(args);
			if (host != null) {
				String[][] mains = { 
						{"-host", host, "-container", "cms:openjade.agent.CMS"},
						{"-host", host, "-container", "agent_monitor_001:openjade.trust.gui.MonitorAgent"}						
					};
				for (String[] main : mains) {
					jade.Boot.main(main);	
				}				
			}
			if (Settings.getInstance().isTimerEnabled()){
				String[] main = {"-host", host, "-container", "agent_timer_001:openjade.agent.TimerAgent"};
				jade.Boot.main(main);
			}
		}
	}

	private static boolean ifArgsContains(String find,String[] args) {
		for (String arg : args)
			if (arg.equals(find))
				return true;
		return false;
	}

	private static String getHost(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-host")) {
				return args[i + 1];
			}
		}
		return null;
	}
}
