package com.api.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandWorker implements CommandExecutor {
	
	public List<Command> commands = new ArrayList<Command>();
	boolean allowForOPs;
	String commandName;
	FileConfiguration messages;
	
	public CommandWorker(String name,boolean ops,FileConfiguration messages)
	{
		commandName = name;
		allowForOPs = ops;
		this.messages = messages;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String lbl, String[] args) {
		if(cmd.getName().equals(commandName))
		{
			if(args.length == 0)
			{
				sender.sendMessage(messages.getString("allowedCommands"));
				for(Command c : commands)
				{
					if(!(sender instanceof Player) || (PermissionsEx.getUser((Player) sender).has(c.getPermission()) || (allowForOPs && sender.isOp())))
					{
						sender.sendMessage(ChatColor.YELLOW + "/" + commandName  +" " + c.getNames().get(0) + " - " + c.getHelp());
					}
				}
			}
			else
			{
				for(Command c : commands)
				{
					if(c.getNames().contains(args[0]))
					{
						if(sender instanceof Player)
						{
							if(PermissionsEx.getUser((Player) sender).has(c.getPermission()) || sender.isOp() && allowForOPs)
							{
								c.Execute(sender, cmd, args);
							}
							else sender.sendMessage(messages.getString("withoutPerms") + ": " + cmd.getName() + " " + args[0]);
							return true;
						}
						else{ c.ExecuteConsole(sender, cmd, args);
						return true;
						}
					}
				}
				sender.sendMessage(messages.getString("notFound") + ": " + cmd.getName() + " " + args[0]);
			}
			return true;
		}
		return false;
	}
	
}
