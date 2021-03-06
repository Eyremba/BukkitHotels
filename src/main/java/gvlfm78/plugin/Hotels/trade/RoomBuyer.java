package gvlfm78.plugin.Hotels.trade;

import org.bukkit.entity.Player;

import gvlfm78.plugin.Hotels.Room;

public class RoomBuyer extends Room implements Buyer {

	private final Player p;
	private double price;

	public RoomBuyer(Room room, Player p, double price) {
		super(room.getHotel(), room.getNum());
		this.p = p;
		this.price = price;
	}

	@Override
	public Player getPlayer() {
		return p;
	}
	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;	
	}
}