import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
/**
 * This is a human player, it will move according to the instructions the player types on console
 * @author LuyiYang
 *
 */
public class HumanPlayer implements Player{

	private String name;
	private GameConfig game;
	private int state;
	private Random rand;

	public HumanPlayer(String name){
		this.name = name;
	}



	@Override
	public void register(GameConfig game, int key) {
		this.game = game;
		state = key;
		rand = new Random();
	}


	@Override
	public String name() {
		return name;
	}

	@Override
	public Move move(boolean[] food, int[] neighbors, int foodleft, int energyleft){

		System.out.printf("The food situations are (west, east, north, south): %b, %b, %b, %b \n", food[0], food[1], food[2], food[3]);
		System.out.printf("The organism situations are (west, east, north, south), %d, %d, %d, %d \n", neighbors[1], neighbors[2], neighbors[3], neighbors[4]);
		System.out.printf("The amount of remaining food on the organism's current cell: %d \n", foodleft);
		System.out.printf("The amount of energy currently possessed by the organism: %d \n", energyleft);


		Move m = null;
		Scanner in = new Scanner(System.in);
		System.out.println("Where do you want your organism to move?");
		System.out.println("Enter 0 to indicate stayput; 1-4(west, east, north, sorth) to indicate move; 5 to indicate reproduce.");

		int direction;
		try{
			direction = in.nextInt();


			if(direction == Constants.STAYPUT){
				m = new Move(Constants.STAYPUT);
			}
			else if(direction == Constants.WEST){
				m = new Move(Constants.WEST);
			}
			else if(direction == Constants.EAST){
				m = new Move(Constants.EAST);
			}
			else if(direction == Constants.SOUTH){
				m = new Move(Constants.SOUTH);
			}
			else if(direction == Constants.NORTH){
				m = new Move(Constants.NORTH);
			}


			else {
				System.out.println("Where do you want to put your child? Enter 1-4(west, east, north sorth)");	

				try{
					direction = in.nextInt();

					if (direction == 1)
						m = new Move(Constants.REPRODUCE, Constants.WEST, state);
					else if (direction == 2)
						m = new Move(Constants.REPRODUCE, Constants.EAST, state);
					else if (direction == 3)
						m = new Move(Constants.REPRODUCE, Constants.NORTH, state);
					else
						m = new Move(Constants.REPRODUCE, Constants.SOUTH, state);
				}catch(InputMismatchException i){
					System.out.println("Ooooops...WRONG INPUT!! Please an integer from 0 to 5");
				}

			}

		}catch(InputMismatchException i){
			System.out.println("Ooooops...WRONG INPUT!! Please an integer from 0 to 5");
		}

		return m;

		//								Move m = null; // placeholder for return value
		//						
		//								// this player selects randomly
		//								int direction = rand.nextInt(6);
		//						
		//								switch (direction) {
		//								case 0:
		//									m = new Move(Constants.STAYPUT);
		//									break;
		//								case 1:
		//									m = new Move(Constants.WEST);
		//									break;
		//								case 2:
		//									m = new Move(Constants.EAST);
		//									break;
		//								case 3:
		//									m = new Move(Constants.NORTH);
		//									break;
		//								case 4:
		//									m = new Move(Constants.SOUTH);
		//									break;
		//								case 5:
		//									direction = rand.nextInt(4);
		//									
		//									if (direction == 0)
		//										m = new Move(Constants.REPRODUCE, Constants.WEST, state);
		//									else if (direction == 1)
		//										m = new Move(Constants.REPRODUCE, Constants.EAST, state);
		//									else if (direction == 2)
		//										m = new Move(Constants.REPRODUCE, Constants.NORTH, state);
		//									else
		//										m = new Move(Constants.REPRODUCE, Constants.SOUTH, state);
		//								}
		//								return m;
	}


}
