package bridge.model.bidding;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Bid implements Comparable<Bid> {
	public static enum Level {
		ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

		private final int rank;
		private Level(int rank) {
			this.rank = rank;
		} 

		public int asInt() {
			return rank;
		}

		public boolean isGrandSlam() {
			return this.equals(SEVEN);
		}

		public boolean isSmallSlam() {
			return this.equals(SIX);
		}
	}

	public static enum Suit {
		CLUBS(1), DIAMONDS(2), HEARTS(3), SPADES(4), NOTRUMP(5);

		private final int rank;
		private Suit(int rank) {
			this.rank = rank;
		}

		public int trickValue() {
			switch(this) {
				case CLUBS: case DIAMONDS: return 20;
				case HEARTS: case SPADES: case NOTRUMP: return 30;
				default: return 0;
			}
		}

		public int contractBonus() {
			switch(this) {
				case CLUBS: case DIAMONDS: case HEARTS: case SPADES: return 0;
				case NOTRUMP: return 10;
				default: return 0;
			}
		}
	}	

	public static final Bid DOUBLE = new Bid("DOUBLE");
	public static final Bid REDOUBLE = new Bid("REDOUBLE");
	public static final Bid PASS = new Bid("PASS");

	private static final int numBids = 38;

	private final Level level;
	private final Suit suit;
	private final boolean special;
	private final String id;

	private Bid(String id) {
		special = true;
		this.id = id;

		// just so that we dont run into initialization checks
		this.level = Level.ONE;
		this.suit = Suit.CLUBS;
	}

	private Bid(Level level, Suit suit) {
		special = false;
		this.level = level;
		this.suit = suit;
		this.id = level.toString() + "_" + suit.toString();
	}

	public boolean isSpecial() {
		return special;
	}

	public boolean isConcrete() {
		return !special;
	}

	public Level getLevel() {
		return level;
	}

	public Suit getSuit() {
		return suit;
	}

	public Suit getContractPoints() {
		if(this.special)
			return 0;
		int base = level.asInt() * suit.trickValue();
		return base + suit.contractBonus();
	}

	public boolean greaterThan(Bid other) {
		if(this.special || other.special) {
			// TODO be sad
			return false;
		}

		return this.compareTo(other) > 0;
	}

	@Override
	public int compareTo(Bid other) {
		if(this.special || other.special) {
			//unspecified behaviour 
			return 0; 
		}

		int levelCompare = this.level.compareTo(other.level);
		if(levelCompare != 0)
			return levelCompare;
		return this.suit.compareTo(other.suit);
	}

	private static List<Bid> bids;

	public static List<Bid> all() {
		if(bids == null)
			initialize();
		return Collections.unmodifiableList(bids);
	}

	public static Bid get(Level level, Suit suit) {
		for(Bid bid : Bid.all()) {
			if(bid.isConcrete()) {
				if(bid.level.equals(level)) {
					if(bid.suit.equals(suit)) {
						return bid;
					}
				}
			}
		}
		return null;
	}

	private static void initialize() {
		bids = new ArrayList<Bid>(numBids);
		for(Level level : Level.values()) {
			for(Suit suit : Suit.values()) {
				bids.add(new Bid(level, suit));
			}
		}
		bids.add(DOUBLE);
		bids.add(REDOUBLE);
		bids.add(PASS);
	}
}