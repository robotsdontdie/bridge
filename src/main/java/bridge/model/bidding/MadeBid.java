package bridge.model.bidding;

import bridge.model.player.Seat;

public class MadeBid {
	private final Seat seat;
	private final Bid bid;

	public MadeBid(Seat seat, Bid bid) {
		this.seat = seat;
		this.bid = bid;
	}

	public Seat getSeat() {
		return seat;
	}

	public Bid getBid() {
		return bid;
	}
}