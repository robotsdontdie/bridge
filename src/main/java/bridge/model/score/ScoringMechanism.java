package bridge.model.score;

import bridge.model.player.Team;
import bridge.model.bidding.Bid;
import bridge.model.bidding.Contract.Modifier;

public abstract class ScoringMechanism {

	abstract Score computeScore(Result result, Team team, Vulnerability vulnerability);



	public static class Duplicate extends ScoringMechanism {

		@Override
		public Score computeScore(Result result, Team team, Vulnerability vulnerability) {
			if(result.isMade()) {
				int contractPoints = result.getContract().getPotentialContractPoints();
				Bid.Level contractLevel = result.getContract().getContractLevel();

				int slamBonus = 0;
				if(contractLevel.isGrandSlam()) {
					slamBonus = vulnerability.get(team).getGrandSlamBonus();
				}
				else if(contractLevel.isSmallSlam()) {
					slamBonus = vulnerability.get(team).getSmallSlamBonus();
				}
				
				int gameBonus = 50;
				if(contractPoints >= 100) {
					gameBonus = vulnerability.get(team).getGameBonus();
				}

				int overtricks = result.overtricks();
				int overtrickPoints = 0;
				int insult = 0;
				switch(result.getContract().getModifier()) {
					case NONE:
						overtrickPoints = result.getContract().getContractSuit().trickValue() * overtricks;
						break;
					case DOUBLED:
						overtrickPoints = vulnerability.get(team).getDoubledOvertricks(overtricks);
						insult = 50;
						break;
					case REDOUBLED:
						overtrickPoints = vulnerability.get(team).getRedoubledOvertricks(overtricks);
						insult = 100;
						break;
					default:
						//TODO hmm...
						break;
				}

				//TODO account for which team?
				return new Score(contractPoints, gameBonus + slamBonus + overtrickPoints + insult);
			}
			else {
				//TODO finish impl
				return null;
			}
		}
	}
}