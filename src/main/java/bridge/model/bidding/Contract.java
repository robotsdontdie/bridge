package bridge.model.bidding;

import bridge.model.player.Seat;

public class Contract {
	public static enum Modifier {
		NONE, DOUBLED, REDOUBLED;
	}

	public static final Contract PASSOUT = new Contract();

	private final MadeBid winningBid;
	private final Seat declarer;
	private final Modifier modifier;
	
	public Contract(MadeBid winningBid, Seat declarer, Modifier modifier) {
		this.winningBid = winningBid;
		this.declarer = declarer;
		this.modifier = modifier;
	}

	private Contract() {
		this(null, null, Modifier.NONE);
	}

	public Seat getDummy() {
		return declarer.partner();
	}

	public Seat getDeclarer() {
		return declarer;
	}

	public Bid.Level getContractLevel() {
		if(winningBid == null)
			return null;
		return winningBid.getBid().getLevel();
	}

	public Bid.Suit getContractSuit() {
		if(winningBid == null)
			return null;
		return winningBid.getBid().getSuit();
	}

	public int getPotentialContractPoints() {
		if(this.equals(PASSOUT))
			return 0;
		return winningBid.getBid().getContractPoints();
	}

	public Modifier getModifier() {
		return modifier;
	}
}