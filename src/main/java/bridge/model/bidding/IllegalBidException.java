package bridge.model.bidding;

import bridge.model.player.Seat;

public class IllegalBidException extends Exception {
	private final Bid bid;
	private final Seat seat;
	private final BiddingSequence sequence;

	public IllegalBidException(String message, Bid bid, Seat seat, BiddingSequence sequence) {
		super(message);
		this.bid = bid;
		this.seat = seat;
		this.sequence = sequence;
	}
}