package test;

import implementationVMI.AccionVMI;
import implementationVMI.EjemploVMI;
import implementationVMI.EstadoVMI;
import reinforcedLearning.model.QLearner;

public class TestVMI {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		EjemploVMI ejemplo = new EjemploVMI(3);
		QLearner<EjemploVMI, EstadoVMI, AccionVMI> ql = new QLearner<EjemploVMI, EstadoVMI,AccionVMI>(ejemplo, 0.9,0.05);
		ql.fitModelBySteps(5000, 10);
		
	}

}