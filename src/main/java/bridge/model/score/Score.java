package bridge.model.score;

public class Score {

	private final int contractPoints;
	private final int bonusPoints;

	public Score(int contractPoints, int bonusPoints) {
		this.contractPoints = contractPoints;
		this.bonusPoints = bonusPoints;
	}

	public int total() {
		return contractPoints + bonusPoints;
	}
}