package com.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.api.command.Command;
import com.data.Data;
import com.first.mainListener;

public class Stats implements Command{

	@Override
	public List<String> getNames() {
		List<String> list = new ArrayList<String>();
		list.add("stats");
		list.add("s");
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return "first.stats";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return mainListener.messages.getString("helpStats");
	}

	@Override
	public void Execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		// TODO Auto-generated method stub
		Data data;
		if(args.length == 0)
		{
			if(sender instanceof Player)
			{
				data = mainListener.mysql.GetPlayerStats(((Player)sender).getName());
			}
			else data = null;
		}
		else
		{
			data = mainListener.mysql.GetPlayerStats(args[0]);
		}
		if(data == null)sender.sendMessage(ChatColor.RED + "Пользователя нет в базе данных");
		else {
			sender.sendMessage(ChatColor.YELLOW + "Статистика игрока " + ChatColor.BLUE + data.name);
			sender.sendMessage(ChatColor.GREEN + "Скрафил: " + data.allowed);
			sender.sendMessage(ChatColor.RED + "Не скрафил: " + data.disallowed);
		}
	}

}
