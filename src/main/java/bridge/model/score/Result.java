package bridge.model.score;

import bridge.model.bidding.Contract;
import bridge.model.player.Team;

public class Result {

	private final Contract contract;
	private final int tricksTaken;

	public Result(Contract contract, int tricksTaken) {
		this.contract = contract;
		this.tricksTaken = tricksTaken;
	}

	public Contract getContract() {
		return contract;
	}

	public int getTricksTaken() {
		return tricksTaken;
	}

	public boolean isMade() {
		return overTricks() >= 0;
	}

	public int overtricks() {
		return tricksTaken - (contract.getContractLevel().asInt() + 6)
	}

	public Score getScore(ScoringMechanism scoring, Team team, Vulnerability vulnerability) {
		return scoring.computeScore(this, team, vulnerability);
	}
}