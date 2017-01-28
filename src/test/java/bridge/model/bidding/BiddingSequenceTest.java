package bridge.model.bidding;

import bridge.model.player.Seat;
import bridge.model.bidding.Bid.Level;
import bridge.model.bidding.Bid.Suit;
import bridge.model.bidding.BiddingSequence.IllegalStateException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class BiddingSequenceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	public static final Bid ONECLUB = Bid.get(Level.ONE, Suit.CLUBS);
	public static final Bid ONEDIAMOND = Bid.get(Level.ONE, Suit.DIAMONDS);
	public static final Bid TWOSPADES = Bid.get(Level.TWO, Suit.SPADES);
	public static final Bid FOURSPADES = Bid.get(Level.FOUR, Suit.SPADES);

	@Test
	public void testOpeningBidValidity() {
		BiddingSequence seq = new BiddingSequence(Seat.NORTH);
		Assert.assertTrue(seq.isValid(ONECLUB));
		Assert.assertTrue(seq.isValid(ONEDIAMOND));
		Assert.assertTrue(seq.isValid(Bid.PASS));
		Assert.assertFalse(seq.isValid(Bid.DOUBLE));
		Assert.assertFalse(seq.isValid(Bid.REDOUBLE));
	}

	@Test
	public void testSingleBid() throws IllegalBidException {
		BiddingSequence seq = new BiddingSequence(Seat.NORTH);
		Assert.assertTrue(seq.isValid(ONECLUB));

		seq.makeBid(ONECLUB);
		Assert.assertEquals(seq.getNextSeat(), Seat.EAST);
		Assert.assertNotNull(seq.getPreviousBid());
		Assert.assertNotNull(seq.getPreviousConcreteBid());
		Assert.assertNotNull(seq.getPreviousNonPassBid());
	}

	@Test
	public void testRespondingBidValidity() throws IllegalBidException {
		BiddingSequence seq = new BiddingSequence(Seat.SOUTH);
		Assert.assertTrue(seq.isValid(ONECLUB));

		seq.makeBid(ONECLUB);
		Assert.assertEquals(seq.getNextSeat(), Seat.WEST);

		Assert.assertTrue(seq.isValid(ONEDIAMOND));
		Assert.assertTrue(seq.isValid(Bid.PASS));
		Assert.assertTrue(seq.isValid(Bid.DOUBLE));
		Assert.assertFalse(seq.isValid(Bid.REDOUBLE));

		seq.makeBid(Bid.PASS);
		Assert.assertEquals(seq.getNextSeat(), Seat.NORTH);

		Assert.assertTrue(seq.isValid(ONEDIAMOND));
		Assert.assertTrue(seq.isValid(Bid.PASS));
		Assert.assertFalse(seq.isValid(Bid.DOUBLE));
		Assert.assertFalse(seq.isValid(Bid.REDOUBLE));
	}

	@Test
	public void testDoublingValidity() throws IllegalBidException {
		BiddingSequence seq = new BiddingSequence(Seat.WEST);

		seq.makeBid(ONEDIAMOND);
		Assert.assertTrue(seq.isValid(Bid.DOUBLE));

		seq.makeBid(Bid.DOUBLE);
		Assert.assertTrue(seq.isValid(Bid.REDOUBLE));
		Assert.assertTrue(seq.isValid(Bid.PASS));
		Assert.assertFalse(seq.isValid(Bid.DOUBLE));
	}

	@Test 
	public void testLongestBiddingEver() throws IllegalBidException {
		BiddingSequence seq = new BiddingSequence(Seat.EAST);

		seq.makeBid(ONECLUB); //EAST
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.DOUBLE);
		seq.makeBid(Bid.PASS); //EAST
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.REDOUBLE);
	}

	@Test
	public void testPassout() throws IllegalBidException, IllegalStateException {
		BiddingSequence seq = new BiddingSequence(Seat.WEST);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);

		Assert.assertTrue(seq.isOver());
		Assert.assertEquals(seq.getContract(), Contract.PASSOUT);
	}

	@Test
	public void testOneBidContract() throws IllegalBidException, IllegalStateException {
		BiddingSequence seq = new BiddingSequence(Seat.SOUTH);
		seq.makeBid(ONEDIAMOND);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);

		Assert.assertTrue(seq.isOver());
		Contract contract = seq.getContract();

		Assert.assertEquals(contract.getDeclarer(),Seat.SOUTH);
		Assert.assertEquals(contract.getContractLevel(),Level.ONE);
		Assert.assertEquals(contract.getContractSuit(),Suit.DIAMONDS);
	}

	@Test
	public void testTooManyBids() throws IllegalBidException {
		BiddingSequence seq = new BiddingSequence(Seat.SOUTH);
		seq.makeBid(ONEDIAMOND);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);

		Assert.assertTrue(seq.isOver());
		Assert.assertFalse(seq.isValid(Bid.PASS));

		thrown.expect(IllegalBidException.class);
		seq.makeBid(Bid.PASS);
	}

	@Test
	public void testBidTooLow() throws IllegalBidException {
		BiddingSequence seq = new BiddingSequence(Seat.SOUTH);
		seq.makeBid(TWOSPADES);

		Assert.assertFalse(seq.isValid(ONEDIAMOND));

		thrown.expect(IllegalBidException.class);
		seq.makeBid(ONEDIAMOND);
	}

	@Test
	public void testContractIsOnFirstBidByTeam() throws IllegalBidException, IllegalStateException {
		BiddingSequence seq = new BiddingSequence(Seat.SOUTH);
		seq.makeBid(Bid.PASS);
		seq.makeBid(TWOSPADES); //west
		seq.makeBid(Bid.PASS);
		seq.makeBid(FOURSPADES); //east
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);

		Assert.assertTrue(seq.isOver());
		Contract contract = seq.getContract();
		Assert.assertEquals(contract.getDeclarer(), Seat.WEST);
		Assert.assertEquals(contract.getDummy(), Seat.EAST);
		Assert.assertEquals(contract.getContractLevel(), Level.FOUR);
	}

	@Test
	public void testTryToGetContractBeforeSequenceOver() throws IllegalBidException, IllegalStateException {
		BiddingSequence seq = new BiddingSequence(Seat.NORTH);
		seq.makeBid(Bid.PASS);
		seq.makeBid(ONECLUB); 
		seq.makeBid(Bid.PASS);
		seq.makeBid(ONEDIAMOND); 
		seq.makeBid(Bid.PASS);
		seq.makeBid(Bid.PASS);
		seq.makeBid(TWOSPADES);
		seq.makeBid(Bid.PASS);

		thrown.expect(IllegalStateException.class);
		seq.getContract();
	}

}