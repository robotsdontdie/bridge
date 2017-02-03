package bridge.model.play;

import bridge.model.cards.Card;

public class InvalidPlayException extends Exception{
	private final Card card;

	public InvalidPlayException(String message, Card card) {
		super(message);
		this.card = card;
	}
}