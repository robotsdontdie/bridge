package bridge.model.play;

import bridge.model.bidding.Bid;
import bridge.model.cards.Card;
import bridge.model.player.Seat;
import bridge.model.game.Deal;

import java.util.ArrayList;
import java.util.List;

public class Trick {
	
	private final Seat leadingSeat;
	private final Deal deal;

	private final List<PlayedCard> cards;
	private Seat nextSeat;
	private Card.Suit leadSuit;

	private boolean over;
	private PlayedCard winner;

	public Trick(Deal deal, Seat leadingSeat) {
		this.deal = deal;
		this.leadingSeat = leadingSeat;

		cards = new ArrayList<PlayedCard>(4);
		nextSeat = leadingSeat;
		leadSuit = null;
	}

	public boolean isValid(Card card, Seat seat) {
		if(!seat.equals(nextSeat))
			return false;

		if(leadSuit == null)
			return true;
		if(card.getSuit().equals(leadSuit))
			return true;
		return false;
	}

	public void play(Card card, Seat seat) throws InvalidPlayException {
		if(!isValid(card, seat)) {
			throw new InvalidPlayException("Cannot play this card", card);
		}
		cards.add(new PlayedCard(card, seat));
		if(leadSuit == null)
			leadSuit = card.getSuit();
		nextSeat = nextSeat.next();

		if(cards.size() == 4) 
			over = true;

		determineWinner();
	}

	private void determineWinner() {
		if(!over) {
			// TODO be angry
			return;
		}
		Bid.Suit trumpSuit = deal.getTrumpSuit();
		boolean trumpPlayed = false;
		PlayedCard candidate = cards.get(0);
		if(candidate.getCard().getSuit().isTrumpIn(trumpSuit)) {
			trumpPlayed = true;
		}

		for(int i=1; i<4; i++) { 
			
		}
	}

}