import java.util.ArrayList;

public class Cell implements Comparable<Cell>{

	private int foodUnits;
	private Player p;
	private int row;
	private int col;
	private int energy;
	private GameConfig game;

	/**
	 * This is the constructor
	 * It will initialize foodUnits as 0 and set the player as null, because the cell is empty
	 * @param row, the row of the cell
	 * @param col, the col of the cell
	 */
	public Cell(int row, int col){
		foodUnits = 0;
		p = null;
		this.row = row;
		this.col = col;
		
	}
	
	/**
	 * This is the setter method of game configuration
	 * @param game
	 */
	public void setConfigur(GameConfig game){
		this.game = game;		
	}
	
	/**
	 * This is the getter method of energy
	 * @return
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * This method will update the player's energy according to different types of move
	 * (produce, stay put, or move)
	 * @param m
	 */
	public void updateEnergy(Move m){

		if(m.type == Constants.REPRODUCE){
			energy = (energy-game.v())/2;
		}
		else if(m.type == Constants.STAYPUT){
			energy -= game.s();
		}
		else{
			energy -= game.v();
		}

	}

	/**
	 * This method will let the player consume food and add energy to it
	 * @param energy, the energy per unit of food
	 */
	public void consumeFood(int energy){
		energy += energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}



	/**
	 * This is the getter method of the units of food at the cell
	 * @return, int foodUnits
	 */
	public int getFood(){
		return foodUnits;
	}

	
	/**
	 * This is the setter method of the units of food
	 * @param food, new units at this cell
	 */
	public void setFood(int food){
		this.foodUnits = food;
	}
	
	
	/**
	 * this is the getter method of the player at the cell
	 * @return, Player
	 */
	public Player getPlayer(){
		return p;
	}

	
	/**
	 * This is the setter method of the player
	 * @param player, the player that will be put on the cell
	 */
	public void setPlayer(Player player, int energy){
		p = player;
		this.energy = energy;
	}

	/**
	 * @return false, if foodUnits == 0; true if foodUnits !=0
	 */
	public boolean hasFood(){
		if(foodUnits <= 0){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * @return true if the cell has a player; false if not
	 */
	public boolean hasPlayer(){

		if(p == null){
			return false;
		}
		else{
			return true;
		}

	}

	/**
	 * This method will get the row of the current cell
	 * @return
	 */
	public int getRow(){
		return row;
	}

	/**
	 * This method will get the col of the current cell
	 * @return col
	 */
	public int getCol(){
		return col;
	}

	@Override
	/**
	 * A cell can compare with another cell according to its row and column
	 * From left to right, up to bottom, the cell is from small to large.
	 */
	public int compareTo(Cell o) {
		int ownRow = this.getRow();
		int ownCol = this.getCol();
		int otherRow = o.getRow();
		int otherCol = o.getCol();

		if(ownRow == otherRow && ownCol > otherCol){
			return 1;
		}

		else if(ownRow == otherRow && ownCol < otherCol){
			return -1;
		}

		else if(ownCol == otherCol && ownRow < otherRow){
			return -1;
		}

		else if(ownCol == otherCol && ownRow > otherRow){
			return 1;
		}

		else if(ownCol != otherCol && ownRow > otherRow ){
			return 1;
		}

		else if(ownCol != otherCol && ownRow < otherRow ){
			return -1;
		}else{
			return 0;
		}
	}


}
