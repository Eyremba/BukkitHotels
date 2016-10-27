package gvlfm78.plugin.Hotels;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

import gvlfm78.plugin.Hotels.handlers.HotelsConfigHandler;
import gvlfm78.plugin.Hotels.managers.Mes;
import gvlfm78.plugin.Hotels.managers.SignManager;

public class RoomSign {

	private Room room;

	public RoomSign(Room room){
		this.room = room;
	}
	public RoomSign(Hotel hotel, int num){
		room = new Room(hotel, num);
	}
	public RoomSign(Hotel hotel, String num){
		room = new Room(hotel, Integer.parseInt(num));
	}
	public RoomSign(World world, String hotelName, int num){
		room = new Room(world, hotelName, num);
	}
	public RoomSign(World world, String hotelName, String num){
		room = new Room(world, hotelName, num);
	}
	public RoomSign(String hotelName, int num){
		room = new Room(hotelName, num);
	}
	public RoomSign(String hotelName, String num){
		room = new Room(hotelName, Integer.parseInt(num));
	}
	public void update(){
		Block b = getBlock();
		Material mat = b.getType();
		if(!mat.equals(Material.SIGN)) return;
		Sign s = (Sign) b;

		if(!room.isFree())
			s.setLine(2, SignManager.TimeFormatter(room.getExpiryMinute()-System.currentTimeMillis()/1000/60));
		else{
			s.setLine(2, SignManager.TimeFormatter(room.getTime()));
			s.setLine(3,ChatColor.GREEN + Mes.mesnopre("sign.vacant"));
		}
		s.update();
	}
	public Block getBlock(){
		return getLocation().getBlock();
	}
	public Sign getSign(){
		Block b = getBlock();
		Material mat = b.getType();
		if(mat.equals(Material.SIGN))
			return (Sign) b;
		else return null;
	}
	public String[] getSignLines(){
		Sign s = getSign();
		if(s!=null)
			return s.getLines();
		else return new String[4];
	}
	public Location getLocation(){
		YamlConfiguration config = getConfig();
		String worldUUID = config.getString("Room.location.world");
		if(worldUUID==null) return null;

		World world = Bukkit.getWorld(worldUUID);
		int x = config.getInt("Room.location.x");
		int y = config.getInt("Room.location.y");
		int z = config.getInt("Room.location.z");

		return new Location(world, x, y, z);
	}
	public File getFile(){
		return HotelsConfigHandler.getSignFile(room.getHotel().getName(), room.getNum());
	}
	public YamlConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(getFile());
	}
	public Room getRoom(){
		return room;
	}
	public void deleteConfig(){
		getFile().delete();
	}
	public String getHotelNameFromSign(){
		String firstLine = getSignLines()[0];
		if(firstLine!=null)
			return firstLine.split(" ")[0];
		else return null;

	}
	public int getRoomNumFromSign(){
		String secondLine = getSignLines()[1];
		if(secondLine!=null)
			return Integer.parseInt(secondLine.split(" ")[1]);
		else return 0;
	}
	public void removeSign(){
		Block b = getBlock();
		Material mat = b.getType();
		if(mat.equals(Material.SIGN)){
			Hotel hotel = room.getHotel();
			if(getHotelNameFromSign().matches(hotel.getName())){
				if(hotel.getRegion().contains(b.getX(), b.getY(), b.getZ()))
					b.setType(Material.AIR);
			}
		}
	}
	public void deleteSignAndConfig(){
		removeSign();
		deleteConfig();
	}

}
