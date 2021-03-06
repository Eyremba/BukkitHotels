package gvlfm78.plugin.Hotels.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.world.DataException;

import gvlfm78.plugin.Hotels.HTCreationMode;
import gvlfm78.plugin.Hotels.Hotel;
import gvlfm78.plugin.Hotels.HotelsAPI;
import gvlfm78.plugin.Hotels.Room;
import gvlfm78.plugin.Hotels.exceptions.BlockNotSignException;
import gvlfm78.plugin.Hotels.exceptions.EventCancelledException;
import gvlfm78.plugin.Hotels.exceptions.FriendNotFoundException;
import gvlfm78.plugin.Hotels.exceptions.HotelNonExistentException;
import gvlfm78.plugin.Hotels.exceptions.NotRentedException;
import gvlfm78.plugin.Hotels.exceptions.NumberTooLargeException;
import gvlfm78.plugin.Hotels.exceptions.OutOfRegionException;
import gvlfm78.plugin.Hotels.exceptions.RoomNonExistentException;
import gvlfm78.plugin.Hotels.exceptions.UserNonExistentException;
import gvlfm78.plugin.Hotels.exceptions.WorldNonExistentException;
import gvlfm78.plugin.Hotels.managers.HTSignManager;
import gvlfm78.plugin.Hotels.managers.Mes;

public class HTCmdSurrogate {

	public static void cmdCreate(Player p, String hotelName){//Hotel creation command
		UUID playerUUID = p.getUniqueId();
		if(HTCreationMode.isInCreationMode(playerUUID))
			HTCreationMode.hotelSetup(hotelName, p);
		else
			Mes.mes(p, "chat.commands.create.fail");
	}
	public static void cmdCommandsAll(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.help"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.creationMode"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.create"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.room"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.renum"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.rename"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.sethome"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.home"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.check"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.list"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.rlist"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.friend"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.friendList"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.sellh"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.buyh"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.sellr"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.buyr"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.reload"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.remove"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.delete"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.delr"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.roomreset"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.resetroom"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.footer"));
	}
	public static void cmdCommandsOnly(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.help"));

		if(Mes.hasPerm(s,"hotels.createmode"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.creationMode"));

		if(Mes.hasPerm(s,"hotels.create")){
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.create"));
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.room"));}

		if(Mes.hasPerm(s,"hotels.renumber"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.renum"));
		if(Mes.hasPerm(s,"hotels.rename"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.rename"));

		if(Mes.hasPerm(s, "hotels.sethome"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.sethome"));
		if(Mes.hasPerm(s, "hotels.home"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.home"));

		if(Mes.hasPerm(s,"hotels.check"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.check"));
		if(Mes.hasPerm(s,"hotels.list.hotels"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.list"));
		if(Mes.hasPerm(s,"hotels.list.rooms"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.rlist"));

		if(Mes.hasPerm(s,"hotels.friend")){
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.friend"));
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.friendList"));}

		if(Mes.hasPerm(s, "hotels.sell.hotel"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.sellh"));
		if(Mes.hasPerm(s, "hotels.buy.hotel"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.buyh"));


		if(Mes.hasPerm(s, "hotels.sell.room"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.sellr"));
		if(Mes.hasPerm(s, "hotels.buy.room"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.buyr"));


		if(Mes.hasPerm(s, "hotels.resetroom.toggle"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.roomreset"));
		if(Mes.hasPerm(s, "hotels.resetroom.reset"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.resetroom"));

		if(Mes.hasPerm(s,"hotels.reload"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.reload"));

		if(Mes.hasPerm(s,"hotels.remove"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.remove"));
		if(Mes.hasPerm(s,"hotels.delete.rooms"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.delr"));
		if(Mes.hasPerm(s,"hotels.delete"))
			s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.delete"));

		s.sendMessage(Mes.getStringNoPrefix("chat.commands.commands.footer"));
	}
	public static void cmdHelp1(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page1.1"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page1.2"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page1.3"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page1.4"));
		s.sendMessage((Mes.getStringNoPrefix("chat.commands.help.prefooter")).replaceAll("%num%", "2"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.footer"));
	}
	public static void cmdHelp2(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page2.1"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page2.2"));
		s.sendMessage((Mes.getStringNoPrefix("chat.commands.help.prefooter")).replaceAll("%num%", "3"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.footer"));
	}
	public static void cmdHelp3(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page3.1"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page3.2"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page3.3"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page3.4"));
		s.sendMessage((Mes.getStringNoPrefix("chat.commands.help.prefooter")).replaceAll("%num%", "4"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.footer"));
	}
	public static void cmdHelp4(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.1"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.2"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.3"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.4"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.5"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.6"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page4.7"));
		s.sendMessage((Mes.getStringNoPrefix("chat.commands.help.prefooter")).replaceAll("%num%", "5"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.footer"));
	}
	public static void cmdHelp5(CommandSender s){
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.header"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.subheader"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.1"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.2"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.3"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.4"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.5"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.6"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.7"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.page5.8"));
		s.sendMessage((Mes.getStringNoPrefix("chat.commands.help.prefooter")).replaceAll("%num%", "1"));
		s.sendMessage(Mes.getStringNoPrefix("chat.commands.help.footer"));
	}

	public static void cmdCreateModeEnter(Player p){
		if(HTCreationMode.isInCreationMode(p.getUniqueId())){
			Mes.mes(p, "chat.commands.creationMode.alreadyIn"); return; }

		HTCreationMode.saveInventory(p);
		HTCreationMode.giveItems(p);
		Mes.mes(p, "chat.commands.creationMode.enter");	
	}
	public static void cmdCreateModeExit(Player p){
		if(!HTCreationMode.isInCreationMode(p.getUniqueId())){
			Mes.mes(p, "chat.commands.creationMode.notAlreadyIn"); return; }

		Mes.mes(p, "chat.commands.creationMode.exit");
		HTCreationMode.loadInventory(p);	
	}
	public static void cmdCreateModeReset(Player p){
		HTCreationMode.resetInventoryFiles(p);
		Mes.mes(p, "chat.commands.creationMode.reset");
	}
	public static void cmdFriendAdd(Player player, String hotelName, String roomNum, String friendName){

		Room room = new Room(player.getWorld(), hotelName, roomNum);

		if(!room.isRenter(player.getUniqueId())){
			Mes.mes(player, "chat.commands.friend.notRenter"); return; }

		@SuppressWarnings("deprecation")
		OfflinePlayer friend = Bukkit.getServer().getOfflinePlayer(friendName);

		if(player.getUniqueId().equals(friend.getUniqueId())){
			Mes.mes(player, "chat.commands.friend.addYourself"); return; }

		try {
			room.addFriend(friend);
			player.sendMessage(Mes.getString("chat.commands.friend.addSuccess").replaceAll("%friend%", friend.getName()));
		} catch (UserNonExistentException e) {
			Mes.mes(player, "chat.commands.friend.nonExistent");
		} catch (NotRentedException e) {
			Mes.mes(player, "chat.commands.friend.noRenter");
		} catch (IOException e) {
			Mes.mes(player, "chat.commands.friend.wrongData");
		}
	}
	public static void cmdFriendRemove(Player player, String hotelName, String roomNum, String friendName){
		Room room = new Room(player.getWorld(), hotelName, roomNum);

		if(!room.isRenter(player.getUniqueId())){
			Mes.mes(player, "chat.commands.friend.notRenter"); return; }

		@SuppressWarnings("deprecation")
		OfflinePlayer friend = Bukkit.getServer().getOfflinePlayer(friendName);

		try {
			room.removeFriend(friend);
			player.sendMessage(Mes.getString("chat.commands.friend.removeSuccess").replaceAll("%friend%", friend.getName()));
		} catch (NotRentedException e) {
			Mes.mes(player, "chat.commands.friend.noRenter");
		} catch (FriendNotFoundException e) {
			Mes.mes(player, "chat.commands.friend.friendNotInList");
		} catch (IOException e) {
			Mes.mes(player, "chat.commands.friend.wrongData");
		}
	}
	public static void cmdFriendList(CommandSender s, String hotelName, String roomNum){
		Room room = new Room(hotelName, roomNum);

		if(!room.doesSignFileExist()){ Mes.mes(s, "chat.commands.friend.wrongData"); return; }

		if(room.isFree()){ Mes.mes(s, "chat.commands.friend.noRenter"); return; }

		List<String> stringList = room.getFriends();

		if(stringList.isEmpty())
			Mes.mes(s, "chat.commands.friend.noFriends");	

		s.sendMessage(Mes.getString("chat.commands.friend.list.heading").replaceAll("%room%", roomNum).replaceAll("%hotel%", hotelName));

		for(String currentFriend : stringList){
			OfflinePlayer friend = Bukkit.getServer().getOfflinePlayer(UUID.fromString(currentFriend));
			String friendName = friend.getName();
			s.sendMessage(Mes.getString("chat.commands.friend.list.line").replaceAll("%name%", friendName));
		}

		Mes.mes(s, "chat.commands.friend.list.footer");
	}
	public static void cmdRoomListPlayer(Player p, String hotelName, World w){
		cmdRoomListPlayer(((CommandSender) p), hotelName, w);
	}
	public static void cmdRoomListPlayer(CommandSender s, String hotelName, World w){
		Hotel hotel = new Hotel(w, hotelName);
		if(hotel.exists()) listRooms(hotel,s);
		else s.sendMessage(Mes.getString("chat.commands.hotelNonExistent").replaceAll("(?i)&([a-fk-r0-9])", ""));
	}
	public static void renumber(Room room, String newNum, CommandSender sender){
		int oldNum = room.getNum();
		Hotel hotel = room.getHotel();

		if(!Mes.hasPerm(sender, "hotels.renumber.admin") && !hotel.isOwner(((Player) sender).getUniqueId())){
			Mes.mes(sender, "chat.commands.youDoNotOwnThat"); return; }

		try {
			room.renumber(newNum);
			sender.sendMessage(Mes.getString("chat.commands.renumber.success").replaceAll("%oldnum%", String.valueOf(oldNum)).replaceAll("%newnum%", String.valueOf(newNum)).replaceAll("%hotel%", hotel.getName()));
		} catch (NumberFormatException e) {
			sender.sendMessage("chat.commands.somethingWentWrong");
			e.printStackTrace();
		} catch (NumberTooLargeException e) {
			Mes.mes(sender, "chat.commands.renumber.newNumTooBig");
		} catch (HotelNonExistentException e) {
			Mes.mes(sender, "chat.commands.hotelNonExistent");
		} catch (RoomNonExistentException e) {
			Mes.mes(sender, "chat.commands.roomNonExistent");
		} catch (BlockNotSignException e) {
			Mes.mes(sender, "chat.commands.rent.invalidLocation");
		} catch (OutOfRegionException e) {
			Mes.mes(sender, "chat.sign.place.outOfRegion");
		} catch (EventCancelledException e) {
		} catch (FileNotFoundException e) {
			Mes.mes(sender, "chat.sign.use.fileNonExistent");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void removeRoom(String hotelName, String roomNum, World world, CommandSender sender){
		Room room = new Room(world, hotelName, roomNum);

		try {
			room.delete();
			Mes.mes(sender, "chat.commands.removeRoom.success");
		} catch (EventCancelledException e) {}
	}
	public static void removePlayer(Room room, String toRemovePlayer, CommandSender sender){

		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(toRemovePlayer);

		if(!room.isRenter(player.getUniqueId())){ Mes.mes(sender, "chat.commands.remove.playerNotRenter"); return; }

		try {
			room.unrent();
			sender.sendMessage(Mes.getString("chat.commands.remove.success")
					.replaceAll("%player%", toRemovePlayer)
					.replaceAll("%room%", String.valueOf(room.getNum()))
					.replaceAll("%hotel%", room.getHotel().getName()));
		} catch (HotelNonExistentException e) {
			Mes.mes(sender, "chat.commands.hotelNonExistent");
		} catch (WorldNonExistentException e) {
			Mes.mes(sender, "chat.commands.worldNonExistent");
		} catch (RoomNonExistentException e) {
			Mes.mes(sender, "chat.commands.roomNonExistent");
		} catch (NotRentedException e) {
			Mes.mes(sender, "chat.commands.remove.noRenter");
		} catch (EventCancelledException e) {
			//Event was cancelled, don't do anything
		} catch (BlockNotSignException e) {
			Mes.mes(sender, "chat.commands.rent.invalidLocation");
			e.printStackTrace();
		} catch (IOException | DataException | WorldEditException e) {
			Mes.mes(sender, "chat.commands.somethingWentWrong");
			e.printStackTrace();
		}
	}
	public static void check(String playername, CommandSender sender){

		@SuppressWarnings("deprecation")
		OfflinePlayer p = Bukkit.getOfflinePlayer(playername);
		if(p==null || !p.hasPlayedBefore()){ Mes.mes(sender, "chat.commands.userNonExistent"); return; }

		//Printing out owned hotels first
		ArrayList<Hotel> hotels = HotelsAPI.getHotelsOwnedBy(p.getUniqueId());

		sender.sendMessage(Mes.getString("chat.commands.check.headerHotels").replaceAll("%player%", playername));
		if(hotels.size() < 1){ Mes.mes(sender, "chat.commands.check.noHotels"); return; }

		for(Hotel hotel : hotels){
			String hotelName = hotel.getName();
			int total = hotel.getTotalRoomCount();
			int free = hotel.getFreeRoomCount();
			sender.sendMessage(Mes.getString("chat.commands.check.lineHotels")
					.replaceAll("%player%", playername)
					.replaceAll("%hotel%", hotelName)
					.replaceAll("%total%", String.valueOf(total))
					.replaceAll("%free%", String.valueOf(free))
					);
		}

		//And printing out rented rooms
		ArrayList<Room> rooms = HotelsAPI.getRoomsRentedBy(p.getUniqueId());

		sender.sendMessage(Mes.getString("chat.commands.check.headerRooms").replaceAll("%player%", playername));
		if(rooms.size() < 0){ Mes.mes(sender, "chat.commands.check.noRooms"); return; }

		for(Room room : rooms){//looping through rented rooms
			Hotel hotel = room.getHotel();
			String hotelName = hotel.getName();
			String roomNum = String.valueOf(room.getNum());

			long expiryDate = room.getExpiryMinute();

			if(expiryDate>0){
				String timeleft = HTSignManager.TimeFormatter(expiryDate - (System.currentTimeMillis()/1000/60) );
				sender.sendMessage(Mes.getString("chat.commands.check.lineRooms")
						.replaceAll("%hotel%", hotelName).replaceAll("%room%", roomNum).replaceAll("%timeleft%", String.valueOf(timeleft)));
			}
			else//Room is permanently rented
				sender.sendMessage(Mes.getString("chat.commands.check.lineRooms")
						.replaceAll("%hotel%", hotelName).replaceAll("%room%", roomNum).replaceAll("%timeleft%", Mes.getStringNoPrefix("sign.permanent")));
		}
	}
	public static void listHotels(World w, CommandSender sender){
		Mes.mes(sender, "chat.commands.listHotels.heading");

		ArrayList<Hotel> hotels = HotelsAPI.getHotelsInWorld(w);

		for(Hotel hotel : hotels){
			String name = WordUtils.capitalizeFully(hotel.getName());

			String repeated = StringUtils.repeat(" ", 10-name.length());
			sender.sendMessage(Mes.getString("chat.commands.listHotels.line").replaceAll("%hotel%", name)
					.replaceAll("%total%", String.valueOf(hotel.getTotalRoomCount()))
					.replaceAll("%free%", String.valueOf(hotel.getFreeRoomCount()))
					.replaceAll("%space%", repeated)
					);
		}
	}
	public static void listRooms(Hotel hotel, CommandSender sender){

		ArrayList<Room> rooms = hotel.getRooms();

		String hotelName = WordUtils.capitalizeFully(hotel.getName());

		sender.sendMessage(Mes.getString("chat.commands.listRooms.heading").replaceAll("%hotel%", hotelName));

		if(rooms.size()<=0){ Mes.mes(sender, "chat.commands.listRooms.noRooms"); return; }

		for(Room room : rooms){
			String roomNum = String.valueOf(room.getNum());

			String rep = StringUtils.repeat(" ", 10-roomNum.length());
			String state = "";

			if(room.doesSignFileExist()){
				if(room.isFree()) //Vacant
					state = ChatColor.GREEN+Mes.getStringNoPrefix("sign.vacant");
				else //Occupied
					state = ChatColor.BLUE+Mes.getStringNoPrefix("sign.occupied");
				sender.sendMessage(Mes.getString("chat.commands.listRooms.line")
						.replaceAll("%room%", roomNum)
						.replaceAll("%state%", state)
						.replaceAll("%space%", rep)
						);
			}
		}
	}
}