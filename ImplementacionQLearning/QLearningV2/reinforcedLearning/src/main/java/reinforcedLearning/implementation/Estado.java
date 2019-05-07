package reinforcedLearning.implementation;

import reinforcedLearning.model.State;

public class Estado extends State{
	
	
	protected int x;
	protected int y;

	public Estado(int x, int y) {
		super(x+","+y);
		this.x=x;
		this.y=y;
		// TODO Auto-generated constructor stub
	}

}
