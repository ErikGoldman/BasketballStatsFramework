package data;


public class ShotPlay extends Play {
	
	protected Player scorer;
	protected String type;
	protected int newScore;
	protected boolean made;
	
	public ShotPlay(String id, String lns, String time, Player player, String type, int newScore, boolean made) {
		super(id, lns, time);
		
		this.type = type;
		this.scorer = player;
		this.newScore = newScore;
		this.made = made;
	}

	@Override
	protected void apply(GameState g) {
		int oldScore = g.getScore(scorer.getTeam());
		
		if (this.made) {
			g.updateScore(scorer.getTeam(), newScore);
		}
		
		int scoreDelta = 0;
		if (made) {
			scoreDelta = newScore - oldScore;
		}
		
		// individual stats
		g.shotTaken(scorer, type, scoreDelta, made);
	}

}
