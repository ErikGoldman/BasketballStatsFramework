package data;


public class SubPlay extends Play {
	Player inPlayer, outPlayer;
	
	public SubPlay(String id, String lns, String time, Player inPlayer, Player outPlayer) {
		super(id, lns, time);
		
		this.inPlayer = inPlayer;
		this.outPlayer = outPlayer;
	}

	@Override
	protected void apply(GameState g) {
		g.substitution(outPlayer, inPlayer);
	}

}
