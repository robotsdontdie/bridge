package bridge.model.game;

import bridge.model.bidding.Bid;
import bridge.model.bidding.Contract;

public class Deal {
	private Contract contract;

	public Bid.Suit getTrumpSuit() {
		return contract.getContractSuit();
	}
}