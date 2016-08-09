package com.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.api.command.Command;
import com.first.mainListener;

public class Toggle implements Command {

	@Override
	public List<String> getNames() {
		List<String> list = new ArrayList<String>();
		list.add("toggle");
		list.add("t");
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return "first.toggle";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return mainListener.messages.getString("helpToggle");
	}

	@Override
	public void Execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
		// TODO Auto-generated method stub
		mainListener.allow = !mainListener.allow;
		if(mainListener.allow) sender.sendMessage(mainListener.messages.getString("allowToCraft"));
		else sender.sendMessage(mainListener.messages.getString("disallowToCraft"));
	}


}
