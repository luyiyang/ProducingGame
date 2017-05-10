import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/**
 * Board has a 2D array of cells.
 * It matches each cell and the player on the cell.
 * Since it is the environment that cells live, it is also responsible of providing local environments for a specific cell.
 * @author LuyiYang
 *
 */
public class Board {

	private Cell[][] board;
	private int boardSize;

	/**
	 * This is the constructor of board
	 * @param size, the size of board
	 */
	public Board(int size, GameConfig game){
		boardSize = size;
		board = new Cell[boardSize][boardSize];

		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				Cell c = new Cell(i, j);
				c.setConfigur(game);
				board[i][j] = c;
			}
		}
	}

	/**
	 * This is the return method of the board's size
	 * @return size of the board
	 */
	public int getBoardSize(){
		return boardSize;
	}

	/**
	 * This is the getter method of the board
	 * @return, Cell[][]
	 */
	public Cell[][] getBoard(){
		return board;
	}


	/**
	 * This method will put the player at the cell
	 * @param cell, the cell will be put at
	 * @param player, the player that will be put
	 * @param energy, the energy that will be put on cell
	 */
	public void setPlayer(Cell cell, Player player, int energy){
		board[cell.getRow()][cell.getCol()].setPlayer(player, energy);
	}


	/**
	 * This method will return the situations of a player's neighbors
	 * @param r, the player's current row
	 * @param c, the player's current col
	 * @return 0 if the neighbor has player; -1 if not
	 */
	public int[] localNeighbors(int r, int c){
		int[] neighbors = new int[5];
		for(int i = 0; i<neighbors.length; i++){
			neighbors[i] = 0;
		}
		if(!board[r][ adjustIndex(c-1)].hasPlayer()){
			neighbors[1] = -1;
		}
		if(!board[r][ adjustIndex(c+1)].hasPlayer()){
			neighbors[2] = -1;
		}
		if(!board[ adjustIndex(r-1)][c].hasPlayer()){
			neighbors[3] = -1;
		}
		if(!board[ adjustIndex(r+1)][c].hasPlayer()){
			neighbors[4] = -1;
		}

		return neighbors;
	}

	/**
	 * This method will return the situations of food around the position
	 * @param r, row
	 * @param c, col
	 * @return a list of food status, true if the cell has food; false if not.
	 */
	public boolean[] localFood(int r, int c){
		boolean[] food = new boolean[5];

		food[0] = board[r][c].hasFood();
		food[1] = board[r][adjustIndex(c-1)].hasFood();
		food[2] = board[r][adjustIndex(c+1)].hasFood();
		food[3] = board[adjustIndex(r-1)][c].hasFood();
		food[4] = board[adjustIndex(r+1)][c].hasFood();

		return food;
	}


	/**
	 * This method will return the cell the player is going to move to
	 * @param row, current row
	 * @param col, current col
	 * @param m, move
	 * @return, cell the player is going to move to
	 */
	public Cell destinationCell(int currentRow, int currentCol, Move m){

		int destinationRow = 0;
		int destinationCol = 0;

		if(m.type == Constants.STAYPUT){
			return board[currentRow][currentCol];
		}

		else if(m.type() == Constants.REPRODUCE){
			destinationCol = currentCol + Constants.CXTrans[m.childpos];
			destinationRow = currentRow + Constants.CYTrans[m.childpos];
		}  

		else {	
			destinationCol = currentCol + Constants.CXTrans[m.type];
			destinationRow = currentRow + Constants.CYTrans[m.type];


		}
		return board[adjustIndex(destinationRow)][adjustIndex(destinationCol)];

	}

	/**
	 * This method will check whether the cell that the organism
	 * will move to has another player
	 * @param row, the organism's current row
	 * @param col, the organism's current col
	 * @param m, the move the organism is about to take
	 * @return true if is occupied by another player; false if not occupied
	 */
	public boolean hasPlayerAtDestination(int row, int col, Move m){
		return destinationCell(row, col, m).hasPlayer();
	}


	/**
	 * If the index is out of bound, adjust it to the wrap world
	 * @param index
	 * @return
	 */
	private int adjustIndex(int index) {
		int a = index;
		if(a>=boardSize){
			a -= boardSize;
		}
		if(a<0){
			a+=boardSize;
		}
		return a;
	}

	/**
	 * This method will deal with food blows in
	 * It will go through each cell to see whether food can be blowed in
	 * @param possibilityBlowIn
	 * @param k, maximum units of food a cell can take
	 */
	public void foodBlowsIn(double possibilityBlowIn, int k){
		Random rand = new Random();
		for(int i = 0; i<boardSize; i++){
			for(int j = 0; j<boardSize; j++){
				double blowIn = rand.nextDouble();
				if(blowIn<possibilityBlowIn && !board[i][j].hasPlayer() && !board[i][j].hasFood()){
					board[i][j].setFood(1);
				}	
			}
		}
	}

	/**
	 * This method will deal with food doubling. It will go through each cell, 
	 * and see whether the cell that has food will be doubled
	 * @param possibilityDoubling
	 * @param k, maximun units a cell can take
	 */
	public void foodDoubling(double possibilityDoubling, int k){
		Random rand = new Random();
		for(int i = 0; i<boardSize; i++){
			for(int j = 0; j<boardSize; j++){
				if(board[i][j].hasFood()){
					int units = board[i][j].getFood();
					int currentFood = units;
					double doubling;

					for(int m = 0; m<units; m++){
						doubling = rand.nextDouble();
						if(doubling < possibilityDoubling){
							currentFood++;
						}
					}

					if(currentFood <= k){
						board[i][j].setFood(currentFood);
					}

					else if(currentFood > k){
						board[i][j].setFood(k);
					}

				}
			}
		}
	}

	/**
	 * This method will deal with food blowin and food doubling
	 * @param possibilityBlowIn
	 * @param possibilityDoubling
	 * @param k, maximun units of food per cell
	 */
	public void dealWithFood(double possibilityBlowIn, double possibilityDoubling, int k){
		//Deal with Food
		foodBlowsIn(possibilityBlowIn, k);
		foodDoubling(possibilityDoubling, k);
	}

	/**
	 * This method will print out the board
	 * If the cell contains a player, it will print o for "organism"
	 * If the cell contains food, it will print units of food in the cell or f for "food"
	 * If the cell contains both player and food, it will print b for "both"
	 */
	public void printBoard(){

		System.out.print("  ");
		for(int m = 0; m<boardSize; m++){
			System.out.printf(" %d  ", m);
		}
		System.out.println();

		for(int i = 0; i< boardSize; i++){
			System.out.printf("%d ", i);

			for(int j = 0; j<boardSize; j++){		

				if(board[i][j].hasPlayer()){
					if(board[i][j].hasFood()){
						System.out.printf("[%c] ", 'b');
					}else{
						//Player plr = board[i][j].getPlayer();

						if(board[i][j].getEnergy() == 0){
							System.out.printf("[%c] ", ' ');
						}
						else{
							System.out.printf("[%c] ", 'o');
						}


					}
				}
				else if(!board[i][j].hasPlayer() && board[i][j].hasFood()){
					//System.out.printf("[%c] ", 'f');
					System.out.printf("[%d] ", board[i][j].getFood());
				}

				else{
					System.out.printf("[%c] ", ' ');
				}
			}
			System.out.println();
		}
	}


}
