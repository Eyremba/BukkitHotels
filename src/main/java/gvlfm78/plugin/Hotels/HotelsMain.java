package gvlfm78.plugin.Hotels;

import java.io.IOException;
import java.util.HashMap;

import org.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import gvlfm78.plugin.Hotels.MCMetrics.Graph;
import gvlfm78.plugin.Hotels.handlers.HTCmdExecutor;
import gvlfm78.plugin.Hotels.handlers.HTConfigHandler;
import gvlfm78.plugin.Hotels.managers.Mes;
import gvlfm78.plugin.Hotels.tasks.RoomTask;
import gvlfm78.plugin.Hotels.updateChecker.HTUpdateChecker;
import gvlfm78.plugin.Hotels.updateChecker.HTUpdateListener;
import net.milkbowl.vault.economy.Economy;

public class HotelsMain extends JavaPlugin{

	public static Economy economy = null; //Creating economy variable

	//Task loops
	private RoomTask roomTask;

	private static HotelsMain INSTANCE;

	@Override
	public void onEnable(){
		INSTANCE = this;

		HTConfigHandler.initialise(this);

		PluginDescriptionFile pdfFile = this.getDescription();
		//Listeners and stuff
		getServer().getPluginManager().registerEvents((new HTListener()), this);//Firing event listener
		getCommand("Hotels").setExecutor(new HTCmdExecutor(this));//Firing commands listener
		setupEconomy();
		//Economy and stuff
		if (!setupEconomy())
			//If economy is turned on
			//but no vault is found it will warn the user
			getLogger().severe(Mes.getStringNoPrefix("main.enable.noVault"));

		//Room sign checker and updater
		roomTask = new RoomTask(this);
		int roomMins = getConfig().getInt("roomTaskTimerMinutes", 2);

		boolean isRoomRunning;
		try{
			isRoomRunning = Bukkit.getScheduler().isCurrentlyRunning(roomTask.getTaskId());
		}
		catch(Exception e5){
			isRoomRunning = false;
		}
		if(!isRoomRunning){
			if(roomMins<=0) roomMins = 2;
			roomTask.runTaskTimer(this, 200, roomMins*60*20);
		}

		//Logging to console the correct enabling of Hotels
		getLogger().info(Mes.getStringNoPrefix("main.enable.success").replaceAll("%pluginname%", pdfFile.getName()).replaceAll("%version%", pdfFile.getVersion()));

		final int hotelsCount = HotelsAPI.getHotelCount();

		//MCStats Metrics
		try {
			MCMetrics metrics = new MCMetrics(this);

			Graph hotelAmount = metrics.createGraph("Amount of Hotels");
			Graph language = metrics.createGraph("Language");

			//Hotel amount
			switch(hotelsCount) {
			case 0:
				hotelAmount.addPlotter(new MCMetrics.Plotter("0") {
					@Override
					public int getValue() {
						return 0;
					}
				}); break;
			case 1: case 2: case 3:
				hotelAmount.addPlotter(new MCMetrics.Plotter("1-3") {
					@Override
					public int getValue() {
						return 1;
					}
				}); break;
			case 4: case 5:
				hotelAmount.addPlotter(new MCMetrics.Plotter("4-5") {
					@Override
					public int getValue() {
						return 2;
					}
				}); break;
			case 6: case 7: case 8: case 9: case 10:
				hotelAmount.addPlotter(new MCMetrics.Plotter("6-10") {
					@Override
					public int getValue() {
						return 3;
					}
				}); break;
			case 11: case 12: case 13: case 14: case 15:
				hotelAmount.addPlotter(new MCMetrics.Plotter("11-15") {
					@Override
					public int getValue() {
						return 4;
					}
				});
			case 16: case 17: case 18: case 19: case 20:
				hotelAmount.addPlotter(new MCMetrics.Plotter("16-20") {
					@Override
					public int getValue() {
						return 5;
					}
				}); break;
			default:
				hotelAmount.addPlotter(new MCMetrics.Plotter(">20") {
					@Override
					public int getValue() {
						return 6;
					}
				});
			}

			//Languages
			for(final Language lang : Language.values()){
				language.addPlotter(new MCMetrics.Plotter(lang.getHumanName()) {
					@Override
					public int getValue() {
						return lang.ordinal();
					}
				});
			}

			metrics.start();
		} catch (IOException e) { /*Failed to submit stats */ }

		//BStats Metrics
		Metrics metrics = new Metrics(this);

		//Hotel amount
		metrics.addCustomChart(new Metrics.SimpleBarChart("hotel_amount") {
			@Override
			public HashMap<String, Integer> getValues(HashMap<String, Integer> values) {
				switch(hotelsCount) {
				case 0: values.put("0", 1); break;
				case 1: case 2: case 3:	values.put("1-3", 1); break;
				case 4: case 5: values.put("4-5", 1); break;
				case 6: case 7: case 8: case 9: case 10: values.put("6-10", 1); break;
				case 11: case 12: case 13: case 14: case 15: values.put("11-15", 1); break;
				case 16: case 17: case 18: case 19: case 20: values.put("16-20", 1); break;
				default: values.put(">20", 1);
				}				
				return values;
			}
		});

		//Locale
		metrics.addCustomChart(new Metrics.SimplePie("locale_language") {
			@Override
			public String getValue() {
				return HTConfigHandler.getLanguage().getHumanName();
			}
		});

		//Checking for updates
		if(getConfig().getBoolean("checkForUpdates", true)){
			getServer().getPluginManager().registerEvents((new HTUpdateListener(this, this.getFile())), this);

			final HotelsMain plugin = this;

			Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable (){
				public void run(){
					HTUpdateChecker HUC = new HTUpdateChecker(plugin, plugin.getFile());
					HUC.sendUpdateMessages(getLogger());
				}
			},20L);
		}
	}
	@Override
	public void onDisable(){
		roomTask.cancel();

		PluginDescriptionFile pdfFile = this.getDescription();
		//Logging to console the disabling of Hotels
		getLogger().info(Mes.getStringNoPrefix("main.disable.success").replaceAll("%pluginname%", pdfFile.getName()).replaceAll("%version%", pdfFile.getVersion()));
	}

	//Setting up the economy
	private boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) economy = economyProvider.getProvider();
		return economy != null;
	}
	public static HotelsMain getHotels(){
		return INSTANCE;
	}
}
