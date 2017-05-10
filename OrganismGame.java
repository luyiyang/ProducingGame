import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
/**
 * This class implements OrganismGameInterface
 * 
 * It is able to deal with a HumanPlayer and a ComputerPlayer competing with each other.
 * (This is the situation which the OrganismGameTester class tests.)
 * 
 * It is able to deal with two strategic players(one ComputerPlayer, one OtherPlayer) 
 * competing with each other, as well as two HumanPlayers competing with each other.
 * 
 * @author LuyiYang
 *
 */
public class OrganismGame implements OrganismsGameInterface{

	private Board b;
	private GameConfiguration gameConfigure;
	private double p;
	private double q;
	private ArrayList<Player> players;
	private int round;
	private int startEnergy;
	HashMap<String, DataTracker> dataMap = new HashMap<>();
	HashMap<String, Player> typeMap = new HashMap<>();
	private ArrayList<Cell> parentCells;
	private ArrayList<Cell> childCells;
	private String name1;
	private String name2;

	/**
	 * This is the constructor
	 * @param b, board the game is going to be used
	 */
	public OrganismGame(Board b, String s1, String s2){
		this.b = b;
		parentCells = new ArrayList<Cell>();
		childCells =new ArrayList<Cell>();
		DataTracker tracker1 = new DataTracker();
		dataMap.put(s1, tracker1);
		name1 = s1;
		DataTracker tracker2 = new DataTracker();
		dataMap.put(s2, tracker2);
		name2 = s2;
	}
	
	/**
	 * This is the getter method of typeMape
	 * @return
	 */
	public HashMap<String, Player> getTypeMap() {
		return typeMap;
	}


	/**
	 * This is the get method of the list of players involved in the game
	 * @return, the array list of players
	 */
	public ArrayList<Player> getPlayer() {
		return players;
	}

	
	/**
	 * This will get the start energy of the player
	 * @return an int, the start energy of the player
	 */
	public int getStartEnergy(){
		return startEnergy;
	}

	
	/**
	 * This is the getter method of parent cell
	 * @return ArrayList<Cell>
	 */
	public ArrayList<Cell> getParentCell(){
		return parentCells;
	}

	/**
	 * This is the getter method of child cell
	 * @return ArrayList<Cell>
	 */
	public ArrayList<Cell> getChildCell(){
		return childCells;
	}

	/**
	 * This is the getter method of data tracker
	 * @return ArrayList<DataTracker>
	 */
	public HashMap<String, DataTracker> getData(){
		return dataMap;
	}

	/**
	 * This method will make a judgement to see whether the move is a legal move
	 * A move is a legal move:
	 * if the board is not full; 
	 * if the destination cell is not occupied; 
	 * if the player has enough energy to move or produce; 
	 * @param r, current row
	 * @param c, current col
	 * @param m, move
	 * @param energyLeft
	 * @return true, if it is a legal move; false, if not.
	 */
	public boolean isLegalMove(int r, int c, Move m, int energyLeft){
		if(players.size()<=b.getBoardSize()*b.getBoardSize() && !b.hasPlayerAtDestination(r, c, m) && energyLeft>= gameConfigure.v()){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * This method will deal with the died organisms.
	 * It will update the die player's cell, array list of players, data tracker, etc., and 
	 * either remove it from the list or clear the cell it occupies
	 */
	private void removeDiedOrganism() {
		for(Cell cell: new ArrayList<Cell>(parentCells)){
			Player plr = cell.getPlayer();
			if(cell.getEnergy() == 0){
				parentCells.remove(cell);
				players.remove(plr);
				dataMap.get(plr.name()).removeCell(cell);;
				cell.setPlayer(null, 0);
			}


		}
	}



	@Override
	/**
	 * This method will initialize the game.
	 * Each game will run for 5000 rounds.
	 * Each player will start with an energy of 500 at the start of the game.
	 * @param game the GameConfig to run
	 * @param p the secret parameter p - probability of spontaneous appearance of food
	 * @param q the secret parameter q - probability of food doubling
	 * @param players the list of players
	 */
	public void initialize(GameConfig game, double p, double q, ArrayList<Player> players) {
		gameConfigure = (GameConfiguration) game;
		this.p = p;
		this.q = q;
		this.players = players;
		round = 5000;
		startEnergy = 500;
	}


	@Override
	/**
	 * This method will play the game for the given configuration
	 * @return true if the game ended normally, false if exceptions were thrown or unexpected behavior
	 */
	public boolean playGame() {


		b.printBoard();
		System.out.println();

		ArrayList<Cell> tempCellArray = new ArrayList<Cell>();

		for(int i = 1; i<round; i++){

			Collections.sort(parentCells);

			System.out.println("========================================");
			System.out.println();
			System.out.printf("Round: %d \n", i);
			System.out.println();

			for(Cell cell: parentCells){	

				Player plr = cell.getPlayer();

				int r = cell.getRow();
				int c = cell.getCol();

				System.out.printf("Organism on row %d, col %d (0-indexed): \n", r, c);

				if(cell.hasFood()){
					cell.consumeFood(gameConfigure.u());
					int foodLeft = cell.getFood()-1;
					cell.setFood(foodLeft);
				}

				int energyLeft = cell.getEnergy();

				try{
					Move m = plr.move(b.localFood(r, c), b.localNeighbors(r, c), cell.getFood(), energyLeft);

					if(isLegalMove(r, c, m, energyLeft)){

						System.out.printf("%s.....\n", m.toString());
						System.out.println("----------------------------------------");

						cell.updateEnergy(m);


						if(m.type!=Constants.REPRODUCE){	

							//get next cell
							Cell nextCell = b.destinationCell(r, c, m);
							b.setPlayer(nextCell, plr, cell.getEnergy());

							dataMap.get(plr.name()).removeCell(cell);
							dataMap.get(plr.name()).addCell(nextCell);
							tempCellArray.add(nextCell);	
							//clear current cell
							b.getBoard()[r][c].setPlayer(null, 0);

						}else{

							tempCellArray.add(cell);

							Player child = null;

							child = typeMap.get(plr.name());

							child.register(gameConfigure,cell.getEnergy());


							//board needs to know where the child is
							Cell nextCell = b.destinationCell(r, c, m); 
							b.setPlayer(nextCell, child, cell.getEnergy());
							//update data tracker
							dataMap.get(plr.name()).addCell(nextCell);
							//add child to the list of players
							players.add(child);
							//add child to the list of child cells
							childCells.add(nextCell);
						}
					}
					//If the move is not a legal move or the player choose to stay put, stay put
					else{

						m = new Move(Constants.STAYPUT);
						System.out.printf("%s.....\n", m.toString());
						System.out.println("----------------------------------------");
						cell.updateEnergy(m);
						tempCellArray.add(cell);
					}
				}catch(NullPointerException n){
					tempCellArray.add(cell);
					System.out.println("Because of the wrong input you have to skip this turn :( ");
				}

			}

			parentCells.clear();
			parentCells.addAll(tempCellArray);
			parentCells.addAll(childCells);


			//remove died cells
			removeDiedOrganism();

			childCells.clear();
			tempCellArray.clear();



			//Deal with Food
			b.dealWithFood(p, q, gameConfigure.K());

			System.out.println();
			System.out.println("Results of this turn: ");
			System.out.println();
			b.printBoard();
			System.out.println();

			for(String s: dataMap.keySet()){
				System.out.printf("%s total energy: %d, count: %d \n", s, dataMap.get(s).getEnergy(), dataMap.get(s).getCount());
			}

			for(DataTracker dt: dataMap.values()){
				if(dt.getEnergy() == 0){
					return true;
				}
			}

			System.out.println();

		}
		return true;
	}


	@Override
	/**
	 * The list of results for all the players
	 * @return an ArrayList of PlayerRoundData objects
	 */
	public ArrayList<PlayerRoundData> getResults() {
		
		ArrayList<PlayerRoundData> d = new ArrayList<PlayerRoundData>();
		d.add(dataMap.get(name1));
		d.add(dataMap.get(name2));

		return d;
	}




}
