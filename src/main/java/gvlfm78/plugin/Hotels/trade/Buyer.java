package gvlfm78.plugin.Hotels.trade;

import org.bukkit.entity.Player;

public interface Buyer {
	
	abstract Player getPlayer();
	abstract double getPrice();
	abstract void setPrice(double price);
}
