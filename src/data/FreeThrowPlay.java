package data;


public class FreeThrowPlay extends Play {
	private Player player;
	private boolean made;
	private int newScore;
	
	public FreeThrowPlay(String id, String lns, String time, Player player, int newScore, boolean made) {
		super(id, lns, time);
		
		this.player = player;
		this.made = made;
		this.newScore = newScore;
	}

	@Override
	protected void apply(GameState g) {
		if (made) {
			g.updateScore(player.getTeam(), newScore);
		}
		g.freeThrow(player, made);
	}
}
