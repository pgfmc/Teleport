package net.pgfmc.teleport.tpa;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import net.pgfmc.core.cmd.Goto;
import net.pgfmc.core.requestAPI.Requester;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.dim.SpawnProtection;

public class TpRequest extends Requester {
	
	public static final TpRequest TPA = new TpRequest("Teleport");
	public static final TpRequest TPAHERE = new TpRequest("Teleport Here");
	
	private TpRequest(String name)
	{
		super(name, 120, (a, b) -> {
			if (name.equals("Teleport"))
			{
				if (!(a.isOnline() && b.isOnline())) { return false; }
				
				a.sendMessage("§6Teleporting to " + b.getNickname() + " §r§6in 5 seconds");
				b.sendMessage("§6Teleporting "+ a.getNickname() +" §r§6here in 5 seconds");
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
					SpawnProtection.TEMP_PROTECT(a.getPlayer(), 40);
					Goto.logBackLocation(a.getPlayer(), a.getPlayer().getLocation());
					a.teleport(b);
					a.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
					}
				}, 20 * 5);
				
				return true;
			}
			
			if (name.equals("Teleport Here"))
			{
				if (!(a.isOnline() && b.isOnline())) { return false; }
				
				b.sendMessage("§6Teleporting to " + a.getNickname() + " §r§ain 5 seconds");
				a.sendMessage("§6Teleporting "+ b.getNickname() +" §r§6here in 5 seconds");
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
					
					SpawnProtection.TEMP_PROTECT(a.getPlayer(), 40);
					
					//a.	tempProtect(20 * 2);
					Goto.logBackLocation(b.getPlayer(), b.getPlayer().getLocation());
					b.teleport(a);
					a.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
					}
				}, 20 * 5);
				
				return true;
			}
			
			
			return false;
		});
	}

}
