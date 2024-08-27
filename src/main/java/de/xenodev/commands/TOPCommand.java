package de.xenodev.commands;

import de.xenodev.mysql.PlayersAPI;
import de.xenodev.utils.NameFetcher;
import de.xenodev.xCloud;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class TOPCommand extends Command implements TabExecutor {

    public TOPCommand() {
        super("top", null, "top");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("time")){
                try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players ORDER BY TIME DESC LIMIT 3");

                    ResultSet resultSet = preparedStatement.executeQuery();
                    Integer checkPlate = 0;

                    sender.sendMessage("");
                    sender.sendMessage("§8----------» §a§lTop 3 Most Time §8«----------");
                    sender.sendMessage("");

                    while(resultSet.next()) {
                        checkPlate++;
                        String time = PlayersAPI.formateTimeByTime(resultSet.getInt("TIME"));
                        String playerName = NameFetcher.getName(UUID.fromString(resultSet.getString("UUID")));

                        if (checkPlate == 1) {
                            sender.sendMessage(ChatColor.of(new Color(255, 215, 0)) + "§l1. §a" + playerName.toUpperCase() + "§7: §e" + time + " §7Hours");
                        } else if (checkPlate == 2) {
                            sender.sendMessage(ChatColor.of(new Color(192, 192, 192)) + "§l2. §a" + playerName.toUpperCase() + "§7: §e" + time + " §7Hours");
                        } else if (checkPlate == 3) {
                            sender.sendMessage(ChatColor.of(new Color(205, 127, 50)) + "§l3. §a" + playerName.toUpperCase() + "§7: §e" + time + " §7Hours");
                        }
                    }

                    sender.sendMessage("");
                    sender.sendMessage("§8----------» §a§lTop 3 Most Time §8«----------");
                    sender.sendMessage("");
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }else if(args[0].equalsIgnoreCase("bytes")){
                try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players ORDER BY BYTES DESC LIMIT 3");

                    ResultSet resultSet = preparedStatement.executeQuery();
                    Integer checkPlate = 0;

                    sender.sendMessage("");
                    sender.sendMessage("§8----------» §a§lTop 3 Most Bytes §8«----------");
                    sender.sendMessage("");

                    while(resultSet.next()) {
                        checkPlate++;
                        Integer bytes = resultSet.getInt("BYTES");
                        String playerName = NameFetcher.getName(UUID.fromString(resultSet.getString("UUID")));

                        if(checkPlate == 1){
                            sender.sendMessage(ChatColor.of(new Color(255, 215, 0)) + "§l1. §a" + playerName.toUpperCase() + "§7: §e" + bytes + " §7Bytes");
                        }else if(checkPlate == 2){
                            sender.sendMessage(ChatColor.of(new Color(192, 192, 192)) + "§l2. §a" + playerName.toUpperCase() + "§7: §e" + bytes + " §7Bytes");
                        }else if (checkPlate == 3){
                            sender.sendMessage(ChatColor.of(new Color(205, 127, 50)) + "§l3. §a" + playerName.toUpperCase() + "§7: §e" + bytes + " §7Bytes");
                        }
                    }

                    sender.sendMessage("");
                    sender.sendMessage("§8----------» §a§lTop 3 Most Bytes §8«----------");
                    sender.sendMessage("");
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }else if(args[0].equalsIgnoreCase("coins")){
                try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players ORDER BY COINS DESC LIMIT 3");

                    ResultSet resultSet = preparedStatement.executeQuery();
                    Integer checkPlate = 0;

                    sender.sendMessage("");
                    sender.sendMessage("§8----------» §a§lTop 3 Most Coins §8«----------");
                    sender.sendMessage("");

                    while(resultSet.next()) {
                        checkPlate++;
                        Integer coins = resultSet.getInt("COINS");
                        String playerName = NameFetcher.getName(UUID.fromString(resultSet.getString("UUID")));

                        if(checkPlate == 1){
                            sender.sendMessage(ChatColor.of(new Color(255, 215, 0)) + "§l1. §a" + playerName.toUpperCase() + "§7: §e" + coins + " §7Coins");
                        }else if(checkPlate == 2){
                            sender.sendMessage(ChatColor.of(new Color(192, 192, 192)) + "§l2. §a" + playerName.toUpperCase() + "§7: §e" + coins + " §7Coins");
                        }else if (checkPlate == 3){
                            sender.sendMessage(ChatColor.of(new Color(205, 127, 50)) + "§l3. §a" + playerName.toUpperCase() + "§7: §e" + coins + " §7Coins");
                        }
                    }

                    sender.sendMessage("");
                    sender.sendMessage("§8----------» §a§lTop 3 Most Coins §8«----------");
                    sender.sendMessage("");
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else{
            sender.sendMessage("§cEs gibt keine weiteren Argumente!");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList arrayList = new ArrayList<>();
        if(args.length == 1){
            arrayList.add("bytes");
            arrayList.add("coins");
            arrayList.add("time");
        }

        return arrayList;
    }
}
