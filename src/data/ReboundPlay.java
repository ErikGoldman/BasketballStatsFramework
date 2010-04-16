package data;

public class ReboundPlay extends Play {
	
	private Player player;
	private int off, def;
	
	public ReboundPlay(String id, String lns, String time, Player player, int off, int def) {
		super(id, lns, time);
		
		this.player = player;
		this.off = off;
		this.def = def;
	}

	@Override
	protected void apply(GameState g) {
		g.rebound(player, off, def);
	}

}
