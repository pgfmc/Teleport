package net.pgfmc.teleport.warp;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.cmd.Goto;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.dim.SpawnProtection;
import net.pgfmc.teleport.Main;

public class Warp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (args.length == 0)
		{
			p.chat("/warps");
			return true;
		}
		
		String name = args[0].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		Map<?, ?> warp = Warps.getWarp(name);
		
		if (warp == null)
		{
			sender.sendMessage("§cCould not find warp: §6" + name);
			return true;
		}
		
		
		
		sender.sendMessage("§aWarping to §6" + name + " §ain 5 seconds!");
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
			SpawnProtection.TEMP_PROTECT(p, 40);
			Goto.logBackLocation(p, p.getLocation());
			p.teleport((Location) warp.get(name));
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
			
			if (Afk.isAfk(p)) { Afk.toggleAfk(p); }
			}
		}, 20 * 5);
		
		
		
		return true;
	}

}
