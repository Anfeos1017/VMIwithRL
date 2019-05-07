package reinforcedLearning.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public abstract class AbstractModel<S extends State, A extends Action> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6793328915188482296L;
	protected Map<String, S> states;
	protected Map<String, A> actions;
	protected Map<String, S> finalStates;
	protected S currentState;
	protected Map<String, Set<A>> allowedActions;
	private String objectInitialState;

	public AbstractModel() {
		this.allowedActions = new HashMap<String, Set<A>>();
		states = new HashMap<String, S>();
		actions = new HashMap<String, A>();
		finalStates = new HashMap<String, S>();

	}

	public void setAllowedAction(S state, A action) throws Exception {
		if (this.states.containsKey(state.name)) {
			if (this.actions.containsKey(action.name)) {
				if (this.allowedActions.containsKey(state.name)) {
					this.allowedActions.get(state.name).add(action);
				} else {
					this.allowedActions.put(state.name, new HashSet<A>());
					this.allowedActions.get(state.name).add(action);
				}
			} else {
				throw new Exception(
						"Action with name " + action.name + " does not exist in the current AbstractModel.");
			}
		} else {
			throw new Exception("State with name " + state.name + " does not exist in the current AbstractModel.");
		}
	}
	
	
	public S getStateByName(String name) {
		return this.states.get(name);
	}
	
	public A getActionByName(String name) {
		return this.actions.get(name);
	}
	
	public abstract S getNextState(S state, A action);

	public A chooseRandomAction(S state) throws Exception {

		if (this.states.containsKey(state.name)) {

			if (this.allowedActions.containsKey(state.name)) {
				int size = this.allowedActions.get(state.name).size();
				int item = new Random().nextInt(size);
				int i = 0;
				A picked = null;
				for (A act : this.allowedActions.get(state.name)) {
					if (i == item)
						picked = act;
					i++;
				}
				return picked;
			} else {
				return null;
			}

		} else {
			throw new Exception("State with name " + state.name + " does not exist in the current AbstractModel.");
		}

	}

	public void addState(S state) {
		this.states.put(state.name, state);
	}

	public void addAction(A action) {
		this.actions.put(action.name, action);
	}

	public void setAsCurrentState(S state) throws Exception {
		if (this.states.containsKey(
				state.name)) {
			this.currentState = state;
		} else {
			throw new Exception("State with name " + state.name + " does not exist in the current AbstractModel.");
		}
	}

	public void setAsFinalState(S state) throws Exception {
		if (this.states.containsKey(state.name)) {
			this.finalStates.put(state.name, state);
		} else {
			throw new Exception("State with name " + state.name + " does not exist in the current AbstractModel.");
		}
	}

	public abstract S takeAction(S state, A action) throws Exception;

	public abstract double getReward(S state1, A action);

	public void saveAsInitialState() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		oos.close();
		this.objectInitialState = Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	public Object getInitialSystem() throws IOException, ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(this.objectInitialState);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
