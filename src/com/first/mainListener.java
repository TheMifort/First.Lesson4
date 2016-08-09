package com.first;


import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.api.command.CommandWorker;
import com.commands.Stats;
import com.commands.Toggle;
import com.data.Data;
import com.mysql.MySQLWorker;



public class mainListener extends JavaPlugin implements Listener {
	public static MySQLWorker mysql;
	public static boolean allow = false;
	public static FileConfiguration messages;
	String allowToCraft =  ChatColor.GREEN + "Теперь можно крафтить сундук!";
	String disallowToCraft =  ChatColor.RED + "Теперь нельзя крафтить сундук!";
	String havenotPerms = ChatColor.RED + "Вы не можете использовать эту команду";
	public void onEnable()
	{

		mysql = new MySQLWorker("localhost","3306","first","First","First","ChestCrafts");
		getServer().getPluginManager().registerEvents(this, this);
		loadConfig();
		
		CommandWorker cworker = new CommandWorker("first",true,messages);
		cworker.commands.add(new Stats());
		cworker.commands.add(new Toggle());
		getCommand("first").setExecutor(cworker);
		
		getLogger().info(String.valueOf(allow));
		getLogger().info(allowToCraft);
	}
	public void onDisable()
	{
		
	}
	public void loadConfig()
	{
		getConfig().addDefault("allow", false);
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		allow = getConfig().getBoolean("allow");
		
		File file = new File("plugins/firstplugin/messages.lang");
		messages = YamlConfiguration.loadConfiguration(file);
		messages .addDefault("allowToCraft", ChatColor.GREEN + "Теперь можно крафтить сундук!");
		messages .addDefault("disallowToCraft", ChatColor.RED + "Теперь нельзя крафтить сундук!");
		messages .addDefault("havenotPerms", ChatColor.RED + "Вы не можете использовать эту команду");
		messages.addDefault("withoutPerms",ChatColor.RED +  "У вас нет прав на использование");
		messages.addDefault("allowedCommands",ChatColor.GREEN +  "Доступные команды");
		messages.addDefault("notFound",ChatColor.RED +  "Команда не найдена");
		messages.addDefault("helpToggle","Переключатель");
		messages.addDefault("helpStats","Статистика");
		messages .options().copyDefaults(true);
		try {
			messages .save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allowToCraft =messages .getString("allowToCraft");
		disallowToCraft = messages .getString("disallowToCraft");
		havenotPerms = messages.getString("havenotPerms");
	}
	public boolean onCommand(CommandSender sender,Command cmd,String lbl,String[] args)
	{
		// /first s
/*		if(cmd.getName().equalsIgnoreCase("first"))
		{
			if(sender instanceof Player)
			{
				if(PermissionsEx.getUser((Player)sender).has("first.first"))
				{
					allow = !allow;
					if(allow) sender.sendMessage(allowToCraft);
					else sender.sendMessage(disallowToCraft);
				}
				else sender.sendMessage(havenotPerms);
			}
			else allow = !allow;
			return true;
		}*/
		if(cmd.getName().equalsIgnoreCase("stats"))
		{
			Data data;
			if(args.length == 0)
			{
				if(sender instanceof Player)
				{
					data = mysql.GetPlayerStats(((Player)sender).getName());
				}
				else data = null;
			}
			else
			{
				data = mysql.GetPlayerStats(args[0]);
			}
			if(data == null)sender.sendMessage(ChatColor.RED + "Пользователя нет в базе данных");
			else {
				sender.sendMessage(ChatColor.YELLOW + "Статистика игрока " + ChatColor.BLUE + data.name);
				sender.sendMessage(ChatColor.GREEN + "Скрафил: " + data.allowed);
				sender.sendMessage(ChatColor.RED + "Не скрафил: " + data.disallowed);
			}
		}
		return false;
	}
	@EventHandler
	public void onCraft(CraftItemEvent event)
	{
		if(event.getRecipe().getResult().getType() == Material.CHEST)
		{
			Data data = mysql.GetPlayerStats(event.getWhoClicked().getName());
			if(data == null) data = new Data(event.getWhoClicked().getName(),0,0);
			if(allow)data.allowed++;
			else{data.disallowed++;
			event.setCancelled(true);}
			mysql.SetPlayerStats(data);
		}
	}
	
}
