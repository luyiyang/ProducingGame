import java.util.Random;

public class GameConfiguration implements GameConfig{
	private int stayEnergy;
	private int moveEnergy;
	private int foodEnergy;
	private int maxOrganismEnergy;
	private int maxFoodUnits;
	private Random rand;
	
	public GameConfiguration(){
		rand = new Random();
		stayEnergy = 1;
		moveEnergy = rand.nextInt(19)+2;//2-20
		foodEnergy = rand.nextInt(491)+10;//10-500
		maxOrganismEnergy = rand.nextInt(901)+100;//100-1000
		maxFoodUnits = rand.nextInt(41)+10;//10-50
	}
	
	
	@Override
	public int s() {
		// TODO Auto-generated method stub
		return stayEnergy;
	}

	@Override
	public int v() {
		// TODO Auto-generated method stub
		return moveEnergy;
	}

	@Override
	public int u() {
		// TODO Auto-generated method stub
		return foodEnergy;
	}

	@Override
	public int M() {
		// TODO Auto-generated method stub
		return maxOrganismEnergy;
	}

	@Override
	public int K() {
		// TODO Auto-generated method stub
		return maxFoodUnits;
	}

}
