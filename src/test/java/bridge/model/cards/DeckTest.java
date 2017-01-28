package bridge.model.cards;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.ExpectedException;

public class DeckTest {
	@Test
	public void testDeckCreation() {
		Deck deck = Deck.create();
		Assert.assertEquals(deck.size(), 52);
	}

}