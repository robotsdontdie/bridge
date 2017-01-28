package bridge.model.cards;

import java.util.List;
import org.junit.Test;
import org.junit.Assert;

public class CardTest {
	@Test
	public void testCardCreation() {
		List<Card> cards = Card.createAll();
		Assert.assertEquals(cards.size(), 52);

		int numFiveOfClubs = 0;
		for(Card card : cards) {
			if(card.getSuit().equals(Card.Suit.CLUBS)) {
				if(card.getRank().equals(Card.Rank.FIVE)) {
					numFiveOfClubs ++;
				}
			}
		}
		Assert.assertEquals(numFiveOfClubs, 1);
	}

}