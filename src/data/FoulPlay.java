package data;

public class FoulPlay extends Play {

	Player fouler, fouled;
	String type;
	
	public FoulPlay(String id, int time, Player[] players, Player fouler, Player fouled, String type) {
		super(id, time, players);
		
		this.fouler = fouler;
		this.fouled = fouled;
		this.type = type;
	}
	
	@Override
	protected void apply(GameState g) {
		g.foul(fouler, fouled, type);
	}

}
