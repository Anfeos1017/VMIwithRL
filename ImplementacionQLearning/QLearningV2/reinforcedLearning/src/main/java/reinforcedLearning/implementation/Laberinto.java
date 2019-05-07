package reinforcedLearning.implementation;

import java.util.HashMap;
import java.util.Random;

import reinforcedLearning.model.AbstractModel;

public class Laberinto extends AbstractModel<Estado, Accion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7768265787682708512L;
	protected int x;
	protected int y;
	protected double probBombas;
	protected double probPoderes;
	protected String[][] matrizLaberinto;

	public Laberinto(int x, int y, double probBombas, double probPoderes) throws Exception {
		super();
		this.x = x;
		this.y = y;
		this.probBombas = probBombas;
		this.probPoderes = probPoderes;
		// TODO Auto-generated constructor stub
		this.addAction(new Accion(Accion.ABAJO));
		this.addAction(new Accion(Accion.ARRIBA));
		this.addAction(new Accion(Accion.DERECHA));
		this.addAction(new Accion(Accion.IZQUIERDA));

		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				this.addState(new Estado(i, j));
			}
		}

		this.setAsCurrentState(
				this.getStateByName("0,0"));

		this.generarMatrizLaberinto();
		this.saveAsInitialState();
	}

	private void generarMatrizLaberinto() throws Exception {
		this.finalStates = new HashMap<String, Estado>();
		String[][] matriz = new String[x][y];
		Random rand = new Random();
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				if ((i == 0 && j == 0) || (i == matriz.length - 1 && j == matriz[0].length - 1)) {
					if ((i == 0 && j == 0)) {
						matriz[i][j] = "S";
					} else {
						matriz[i][j] = "E";
						this.setAsFinalState(this.states.get((i) + "," + (j)));
					}
				} else {
					if (rand.nextDouble() <= probBombas) {
						matriz[i][j] = "*";
						this.setAsFinalState(this.states.get((i) + "," + (j)));
					} else if (rand.nextDouble() <= this.probPoderes) {
						matriz[i][j] = "p";
					} else {
						matriz[i][j] = "b";
					}
				}
			}
		}

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				if (i == 0 && j == 0) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.DERECHA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ABAJO));
				} else if (i == 0 && j == matriz[0].length - 1) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.IZQUIERDA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ABAJO));

				} else if (j == 0 && i == matriz.length - 1) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.DERECHA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ARRIBA));

				} else if (i == matriz.length - 1 && j == matriz[0].length - 1) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.IZQUIERDA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ARRIBA));


				} else if (i == 0) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.IZQUIERDA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ABAJO));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.DERECHA));

				} else if (i == matriz.length - 1) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.IZQUIERDA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ARRIBA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.DERECHA));

				} else if (j == 0) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ARRIBA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.DERECHA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ABAJO));

				} else if (j == matriz[0].length - 1) {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ARRIBA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.IZQUIERDA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ABAJO));

				} else {
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.IZQUIERDA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ABAJO));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.DERECHA));
					this.setAllowedAction(getStateByName(i + "," + j), getActionByName(Accion.ARRIBA));

				}

			}
		}

		this.matrizLaberinto = matriz;
	}
	
	
	public void imprimirLaberinto() {
		for (int k = 0; k < this.matrizLaberinto.length; k++) {
			for (int l = 0; l < this.matrizLaberinto[0].length; l++) {
				System.out.print(this.matrizLaberinto[k][l] + " ");
			}
			System.out.println();
		}
	}
	

	@Override
	public double getReward(Estado state1, Accion action) {
		// TODO Auto-generated method stub
		Estado prox=this.getNextState(state1, action);
		String type=this.matrizLaberinto[prox.x][prox.y];
		if(type.equals("b")) {
			return -1;
		}else if(type.equals("*")) {
			return -100;
		}else if(type.equals("E")) {
			return 100;
		}else  if(type.equals("p")) {
			return 10;
		}else {
			return -1;
		}
	}
	
	

	@Override
	public Estado takeAction(Estado state, Accion action) throws Exception {
		// TODO Auto-generated method stub
		Estado proximo=getNextState(state,action);
		//System.out.println("Landing on ("+proximo.x+","+proximo.y+") ("+this.matrizLaberinto[proximo.x][proximo.y]+")");
		if(this.matrizLaberinto[proximo.x][proximo.y].equals("p")) {
			this.matrizLaberinto[proximo.x][proximo.y]="b";
		}
		this.setAsCurrentState(proximo);
		return proximo;
	}

	@Override
	public Estado getNextState(Estado state, Accion action) {
		// TODO Auto-generated method stub
		if(action.getName().equals(Accion.ABAJO)) {
			return this.getStateByName((state.x+1)+","+(state.y));
		}else if(action.getName().equals(Accion.ARRIBA)) {
			return this.getStateByName((state.x-1)+","+(state.y));
		}else if(action.getName().equals(Accion.DERECHA)) {
			return this.getStateByName((state.x)+","+(state.y+1));
		}else {
			return this.getStateByName((state.x)+","+(state.y-1));
		}
	}

}
