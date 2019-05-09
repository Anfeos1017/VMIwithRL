package implementationVMI;

import java.util.Random;

import reinforcedLearning.model.AbstractModel;


public class EjemploVMI  extends AbstractModel<EstadoVMI, AccionVMI> {
	
	
	/**
 * 
 */
private static final long serialVersionUID = -6761689984138693132L;
	//Castigo por faltantes
	private static final int FALTANTES = -5;
	//Castigo por inventarios
	private static final int INVENTARIO = -1;
	private static final int EXCESOINV = -8;

	
	protected int capacidadMax;
	
	protected int A;
	
	private Random random;
	private int demanda1;
	private int demanda2;

	
	public EjemploVMI(int capacidadMax) throws Exception {
		super();
		
		this.capacidadMax= capacidadMax;
		random = new Random();
		demanda1 = 0;
		demanda2 = 0;
		
		for(int i = 0; i<capacidadMax+1; i++)
		{
			for(int j = 0; j<capacidadMax+1;j++)
			{
				this.addAction(new AccionVMI(i,j));
			}
		}
	
		for(int i = 0; i<capacidadMax+1; i++)
		{
			for(int j = 0; j<capacidadMax+3;j++)
			{
				for(int y = 0; y<capacidadMax+1;y++)
					{
						EstadoVMI estado =  new EstadoVMI(y,i,j);

						this.addState( new EstadoVMI(y,i,j));

						for(int s1 = 0; s1<capacidadMax+1;s1++)
						{
							{
								for(int s2 = 0; s2<capacidadMax+1;s2++) {
									AccionVMI act = getActionByName(s1+","+s2);
									if(estado.ICD> act.total || estado.ICD == act.total)
									{
										this.setAllowedAction(getStateByName(y+","+i+","+j), getActionByName(s1+","+s2));

									}
									
		
								}
							}
							
						}
					}
			}
		}
	

		
		currentState = null;
		EstadoVMI s = states.get(3+","+1+","+2);
		System.out.println(s.ICD);
		this.setAsCurrentState(this.getStateByName("3,1,2"));
		this.saveAsInitialState();

	}



	@Override
	public double getReward(EstadoVMI state1, AccionVMI action) {
		// TODO Auto-generated method stub
		 demanda1 = random.nextInt(capacidadMax+1);
		 demanda2 = random.nextInt(capacidadMax+1);

		int faltantes1 = 0; 
		int faltantes2 = 0;
		
		//Si la demanda es mayor a lo que hay en inventario y lo que envía el CD
		if(demanda1 > action.sent1 + state1.IP1)
		{
			faltantes1 = demanda1-action.sent1-state1.IP1;
		}
		
		if(demanda2 > action.sent2 + state1.IP2)
		{
			faltantes2 = demanda2-action.sent2 - state1.IP2;
		}
		
		int nuevoInv1 = 0;
		int nuevoInv2 = 0;
		
		int excesoInventario1 = 0;
		int excesoInventario2 = 0;

		// Si la demanda es menor a lo que hay en inventario y lo que se envía del CD
		if(action.sent1+ state1.IP1 - demanda1 > -1)
		{
			nuevoInv1 = action.sent1 + state1.IP1 - demanda1;
			if(nuevoInv1 > capacidadMax)
			{
				excesoInventario1 = nuevoInv1 -capacidadMax;
			}
		}
		if(action.sent2+ state1.IP2 - demanda2 > -1)
		{
			nuevoInv2 = action.sent2 + state1.IP2 - demanda2;
			if(nuevoInv2 > capacidadMax)
			{
				excesoInventario2 = nuevoInv2 -capacidadMax;
			}
		}
		
		double reward = ((faltantes1 + faltantes2)*FALTANTES) + ((nuevoInv1+ nuevoInv2)*INVENTARIO) + ((excesoInventario1+excesoInventario2)*EXCESOINV);
	
		return reward;
	}
	
	

	@Override
	public EstadoVMI takeAction(EstadoVMI state, AccionVMI action) throws Exception {
		
		EstadoVMI state2 = getNextState(state, action);
		this.setAsCurrentState(state2);
		
		return state2;
	}

	@Override
	public EstadoVMI getNextState(EstadoVMI state, AccionVMI action) {
		
		A = random.nextInt(capacidadMax+1);
		int nuevoInvCD = state.ICD - action.total + A;
		//if (nuevoInvCD<0)
		//{
			//nuevoInvCD = 0;
		//}
	
		int nuevoInv1 = 0;
		int nuevoInv2 = 0;

		// Si la demanda es menor a lo que hay en inventario y lo que se envía del CD
		if(action.sent1+state.IP1-demanda1 > -1)
		{
			nuevoInv1 = action.sent1 + state.IP1 - demanda1;
		}
		if(action.sent2+state.IP2 -demanda2> -1)
		{
			nuevoInv2 = action.sent2 + state.IP2 - demanda2;
		}
		
		if(nuevoInv1 >capacidadMax)
		{
			nuevoInv1 = capacidadMax;
		}
		
		
		///¿QUE PASA SI LO QUE SE ENVIA ES MAYOR A LO QUE CABE???
		
		if(nuevoInv2 >capacidadMax)
		{
			nuevoInv2 = capacidadMax;
		}
		
		if(nuevoInvCD >capacidadMax)
		{
			nuevoInvCD = capacidadMax;
		}
		
		EstadoVMI state2 = getStateByName(nuevoInvCD+","+nuevoInv1+","+nuevoInv2);


		return state2;
		
		
	}


}