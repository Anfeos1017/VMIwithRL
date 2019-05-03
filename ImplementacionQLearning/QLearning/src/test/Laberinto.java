package test;

import java.util.HashMap;
import java.util.Random;

import model.LearningModel;

public class Laberinto extends LearningModel {

	public Laberinto(String initialState, int filas, int columnas) {
		super(initialState);
		initialize(filas, columnas);
		// TODO Auto-generated constructor stub
	}

	private HashMap<String, String> cellTypes;
	private String[][] table;

	public void initialize(int filas, int columnas) {
		cellTypes = new HashMap<>();
		table = new String[filas][columnas];
		Random rand = new Random();
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				if (i == 0 && j == 0) {
					this.registerStateActionTransition(i + "," + j, "right", (i) + "," + (j + 1));
					this.registerStateActionTransition(i + "," + j, "down", (i + 1) + "," + (j));
				} else if (i == 0 && j == columnas - 1) {
					this.registerStateActionTransition(i + "," + j, "left", (i) + "," + (j - 1));
					this.registerStateActionTransition(i + "," + j, "down", (i + 1) + "," + (j));
				} else if (j == 0 && i == filas - 1) {
					this.registerStateActionTransition(i + "," + j, "right", (i) + "," + (j + 1));
					this.registerStateActionTransition(i + "," + j, "up", (i - 1) + "," + (j));
				} else if (i == filas - 1 && j == columnas - 1) {
					this.registerStateActionTransition(i + "," + j, "left", (i) + "," + (j - 1));
					this.registerStateActionTransition(i + "," + j, "up", (i - 1) + "," + (j));
					this.finalStates.add(i + "," + j);
				} else if (i == 0) {
					this.registerStateActionTransition(i + "," + j, "left", (i) + "," + (j - 1));
					this.registerStateActionTransition(i + "," + j, "down", (i + 1) + "," + (j));
					this.registerStateActionTransition(i + "," + j, "right", (i) + "," + (j + 1));
				} else if (i == filas - 1) {
					this.registerStateActionTransition(i + "," + j, "left", (i) + "," + (j - 1));
					this.registerStateActionTransition(i + "," + j, "up", (i - 1) + "," + (j));
					this.registerStateActionTransition(i + "," + j, "right", (i) + "," + (j + 1));
				} else if (j == 0) {
					this.registerStateActionTransition(i + "," + j, "up", (i - 1) + "," + (j));
					this.registerStateActionTransition(i + "," + j, "right", (i) + "," + (j + 1));
					this.registerStateActionTransition(i + "," + j, "down", (i + 1) + "," + (j));
				} else if (j == columnas - 1) {
					this.registerStateActionTransition(i + "," + j, "up", (i - 1) + "," + (j));
					this.registerStateActionTransition(i + "," + j, "left", (i) + "," + (j - 1));
					this.registerStateActionTransition(i + "," + j, "down", (i + 1) + "," + (j));
				} else {
					this.registerStateActionTransition(i + "," + j, "up", (i - 1) + "," + (j));
					this.registerStateActionTransition(i + "," + j, "left", (i) + "," + (j - 1));
					this.registerStateActionTransition(i + "," + j, "down", (i + 1) + "," + (j));
					this.registerStateActionTransition(i + "," + j, "right", (i) + "," + (j + 1));
				}
				if ((i != 0 || j != 0) && (j != columnas - 1 || i != filas - 1)) {
					if (rand.nextDouble() <= 0.2) {
						if (rand.nextDouble() <= 0.5) {
							cellTypes.put(i + "," + j, "bomb");
							this.finalStates.add(i + "," + j);
							this.table[i][j] = "*";
						} else {
							cellTypes.put(i + "," + j, "power");
							this.table[i][j] = "p";
						}
					} else {
						cellTypes.put(i + "," + j, "blank");
						this.table[i][j] = "b";
					}
				}

				if (i == filas - 1 && j == columnas - 1) {
					cellTypes.put(i + "," + j, "end");
					this.table[i][j] = "E";
				}
				
				if (i ==0 && j ==0) {
					cellTypes.put(i + "," + j, "start");
					this.table[i][j] = "S";
				}

			}
		}

	}

	@Override
	public double getReward(String state, String action) {
		// TODO Auto-generated method stub
		String nextState = this.transitionFunction.get(state).get(action);
		
		if (this.cellTypes.get(nextState).equals("bomb")) {
			return -100 - 1;
		} else if (this.cellTypes.get(nextState).equals("power")) {
			return 10 - 1;
		} else if (this.cellTypes.get(nextState).equals("end")) {
			return 100 - 1;
		} else {
			return -1;
		}

	}

	public void imprimirLaberinto() {
		for (int k = 0; k < table.length; k++) {
			for (int l = 0; l < table[0].length; l++) {
				System.out.print(table[k][l] + " ");
			}
			System.out.println();
		}
	}
	
	
	

	@Override
	public void notifyCurrentState(String state) {
		// TODO Auto-generated method stub
		if(this.cellTypes.get(state).equals("power")) {
			this.cellTypes.put(state, "blank");
			
		}
	}
}
