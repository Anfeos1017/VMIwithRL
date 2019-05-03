package test;

import java.util.HashMap;
import java.util.Random;

import model.LearningModel;
import model.QLearner;

public class TestModels {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Laberinto l=generarLaberinto(5,6);
		QLearner ql=new QLearner(l, 0.9);
		ql.fit(1000);
	}

	public static Laberinto generarLaberinto(int filas, int columnas) {

		Laberinto l =new Laberinto("0,0",10,10);
		l.imprimirLaberinto();
		return l;
	}

}
