package implementationVMI;

import reinforcedLearning.model.State;

public class EstadoVMI extends State {

	
	protected int ICD;
	protected int IP1;
	protected int IP2;
	
	public EstadoVMI(int ICD, int IP1, int IP2) {
		super(ICD+","+IP1+","+IP2);
		this.ICD = ICD;
		this.IP1 = IP1;
		this.IP2 = IP2;
		// TODO Auto-generated constructor stub
	}

}