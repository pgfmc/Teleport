package net.pgfmc.teleport.back;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.cmd.Goto;
import net.pgfmc.survival.dim.SpawnProtection;
import net.pgfmc.teleport.Main;

/**
 * Command to teleport the player to their last death location.
 * @author bk
 */
public class Back implements CommandExecutor, Listener {
	
	/**
	 * Teleports a player their back location if it exists
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { return true; }
		
		Player p = (Player) sender;
		
		Location dest = Goto.getBackLocation(p);
		if (dest == null)
		{
			p.sendMessage("§cYou do not have a back location.");
			return true;
		}
		
		p.sendMessage("§6You will be teleported in 5 seconds.");
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				Goto.logBackLocation(p, null);
				SpawnProtection.TEMP_PROTECT(p, 20 * 2);
				p.teleport(dest); // may be wrong I will test
				p.sendMessage("§aPoof!");
			}
			
		}, 20 * 5);
		
		
		return true;
	}
	
	/**
	 * Sets the back location on death
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		
		Goto.logBackLocation(p, p.getLocation());
	}

}
