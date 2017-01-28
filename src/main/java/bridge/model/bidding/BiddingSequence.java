package bridge.model.bidding;

import bridge.model.player.Seat;
import bridge.model.player.Team;
import java.util.ArrayList;
import java.util.List;

public class BiddingSequence extends ArrayList<MadeBid> {

	private Seat nextSeat;
	private MadeBid previousBid;
	private MadeBid previousConcreteBid;
	private MadeBid previousNonPassBid;
	private int consecutivePasses;
	private boolean over;

	public BiddingSequence(Seat firstSeat) {
		nextSeat = firstSeat;
		consecutivePasses = 0;
		over = false;
	}

	public Seat getNextSeat() {
		return nextSeat;
	}

	/* package */ MadeBid getPreviousBid() {
		return previousBid;
	}

	/* package */ MadeBid getPreviousConcreteBid() {
		return previousConcreteBid;
	}

	/* package */ MadeBid getPreviousNonPassBid() {
		return previousNonPassBid;
	}

	/* package */ int getConsecutivePasses() {
		return consecutivePasses;
	}

	public boolean isValid(Bid bid) {

		if(isOver())
			return false;

		// check if rank is high enough
		if(bid.isConcrete()) {
			if(previousConcreteBid == null)
				return true;
			if(bid.greaterThan(previousConcreteBid.getBid()))
				return true;
			return false;
		}

		// check redouble
		if(bid.equals(Bid.REDOUBLE)) {
			if(previousNonPassBid != null) {
				if(previousNonPassBid.getBid().equals(Bid.DOUBLE)) {
					if(!previousNonPassBid.getSeat().isPartnerOf(nextSeat)) {
						return true;
					}
				}
			}
			return false;
		}

		// check double
		if(bid.equals(Bid.DOUBLE)) {
			if(previousNonPassBid != null) {
				if(previousNonPassBid.getBid().isConcrete()) {
					if(!previousNonPassBid.getSeat().isPartnerOf(nextSeat)) {
						return true;
					}
				}
			}
			return false;
		}

		// only thing left is pass
		return true;
	}

	public List<Bid> getValidBids() {
		// TODO impl
		return null;
	}

	public boolean isOver() {
		return over;
	}

	public void checkIsOver() {
		if(consecutivePasses >= 4) {
			over = true;
		}
		if(consecutivePasses == 3 && previousNonPassBid != null) {
			over = true;
		}
	}

	public boolean isPassout() {
		if(!over)
			return false;
		boolean allpass = true;
		for(MadeBid bid : this) {
			if(!bid.getBid().equals(Bid.PASS)) {
				allpass = false;
			}
		}
		return allpass;
	}

	public Contract getContract() throws IllegalStateException {
		if(!over) {
			throw new IllegalStateException("Tried to determine contract for sequence that is not over", this);
		}

		if(isPassout())
			return Contract.PASSOUT;

		if(previousConcreteBid == null) {
			throw new IllegalStateException("Tried to determine contract for sequence with no concrete bids", this);
		}

		Bid.Suit contractSuit = previousConcreteBid.getBid().getSuit();
		Team declaringTeam = previousConcreteBid.getSeat().team();

		Seat declarer = getDeclarer(contractSuit, declaringTeam);
		Contract.Modifier modifier = getModifier();
		
		Contract contract = new Contract(previousConcreteBid, declarer, modifier);
		return contract;
	}

	private Seat getDeclarer(Bid.Suit suit, Team team) throws IllegalStateException {
		for(MadeBid bid : this) {
			if(bid.getBid().isConcrete()) {
				if(bid.getBid().getSuit().equals(suit)) {
					if(bid.getSeat().team().equals(team)) {
						return bid.getSeat();
					}
				}	
			}
		}
		throw new IllegalStateException("Cannot determine declarer", this);
	}

	private Contract.Modifier getModifier() {
		for(int i=size()-1; i>=0; i--) {
			Bid bid = get(i).getBid();
			if(bid.equals(Bid.REDOUBLE))
				return Contract.Modifier.REDOUBLED;
			if(bid.equals(Bid.DOUBLE))
				return Contract.Modifier.DOUBLED;
			if(bid.isConcrete())
				break;
		}
		return Contract.Modifier.NONE;
	}

	public void makeBid(Bid bid) throws IllegalBidException {
		if(!isValid(bid)) {
			throw new IllegalBidException("Attempted to make illegal bid", bid, nextSeat, this);
		}

		previousBid = new MadeBid(nextSeat, bid);


		if(!bid.equals(Bid.PASS)) {
			consecutivePasses = 0;
			previousNonPassBid = previousBid;
			if(bid.isConcrete()) {
				previousConcreteBid = previousBid;
			}
		}
		else {
			consecutivePasses++;
		}

		add(previousBid);
		nextSeat = nextSeat.next();
		checkIsOver();
	}


	public static class IllegalStateException extends Exception {
		private final BiddingSequence sequence;
		public IllegalStateException(String message, BiddingSequence sequence) {
			super(message);
			this.sequence = sequence;
		}
	}

}