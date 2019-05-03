package model;


import java.util.HashMap;
import java.util.HashSet;

public abstract class LearningModel {

	protected HashSet<String> states;
	protected HashSet<String> actions;
	protected String initialState;
	protected HashSet<String> finalStates;
	protected HashMap<String,HashMap<String,String>>transitionFunction;
	
	
	public LearningModel(String initialState) {
		states=new HashSet<String>();
		actions=new HashSet<String>();
		finalStates=new HashSet<String>();
		transitionFunction=new HashMap<String,HashMap<String,String>>();
		registerState(initialState);
		this.initialState=initialState;
		
	}
	
	
	public void registerStateActionTransition(String state1, String action, String state2) {
		if(!states.contains(state1)) {
			this.states.add(state1);
		}
		if(!states.contains(state2)) {
			this.states.add(state2);
		}
		if(!actions.contains(action)) {
			this.actions.add(action);
		}
		
		if(this.transitionFunction.containsKey(state1)) {
			this.transitionFunction.get(state1).put(action,state2);
		}else {
			this.transitionFunction.put(state1, new HashMap<String,String>());
			this.transitionFunction.get(state1).put(action,state2);
		}
		
	}
	
	
	
	public void registerState(String state) {
		this.states.add(state);
	}
	
	public void registerAction(String action) {
		this.actions.add(action);
	}
	
	public void addToFinalStates(String state) {
		if(!states.contains(state)) {
			registerState(state);
		}
		this.finalStates.add(state);
	}
	
	
	public abstract double getReward(String state, String action);
	public abstract void notifyCurrentState(String state);
	
}
