package de.xenodev.commands;

import de.xenodev.mysql.CoinAPI;
import de.xenodev.mysql.RewardAPI;
import de.xenodev.mysql.TicketAPI;
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

            if(!RewardAPI.allowReward(player.getUniqueId())){
                player.sendMessage(xCloud.getPrefix() + "§7Du hast deine Daily-Reward bereits abgeholt");
                long current = System.currentTimeMillis();
                long time = RewardAPI.getTime(player.getUniqueId());
                long now = time - current;
                player.sendMessage(xCloud.getPrefix() + "§7Verbleibende Zeit: " + RewardAPI.remainingTime(now));
                return;
            }
            Integer reward = 1000;
            Integer bonus = 50*RewardAPI.getStreak(player.getUniqueId());
            Integer amount = reward + bonus;
            CoinAPI.addCoins(player.getUniqueId(), amount);
            TicketAPI.addTickets(player.getUniqueId(), 1);
            RewardAPI.setTime(player.getUniqueId());
            RewardAPI.addStreak(player.getUniqueId(), 1);
            player.sendMessage(xCloud.getPrefix() + "§7Du hast §e" + amount + " §7Coins und §21 Ticket §7erhalten §8[§7Streak: §c" + RewardAPI.getStreak(player.getUniqueId()) + "§8]");
        }
    }
}
