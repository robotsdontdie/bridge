package bridge.model.player;

import org.junit.Assert;
import org.junit.Test;

public class SeatTest {
	@Test
	public void testNext() {
		Seat north = Seat.NORTH;
		Assert.assertEquals(north.next(), Seat.EAST);
		Assert.assertEquals(north.next().next(), Seat.SOUTH);
		Assert.assertEquals(north.next().next().next(), Seat.WEST);
		Assert.assertEquals(north.next().next().next().next(), Seat.NORTH);
	}

	@Test
	public void testPartner() {
		Seat north = Seat.NORTH;
		Assert.assertEquals(north.partner(), Seat.SOUTH);
		Assert.assertEquals(north.partner().partner(), Seat.NORTH);

		Seat east = Seat.EAST;
		Assert.assertEquals(east.partner(), Seat.WEST);
		Assert.assertEquals(east.partner().partner(), Seat.EAST);
	}

	@Test
	public void testIsPartnerOf() {
		Seat east = Seat.EAST;
		Assert.assertTrue(east.isPartnerOf(Seat.WEST));
		Assert.assertFalse(east.isPartnerOf(Seat.EAST));
		Assert.assertFalse(east.isPartnerOf(Seat.SOUTH));
		Assert.assertFalse(east.isPartnerOf(Seat.NORTH));
	}

	@Test
	public void testTeam() {
		Assert.assertEquals(Seat.WEST.team(), Team.EAST_WEST);
		Assert.assertEquals(Seat.EAST.team(), Team.EAST_WEST);
		Assert.assertEquals(Seat.NORTH.team(), Team.NORTH_SOUTH);
		Assert.assertEquals(Seat.SOUTH.team(), Team.NORTH_SOUTH);
	}

	@Test
	public void testIsMemberOf() {
		Assert.assertTrue(Seat.WEST.isMemberOf(Team.EAST_WEST));
		Assert.assertFalse(Seat.WEST.isMemberOf(Team.NORTH_SOUTH));
		Assert.assertTrue(Seat.SOUTH.isMemberOf(Team.NORTH_SOUTH));
		Assert.assertFalse(Seat.SOUTH.isMemberOf(Team.EAST_WEST));
	}
}