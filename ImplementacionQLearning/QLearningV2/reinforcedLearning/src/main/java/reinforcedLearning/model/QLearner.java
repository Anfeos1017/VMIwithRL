package reinforcedLearning.model;

import java.io.IOException;
import java.util.HashMap;

import java.util.Random;

public class QLearner<M extends AbstractModel<S, A>, S extends State, A extends Action> {

	private M model;
	private double[][] Qtable;
	private double discountFactor;
	private double learningRate;
	private HashMap<String, Integer> stateMapping;
	private HashMap<String, Integer> actionMapping;

	public QLearner(M abstractModel, double discountFactor, double learningRate) {
		this.model = abstractModel;
		this.Qtable = new double[abstractModel.states.size()][abstractModel.actions.size()];
		this.discountFactor = discountFactor;
		this.learningRate=learningRate;
		stateMapping = new HashMap<String, Integer>();
		actionMapping = new HashMap<String, Integer>();

		int count = 0;
		for (A action : model.actions.values()) {
			this.actionMapping.put(action.name, count);
			count++;
		}
		count = 0;

		for (S state : model.states.values()) {
			this.stateMapping.put(state.name, count);
			count++;
		}
	}

	private A pickOptimalAction(S state, M model) {
		double max = Double.NEGATIVE_INFINITY;
		A picked = null;
		for (A action : model.allowedActions.get(state.name)) {
			if (this.Qtable[this.stateMapping.get(state.name)][this.actionMapping.get(action.name)] > max) {
				max = this.Qtable[this.stateMapping.get(state.name)][this.actionMapping.get(action.name)];
				picked = action;
			}
		}
		return picked;
	}
	
	
	public void printQTable() {
		for (S state : this.model.states.values()) {
			String toPrint=state.name;
			for (A action : this.model.allowedActions.get(state.name)) {
				toPrint+=" "+action.name+":"+this.Qtable[this.stateMapping.get(state.name)][this.actionMapping.get(action.name)];
			}
			System.out.println(toPrint);
		}
	}
	
	public double runPolicyTest() throws Exception {
		M model = (M) this.model.getInitialSystem();
		double cumReward = 0;
		boolean stoppedRun = false;
		while(!stoppedRun) {

			A nextAction = this.pickOptimalAction(model.currentState, model);

			if (nextAction != null) {
				double reward = model.getReward(model.currentState, nextAction);
				cumReward += reward;
				S nextState = model.getNextState(model.currentState, nextAction);
				//System.out.println(model.currentState.name+" + "+nextAction.name+" => "+nextState.name +" ; "+ cumReward+" ; "+this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping.get(nextAction.name)]);
				double max = Double.NEGATIVE_INFINITY;

				for (A action : model.allowedActions.get(nextState.name)) {
					if (this.Qtable[this.stateMapping.get(nextState.name)][this.actionMapping
							.get(action.name)] > max) {
						max = this.Qtable[this.stateMapping.get(nextState.name)][this.actionMapping
								.get(action.name)];

					}
				}

				double qaux = this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping
						.get(nextAction.name)];
				this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping
						.get(nextAction.name)] += this.learningRate * (reward + (this.discountFactor*(max - qaux)));

				model.setAsCurrentState(model.takeAction(model.currentState, nextAction));
				if (model.finalStates.containsKey(model.currentState.name)) {
					stoppedRun = true;
					// System.out.println("Reached final State on Step: "+step);
				}
			} else {
				stoppedRun = true;
				// System.out.println("Reached dead State on Step: "+step);
			}

		
		}
		return cumReward;
	}
	
	

	public void fitModel(int runs) throws Exception {
		Random rand = new Random();
		for (int i = 0; i < runs; i++) {
			
			double cumReward = 0;
			M model = (M) this.model.getInitialSystem();
			boolean stoppedRun = false;
			int totalStep = 0;
			int explotation=0;
			while (!stoppedRun) {
				// System.out.println(model.currentState.name);
				if (rand.nextDouble() <= ((i+1) / ((double) runs))) {
					// EXPLOTATION
					totalStep++;
					explotation++;
					A nextAction = this.pickOptimalAction(model.currentState, model);

					if (nextAction != null) {
						double reward = model.getReward(model.currentState, nextAction);
						cumReward += reward;
						S nextState = model.getNextState(model.currentState, nextAction);
						//System.out.println(model.currentState.name+" + "+nextAction.name+" => "+nextState.name +" ; "+ cumReward+" ; "+this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping.get(nextAction.name)]);
						double max = Double.NEGATIVE_INFINITY;

						for (A action : model.allowedActions.get(nextState.name)) {
							if (this.Qtable[this.stateMapping.get(nextState.name)][this.actionMapping
									.get(action.name)] > max) {
								max = this.Qtable[this.stateMapping.get(nextState.name)][this.actionMapping
										.get(action.name)];

							}
						}

						double qaux = this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping
								.get(nextAction.name)];
						this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping
								.get(nextAction.name)] += this.learningRate * (reward + (this.discountFactor*(max - qaux)));

						model.setAsCurrentState(model.takeAction(model.currentState, nextAction));
						if (model.finalStates.containsKey(model.currentState.name)) {
							stoppedRun = true;
							// System.out.println("Reached final State on Step: "+step);
						}
					} else {
						stoppedRun = true;
						// System.out.println("Reached dead State on Step: "+step);
					}

				} else {
					// EXPLORATION
					A nextAction = model.chooseRandomAction(model.currentState);
					totalStep++;
					if (nextAction != null) {
						double reward = model.getReward(model.currentState, nextAction);
						cumReward += reward;
						S nextState = model.getNextState(model.currentState, nextAction);
						//System.out.println(model.currentState.name+" + "+nextAction.name+" => "+nextState.name +" ; "+ cumReward+" ; "+this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping.get(nextAction.name)]);
						double max = Double.NEGATIVE_INFINITY;

						for (A action : model.allowedActions.get(nextState.name)) {
							if (this.Qtable[this.stateMapping.get(nextState.name)][this.actionMapping
									.get(action.name)] > max) {
								max = this.Qtable[this.stateMapping.get(nextState.name)][this.actionMapping
										.get(action.name)];

							}
						}

						double qaux = this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping
								.get(nextAction.name)];
						this.Qtable[this.stateMapping.get(model.currentState.name)][this.actionMapping
						                            								.get(nextAction.name)] += this.learningRate * (reward + (this.discountFactor*(max - qaux)));

						model.setAsCurrentState(model.takeAction(model.currentState, nextAction));
						if (model.finalStates.containsKey(model.currentState.name)) {
							stoppedRun = true;
							// System.out.println("Reached final State on Step: "+step);
						}
					} else {
						stoppedRun = true;
						// System.out.println("Reached dead State on Step: "+step);
					}

				}
				
			}
			System.out.println(i+"," + cumReward +","+(explotation/(double)totalStep));
			// System.out.println("Q(inicio): "+this.Qtable[0][0]);
			// System.out.println("Q(final):
			// "+this.Qtable[this.Qtable.length-1][this.Qtable[0].length-1]);
			//System.out.println((i)+" : "+ "Explotation:"+(explotation/(double)totalStep));
		}
		this.printQTable();
	}

}
