package bridge.model.cards; 

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Deck extends Stack<Card> {
	private Deck() {

	}

	public static Deck create() {
		Deck deck = new Deck();
		deck.addRandomly(Card.createAll());
		return deck;
	}

	private void addRandomly(List<Card> cards) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		for(int cardsLeft=52; cardsLeft>0; cardsLeft--) {
			add(cards.remove(rand.nextInt(cardsLeft)));
		}
	}
}