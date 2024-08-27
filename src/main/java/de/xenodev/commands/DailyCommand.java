package de.xenodev.commands;

import de.xenodev.mysql.PlayersAPI;
import de.xenodev.xCloud;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class DailyCommand extends Command {

    public DailyCommand() {
        super("daily", null, "daily");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer)sender;

            if(!PlayersAPI.checkReward(player.getUniqueId())){
                player.sendMessage(xCloud.getPrefix() + "§7Du hast deine Daily-Reward bereits abgeholt");
                long current = System.currentTimeMillis();
                long time = PlayersAPI.getRewardTime(player.getUniqueId());
                long now = time - current;
                player.sendMessage(xCloud.getPrefix() + "§7Verbleibende Zeit: " + PlayersAPI.remainingReward(now));
                return;
            }
            Integer reward = 1000;
            Integer bonus = 50*PlayersAPI.getRewardStreak(player.getUniqueId());
            Integer amount = reward + bonus;
            PlayersAPI.addCoins(player.getUniqueId(), amount);
            PlayersAPI.addTickets(player.getUniqueId(), 1);
            PlayersAPI.setRewardTime(player.getUniqueId());
            PlayersAPI.addRewardStreak(player.getUniqueId(), 1);
            player.sendMessage(xCloud.getPrefix() + "§7Du hast §e" + amount + " §7Coins und §21 Ticket §7erhalten §8[§7Streak: §c" + PlayersAPI.getRewardStreak(player.getUniqueId()) + "§8]");
        }
    }
}
