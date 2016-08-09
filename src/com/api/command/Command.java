package com.api.command;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface Command {
	List<String> getNames();
	String getPermission();
	String getHelp();
	
	void Execute(CommandSender sender,org.bukkit.command.Command cmd,String[] args);
	default void ExecuteConsole(CommandSender sender,org.bukkit.command.Command cmd,String[] args)
	{
		Execute(sender,cmd,args);
	}
}
