package net.pgfmc.teleport.home;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.cmd.Goto;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.dim.SpawnProtection;

public class Home implements CommandExecutor {

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
			//TODO GUI
			return false;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(p);
		String name = args[0].toLowerCase();
		if (homes.containsKey(name))
		{
			Location toHome = homes.get(name); // Must be generated before timer otherwise they can delete home before they teleport, generating an exception
			p.sendMessage("§aTeleporting to home §6" + name + "§a in 5 seconds!");
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
				SpawnProtection.TEMP_PROTECT(p, 20 * 2); // Spawn protection
				Goto.logBackLocation(p, p.getLocation()); // Log back location
				p.teleport(toHome);
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
				
				if (Afk.isAfk(p)) { Afk.toggleAfk(p); }
				}
			}, 20 * 5);
		} else
		{
			p.sendMessage("§aCould not find home §6" + name + "§a.");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
		}
		
		return true;
	}

}
