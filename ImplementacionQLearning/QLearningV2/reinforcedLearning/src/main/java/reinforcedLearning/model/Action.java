package reinforcedLearning.model;

import java.io.Serializable;

public  abstract class Action implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1421153532332195214L;
	protected String name;
	
	
	public Action(String name) {
		this.name=name;
	}
	
	
	public String getName() {
		return this.name;
	}
}
