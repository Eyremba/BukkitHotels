package gvlfm78.plugin.Hotels;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import gvlfm78.plugin.Hotels.handlers.HotelsConfigHandler;
import gvlfm78.plugin.Hotels.managers.HotelsMessageManager;
import gvlfm78.plugin.Hotels.managers.SignManager;
import gvlfm78.plugin.Hotels.managers.WorldGuardManager;

public class HotelsListener implements Listener {

	private HotelsMain plugin;
	public HotelsListener(HotelsMain instance){
		this.plugin = instance;
	}
	HotelsMessageManager HMM = new HotelsMessageManager(plugin);
	SignManager SM = new SignManager(plugin);
	WorldGuardManager WGM = new WorldGuardManager(plugin);
	HotelsConfigHandler HConH = new HotelsConfigHandler(plugin);

	@EventHandler
	public void onSignPlace(SignChangeEvent e){
		//Player places sign, checking if it's a hotel sign
		Player p = e.getPlayer();
		//If sign is a hotels sign
		if(e.getLine(0).toLowerCase().contains("[hotels]")) {
			if(p.isOp()||(plugin.getConfig().getBoolean("settings.use-permissions")&&(p.hasPermission("hotels.sign.create")||p.hasPermission("hotels.*")))){
				//Sign lines
				String Line3 = ChatColor.stripColor(e.getLine(2)).trim();
				String Line4 = ChatColor.stripColor(e.getLine(3)).trim();

				if(Line3.isEmpty()&&Line4.isEmpty()){
					//Reception sign?
					SM.placeReceptionSign(e);
				}
				else{
					//Room sign?
					SM.placeRoomSign(e);
				}
			}
			else{
				p.sendMessage(HMM.mes("chat.noPermission").replaceAll("(?i)&([a-fk-r0-9])", "\u00A7$1")); 
				e.setLine(0, ChatColor.DARK_RED+"[Hotels]");
				//No permissions
			}
		}
	}

	@EventHandler
	public void onSignUse(PlayerInteractEvent e){
		//Player right clicks sign, checking if it's a hotel sign
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN){//If block is sign
				Player p = e.getPlayer();
				//Permission check
				if(p.isOp()||(plugin.getConfig().getBoolean("settings.use-permissions")&&(p.hasPermission("hotels.sign.use")||p.hasPermission("hotels.*")))){
					Sign s = (Sign) e.getClickedBlock().getState();
					if(SM.isReceptionSign(s))
						SM.useReceptionSign(e);
					else
						SM.useRoomSign(e);
				}
				else
					p.sendMessage(HMM.mes("chat.noPermission").replaceAll("(?i)&([a-fk-r0-9])", "\u00A7$1")); 
			}
		}
	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent e){
		//Player broke a sign, checking if it's a hotel sign
		Block b = e.getBlock();
		if(b.getType().equals(Material.SIGN)||b.getType().equals(Material.SIGN_POST)||b.getType().equals(Material.WALL_SIGN)){
			SM.breakRoomSign(e);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		//Player joined the server, update notification to admins:
		Player p = e.getPlayer();
		if(p.hasPermission("hotel.*")||p.isOp()){
			File qfile = HConH.getFile("queuedMessages.yml");
			YamlConfiguration queue = YamlConfiguration.loadConfiguration(qfile);
			String ava = queue.getString("messages.update.available");
			String lin = queue.getString("messages.update.link");
			if(ava!=null)
				p.sendMessage(ChatColor.BLUE+ava);
			if(lin!=null)
				p.sendMessage(ChatColor.BLUE+lin);
		}
		//Notifying players if any of their rooms has expired while they were offline
		UUID playerUUID = p.getUniqueId();
		YamlConfiguration queue = HConH.getMessageQueue();
		ConfigurationSection allExpiryMessages = queue.getConfigurationSection("messages.expiry");
		if(allExpiryMessages!=null){
			Set<String> keys = allExpiryMessages.getKeys(false);
			if(keys!=null){
				for(String key:keys){
					UUID configUUID = UUID.fromString(queue.getString("messages.expiry."+key+".UUID"));
					if(playerUUID.equals(configUUID)){
						p.sendMessage(queue.getString("messages.expiry."+key+".message"));
						queue.set("messages.expiry."+key, null);
						HConH.saveMessageQueue(queue);
					}
				}
			}
			//There are no messages
		}
		//There are no messages
	}

	public int totalRooms(String hotelName,World w){
		int tot = 0;
		Map<String, ProtectedRegion> regions = new HashMap<String, ProtectedRegion>();
		regions = WGM.getRM(w).getRegions();
		ProtectedRegion[] rlist = regions.values().toArray(new ProtectedRegion[regions.size()]);
		for(int i=0; i<rlist.length; i++){
			ProtectedRegion r = rlist[i];
			if(r.getId().startsWith("hotel-"+hotelName)){
				if(r.getId().matches("^hotel-"+hotelName+"-.+")){
					tot++;
				}
			}
		}
		return tot;
	}

	public int freeRooms(String hotelName,World w){
		int free = 0;
		Map<String, ProtectedRegion> regions = new HashMap<String, ProtectedRegion>();
		regions = WGM.getRM(w).getRegions();
		ProtectedRegion[] rlist = regions.values().toArray(new ProtectedRegion[regions.size()]);
		for(int i=0; i<rlist.length; i++){
			ProtectedRegion r = rlist[i];
			if(r.getId().startsWith("hotel-"+hotelName)){
				if(r.getId().matches("^hotel-"+hotelName+"-.+")){
					int roomNum = Integer.parseInt(r.getId().replaceAll("^hotel-.+-", ""));
					File signFile = HConH.getFile("Signs"+File.separator+hotelName+"-"+roomNum+".yml");
					if(signFile.exists()){
						new YamlConfiguration();
						YamlConfiguration config = YamlConfiguration.loadConfiguration(signFile);
						if(config.get("Sign.renter")==null){
							free++;
						}
					}
				}
			}
		}
		return free;
	}
	//When a player tries to drop an item/block
	@EventHandler
	public void avoidDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		File file = HConH.getFile("Inventories"+File.separator+"Inventory-"+playerUUID+".yml");

		if(file.exists())
			e.setCancelled(true);
	}
	//When a player tries to pickup an item/block
	@EventHandler
	public void avoidPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		File file = HConH.getFile("Inventories"+File.separator+"Inventory-"+playerUUID+".yml");

		if(file.exists())
			e.setCancelled(true);
	}
}
