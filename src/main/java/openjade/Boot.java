package openjade;

public class Boot {

	public static void main(String[] args) {
		jade.Boot.main(args);
		if (isMainContainer(args)) {
			String host = getHost(args);
			if (host != null) {
				String[] cont = { "-host", host, "-container", "cms:openjade.agent.CMS" };
				jade.Boot.main(cont);
			}
		}
	}

	private static boolean isMainContainer(String[] args) {
		for (String arg : args)
			if (arg.equals("-container"))
				return false;
		return true;
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
