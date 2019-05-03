package model;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import test.Laberinto;

public class QLearner {

	public LearningModel model;
	private double[][] Qtable;
	private double discountFactor;
	private HashMap<String, Integer> stateMapping;
	private HashMap<String, Integer> actionMapping;

	public QLearner(LearningModel m, double discountFactor) {
		this.model = m;
		this.Qtable = new double[m.states.size()][m.actions.size()];
		this.discountFactor = discountFactor;

		stateMapping = new HashMap<>();
		actionMapping = new HashMap<>();

		int count = 0;
		for (String action : model.actions) {
			this.actionMapping.put(action, count);
			count++;
		}
		count = 0;

		for (String state : model.states) {
			this.stateMapping.put(state, count);
			count++;
		}
	}

	private String pickRandomAction(HashMap<String, String> actionPool) {
		// Los keys son acciones, los values son estados
		int size = actionPool.keySet().size();
		int item = new Random().nextInt(size);
		int i = 0;
		String picked = null;
		for (String obj : actionPool.keySet()) {
			if (i == item)
				picked = obj;
			i++;
		}
		return picked;
	}

	private String pickOptimalAction(HashMap<String, String> actionPool) {
		double max = Double.NEGATIVE_INFINITY;
		String picked = null;
		for (Entry<String, String> entry : actionPool.entrySet()) {
			if (this.Qtable[this.stateMapping.get(entry.getValue())][this.actionMapping.get(entry.getKey())] > max) {
				max = this.Qtable[this.stateMapping.get(entry.getValue())][this.actionMapping.get(entry.getKey())];
				picked = entry.getKey();
			}
		}
		return picked;
	}

	private void printOptimaPolicy() {
		boolean stoppedRun = false;
		String currentState = this.model.initialState;
		while (!stoppedRun) {

			System.out.println(currentState);
			((Laberinto)this.model).imprimirLaberinto();
			// Tomar la accion mas favorable (Explotation)

			String nextAction = pickOptimalAction(this.model.transitionFunction.get(currentState));
			System.out.println(nextAction);
			System.out.println(this.model.getReward(currentState, nextAction));
			if (nextAction != null) {

				currentState = this.model.transitionFunction.get(currentState).get(nextAction);

				if (this.model.finalStates.contains(currentState)) {
					stoppedRun = true;
				}
			} else {
				stoppedRun = true;
			}

		}
	}

	public void fit(int runs) {
		Random rand = new Random();
		for (int i = 0; i < runs; i++) {
			System.out.println("Run # " + i);
			String currentState = model.initialState;

			boolean stoppedRun = false;

			while (!stoppedRun) {

				double prob = (double) i / (double) runs;

				if (rand.nextDouble() <= prob) {
					// Tomar la accion mas favorable (Explotation)

					String nextAction = pickOptimalAction(this.model.transitionFunction.get(currentState));
					if (nextAction != null) {
						double reward = this.model.getReward(currentState, nextAction);
						String nextState = this.model.transitionFunction.get(currentState).get(nextAction);

						double max = Double.NEGATIVE_INFINITY;

						for (String action : this.model.transitionFunction.get(nextState).keySet()) {
							if (this.Qtable[this.stateMapping.get(nextState)][this.actionMapping.get(action)] > max) {
								max = this.Qtable[this.stateMapping.get(nextState)][this.actionMapping.get(action)];

							}
						}

						double qaux = this.Qtable[this.stateMapping.get(currentState)][this.actionMapping
								.get(nextAction)];
						this.Qtable[this.stateMapping.get(currentState)][this.actionMapping
								.get(nextAction)] += this.discountFactor * (reward + max - qaux);
						currentState = this.model.transitionFunction.get(currentState).get(nextAction);
						this.model.notifyCurrentState(currentState);
						if (this.model.finalStates.contains(currentState)) {
							stoppedRun = true;
						}
					} else {
						stoppedRun = true;
					}

				} else {
					// Tomar una accion aleatoria (Exploration)

					String nextAction = pickRandomAction(this.model.transitionFunction.get(currentState));
					if (nextAction != null) {
						double reward = this.model.getReward(currentState, nextAction);
						String nextState = this.model.transitionFunction.get(currentState).get(nextAction);

						double max = Double.NEGATIVE_INFINITY;

						for (String action : this.model.transitionFunction.get(nextState).keySet()) {
							if (this.Qtable[this.stateMapping.get(nextState)][this.actionMapping.get(action)] > max) {
								max = this.Qtable[this.stateMapping.get(nextState)][this.actionMapping.get(action)];

							}
						}

						double qaux = this.Qtable[this.stateMapping.get(currentState)][this.actionMapping
								.get(nextAction)];
						this.Qtable[this.stateMapping.get(currentState)][this.actionMapping
								.get(nextAction)] += this.discountFactor * (reward + max - qaux);
						currentState = this.model.transitionFunction.get(currentState).get(nextAction);
						this.model.notifyCurrentState(currentState);
						if (this.model.finalStates.contains(currentState)) {
							stoppedRun = true;
						}
					} else {
						stoppedRun = true;
					}

				}
			}

		}
this.printOptimaPolicy();
	}

}
