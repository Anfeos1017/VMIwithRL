package reinforcedLearning.model;

import java.io.Serializable;

public abstract class State implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2348733065476545861L;
	protected String name;
	
	public State(String name) {
		this.name=name;

	}
	
	public String getName() {
		return this.name;
	}

}
