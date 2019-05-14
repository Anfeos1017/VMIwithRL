package test;



import reinforcedLearning.implementation.Accion;
import reinforcedLearning.implementation.Estado;
import reinforcedLearning.implementation.Laberinto;
import reinforcedLearning.model.QLearner;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Laberinto l=new Laberinto(10, 10, 0.05, 0.15);
		QLearner<Laberinto, Estado, Accion> ql=new QLearner<Laberinto, Estado, Accion>(l, 0.9,0.05);
		ql.fitModel(1000);
		l.imprimirLaberinto();
		System.out.println("HOLAAAAAAAAA");
	}

}
