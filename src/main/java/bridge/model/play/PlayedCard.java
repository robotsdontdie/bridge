package bridge.model.play;

import bridge.model.cards.Card;
import bridge.model.player.Seat;

public class PlayedCard {
	private final Card card;
	private final Seat seat;

	public PlayedCard(Card card, Seat seat) {
		this.card = card;
		this.seat = seat;
	}

	public Card getCard() {
		return card;
	}

	public Seat getSeat() {
		return seat;
	}
}