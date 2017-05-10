import java.util.ArrayList;

public class DataTracker implements PlayerRoundData{
	
	private ArrayList<Player> players;
	private ArrayList<Cell> cells;
	
	
	public DataTracker(){
		players = new ArrayList<>();
		cells = new ArrayList<>();
	}
	
	public ArrayList<Cell> getCells(){
	return cells;
}

public void addCell(Cell c){
	cells.add(c);
}

public void removeCell(Cell c){
	cells.remove(c);
}
	
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
//	
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void removePlayer(Player p){
		players.remove(p);
	}
	
	@Override
	public int getPlayerId() {
		String s = players.get(0).name();
		char[] n = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(char a: n){
			sb.append(Character.getNumericValue(a));
		}
		return Integer.parseInt(sb.toString());
	}
	
	@Override
	public int getEnergy() {
		int totalEnergy = 0;
		
		for(Cell c: cells){
			totalEnergy += c.getEnergy();
			
		}

		return totalEnergy;
	}

	@Override
	public int getCount() {
		return cells.size();
	}

}
