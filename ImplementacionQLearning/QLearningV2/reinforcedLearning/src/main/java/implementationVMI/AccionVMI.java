package implementationVMI;

import reinforcedLearning.model.Action;

public class AccionVMI extends Action {

	
	protected int sent1;
	protected int sent2;
	protected int total;
	
	public AccionVMI(int sent1, int sent2) {
		super(sent1+","+sent2);
		this.sent1 = sent1;
		this.sent2 = sent2;
		this.total = sent1 + sent2;
	}

}