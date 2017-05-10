import java.util.Random;

/**
 * This is the computer player, it will have a strategy to move.
 * @author LuyiYang
 *
 */
public class ComputerPlayer implements Player{

	private String name;
	private GameConfig game;
	private int state;
	private Random rand;

	/**
	 * This is the setter method of the computer player
	 * @param name
	 */
	public ComputerPlayer(String name){
		this.name = name;
		rand = new Random();
	}


	@Override
	public void register(GameConfig game, int key) {
		// TODO Auto-generated method stub
		this.game = game;
		state = key;

	}

	@Override
	public String name() {
		return name;
	}

	/**
	 * The computer player has a strategy to move:
	 * 
	 * If the cell it currently sits on has food, 
	 * the computer will stay put and consume the food until the food runs out.
	 * 
	 * If not the above case and if the computer player has a neighbor cell that has food, 
	 * it will first moves to the cell that has food.
	 * 
	 * If neither is the case above, 
	 * the computer will reproduce to an empty cell until it has half of its energy.
	 * 
	 * If the player has less than half of its energy or else other situations, it will stay put.
	 */
	@Override
	public Move move(boolean[] food, int[] neighbors, int foodleft, int energyleft) {

		if(foodleft != 0){
			Move m = new Move(Constants.STAYPUT);
			return m;
		}

		for(int i = 1; i<food.length; i++){
			if(food[i] == true && neighbors[i]== -1){
				Move m = new Move(i);
				return m;
			}
		}

		if(energyleft > 2*state/3){
			for(int i = 1; i<neighbors.length; i++){
				if(neighbors[i] == -1){
					Move m = new Move(Constants.REPRODUCE, i, energyleft);
					return m;
				}
			}
		}

		else if(energyleft <= state/2){
			for(int i = 1; i<neighbors.length; i++){
				if(neighbors[i] == -1){
					Move m = new Move(i);
					return m;
				}
			}
		}

		return new Move(Constants.STAYPUT);
	}


}
