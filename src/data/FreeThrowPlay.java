package data;


public class FreeThrowPlay extends Play {
	private Player player;
	private boolean made;
	private int num;
	
	public FreeThrowPlay(String id, int time, Player[] players, Player player, int num, boolean made) {
		super(id, time, players);
		
		this.player = player;
		this.made = made;
	}

	@Override
	protected void apply(GameState g) {
		if (made) {
			g.updateScore(player.getTeam(), 1);
		}
		g.freeThrow(player, num, made);
	}
}
