package bridge.model.cards;

import java.util.ArrayList;
import java.util.List;

public class Card {

	public static enum Suit {
		CLUBS(), DIAMONDS(), HEARTS(), SPADES();

		Suit() {
			
		}
	}

	public static enum Rank {
		TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), 
		TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

		private final int rank;
		Rank(int rank) {
			this.rank = rank;
		}
	}

	private final Suit suit;
	private final Rank rank;

	private Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	/* package */ static List<Card> createAll() {
		List<Card> cards = new ArrayList<Card>(52);
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				cards.add(new Card(suit, rank));
			}
		}
		return cards;
	}
}