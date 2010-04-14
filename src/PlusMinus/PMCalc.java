package PlusMinus;

import data.Play;

public class PMCalc {
	public PMCalc() {
		
	}
	
	private Play lastPlay = null;
	public void addPlay(Play p) {
		// if we started a new game
		if (lastPlay != null && p.getGame() != lastPlay.getGame()) {
			// TODO: add time to all active players equal to the time remaining on the last play
		}
	}
}
