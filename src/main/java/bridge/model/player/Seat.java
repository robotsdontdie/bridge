package bridge.model.player;

public enum Seat {
	NORTH(1,"N"), EAST(2,"E"), SOUTH(3,"S"), WEST(4,"W");

	private final int order;
	private final String shorthand;
	private Seat(int order, String shorthand) {
		this.order = order;
		this.shorthand = shorthand;
	}

	public Seat next() {
		switch(this) {
			case NORTH: return EAST;
			case EAST: return SOUTH;
			case SOUTH: return WEST;
			case WEST: return NORTH;
			default: return null;
		}
	}

	public Seat partner() {
		switch(this) {
			case NORTH: return SOUTH;
			case EAST: return WEST;
			case SOUTH: return NORTH;
			case WEST: return EAST;
			default: return null;
		}
	}

	public boolean isPartnerOf(Seat other) {
		return other.equals(partner());
	}

	public Team team() {
		switch(this) {
			case NORTH: case SOUTH: return Team.NORTH_SOUTH;
			case EAST: case WEST: return Team.EAST_WEST;
			default: return null;
		}
	}

	public boolean isMemberOf(Team team) {
		return team.equals(team());
	}

}