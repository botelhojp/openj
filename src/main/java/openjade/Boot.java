package openjade;

import java.util.List;

import openjade.setting.Settings;

public class Boot {

	public static void main(String[] args) {		
		boolean monitor = false;
		if (ifArgsContains("-monitor", args)) {
			args = removeArgs("-monitor", args);
			monitor = true;
		}			
		jade.Boot.main(args);
		if (!ifArgsContains("-container", args)) {
			String host = getHost(args);
			if (host != null) {
				String[] main = {"-host", host, "-container", "cms:openjade.agent.CMS"};
				jade.Boot.main(main);
				if (monitor){
					String[] mainMonitor = {"-host", host, "-container", "agent_monitor_001:openjade.trust.gui.MonitorAgent"};
					jade.Boot.main(mainMonitor);
				}
				if (Settings.getInstance().getIterationEnabled()){
					String[] mainTimer = {"-host", host, "-container", "agent_timer_001:openjade.agent.TimerAgent"};
					jade.Boot.main(mainTimer);
				}
			}
		}
	}

	private static String[] removeArgs(String _value, String[] _args) {
		String[] args = new String[_args.length - 1];
		int index = 0;
		for (String string2 : _args) {
			if (!_value.equals(string2)){
				args[index++] = string2;	
			}			
		}
		return args;
	}

	private static boolean ifArgsContains(String find, String[] args) {
		for (String arg : args)
			if (arg.equals(find))
				return true;
		return false;
	}

	private static String getHost(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-host") || args[i].equals("-local-host")) {
				return args[i + 1];
			}
		}
		return null;
	}	
	
	public static void loadXml() {
		List<String[]> args = Settings.getInstance().getBoot();
		if (args != null){
			for (String[] string : args) {
				main(string);
			}
		}	
	}	
}
