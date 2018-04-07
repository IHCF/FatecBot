package model;

/**
 * 
 * Enumerador dos estados do chat
 */
public enum State {
	IS_NOTHING(0), IS_REGISTERING(1), IS_REGISTERING_USERNAME(2), IS_REGISTERING_PASSWORD(3), IS_SEARCHING(
			4), IS_RECOVERY_USER(5);

	private int STATE;

	private State(int value) {
		STATE = value;
	}

	public int getState() {
		return STATE;
	}
}
