import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 * This is the main method of the game.
 * It will run the game and print out the results on console.
 * @author LuyiYang
 *
 */
public class OrganismGameTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("What is the name of your human player?");
		Scanner in = new Scanner(System.in);
		final String humanName = in.nextLine();
		
		System.out.println("What is the name of your computer player?");
		final String computerName = in.nextLine();
	
		GameConfiguration gc = new GameConfiguration();
		Board b = new Board(10, gc);
		OrganismGame og = new OrganismGame(b, humanName, computerName);
		ArrayList<Player> players = new ArrayList<>();

		System.out.println();
				
		//human player register
		HumanPlayer human = new HumanPlayer(humanName);
		//HumanPlayer human = new HumanPlayer();
		human.register(gc, 500);
		//human.setName(humanName);
				
		//computer player register
		ComputerPlayer computer = new ComputerPlayer(computerName);
		//ComputerPlayer computer = new ComputerPlayer();
		computer.register(gc, 500);
		//computer.setName(computerName);

		//the position of the first player is randomly generated
		Random rand = new Random();
		int row = rand.nextInt(b.getBoardSize());
		int col = rand.nextInt(b.getBoardSize());

		
		//add occupied cell to an array list
		og.getParentCell().add(b.getBoard()[row][col]);
		//board needs to know which cell the player is on
		b.setPlayer(b.getBoard()[row][col], human, 500);
		og.getData().get(human.name()).addPlayer(human);
		og.getData().get(human.name()).addCell(b.getBoard()[row][col]);
		players.add(human);
		og.getTypeMap().put(humanName, human);


		//if the computer player's position is occupied, re-generates its position 
		do{
			row = rand.nextInt(b.getBoardSize());
			col = rand.nextInt(b.getBoardSize());
		}while(b.getBoard()[row][col].hasPlayer());
		
		//add occupied cell to an array list
		og.getParentCell().add(b.getBoard()[row][col]);
		//board needs to know which cell the player is on
		b.setPlayer(b.getBoard()[row][col], computer, 500);
		og.getData().get(human.name()).addPlayer(computer);
		og.getData().get(computer.name()).addCell(b.getBoard()[row][col]);
		players.add(computer);
		og.getTypeMap().put(computerName, computer);
		
		double foodBlowIn = (double)(rand.nextInt(1000)+1)/10000;
		double foodDoubling = (double)2*(rand.nextInt(1000)+1)/10000;
		og.initialize(gc, foodBlowIn, foodDoubling, players);

		System.out.println("----------------------------------------");
		System.out.println("Print Configurations: ");
		System.out.printf("The energy consumed in staying put: %d \n", gc.s());
		System.out.printf("Energy consumed when moving or producing: %d \n", gc.v());
		System.out.printf("Food energy per unit: %d \n", gc.u());
		System.out.printf("The maximum energy per organisms: %d \n", gc.M());
		System.out.printf("The maximum food units per cell: %d \n", gc.K());
		System.out.println("----------------------------------------");
		System.out.println();
		
		og.playGame();

		System.out.println("========================================");

//		System.out.printf("%s energy: %d, count: %d \n", humanName, og.getResults().get(0).getEnergy(), og.getResults().get(0).getCount());
//
//		System.out.printf("%s energy: %d, count: %d \n", computerName, og.getResults().get(1).getEnergy(), og.getResults().get(1).getCount());

		System.out.printf("%s energy: %d, count: %d \n", humanName, og.getResults().get(0).getEnergy(), og.getResults().get(0).getCount());

		System.out.printf("%s energy: %d, count: %d \n", computerName, og.getResults().get(1).getEnergy(), og.getResults().get(1).getCount());

		System.out.println("The game ends. ");

		if(og.getResults().get(0).getEnergy() > og.getResults().get(1).getEnergy()){
			System.out.println("Human wins");

		}else if(og.getResults().get(0).getEnergy() < og.getResults().get(1).getEnergy()){
			System.out.println("Computer wins");
		}
		else{
			System.out.println("Game ties");
		}	


		System.out.printf("Total Player: %d \n", og.getPlayer().size()) ;
		System.out.println();
	}

}
