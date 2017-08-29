package ufrn.ia.fuzzy;

import static ufrn.ia.fuzzy.gui.FuzzyScenesManager.launchApplication;

public class Fuzzy implements Runner {

	private static Fuzzy fuzzy;

	private boolean running;

	private Fuzzy() {
		super();
	}

	public void startup() {

		initializeOptions();

		launchApplication();

	}

	private void initializeOptions() {

	}

	public void start() {

		if (!running) {

			running = !running;
//			dashboardRunner.start();
		}
	}

	public void stop() {

		if (running) {

			running = !running;
//			dashboardRunner.stop();
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public static Fuzzy fuzzy() {
		return fuzzy;
	}

	public static void run() {

		if (fuzzy == null) {
			fuzzy = new Fuzzy();
		}

		fuzzy.startup();

		fuzzy.stop();
	}

    public static void main(String[] args) {

    	run();

    	System.exit(0);
    }

	public static Fuzzy initFuzzy() {
		if (fuzzy == null) {
			fuzzy = new Fuzzy();
		}
		return fuzzy;
	}

}
