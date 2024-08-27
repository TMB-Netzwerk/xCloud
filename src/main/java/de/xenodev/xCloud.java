package de.xenodev;

import de.xenodev.commands.DailyCommand;
import de.xenodev.commands.TOPCommand;
import de.xenodev.mysql.MySQL;
import de.xenodev.mysql.PlayersAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class xCloud extends Plugin {
    private static MySQL mySQL;
    public static xCloud instance;
    private File file;
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;

        try {
            if(!getDataFolder().exists()){
                getDataFolder().mkdir();
            }
            file = new File(getDataFolder().getPath(), "config.yml");
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            if(!file.exists()){
                file.createNewFile();
                config.set("MySQL.Host", "localhost");
                config.set("MySQL.Database", "changed_db");
                config.set("MySQL.Username", "changed_user");
                config.set("MySQL.Password", "changed_pw");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        checkMySQL();
        checkOnlineTime();

        getProxy().getPluginManager().registerCommand(this, new TOPCommand());
        getProxy().getPluginManager().registerCommand(this, new DailyCommand());
    }

    @Override
    public void onDisable() {
        mySQL.closeDatabaseConnectionPool();
    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static String getPrefix() {
        return "§l§exCloud §8| ";
    }

    public static xCloud getInstance() {
        return instance;
    }

    private void checkMySQL(){
        mySQL = new MySQL(config.getString("MySQL.Host"), config.getString("MySQL.Database"), config.getString("MySQL.Username"), config.getString("MySQL.Password"));
        try (Connection connection = getMySQL().dataSource.getConnection()) {
            PreparedStatement preparedStatement1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Players(UUID VARCHAR(100),TIME BIGINT,COINS BIGINT,BYTES BIGINT,TICKETS BIGINT,FIRST_JOIN VARCHAR(20),JOINS BIGINT,REWARD_TIME BIGINT,REWARD_STREAK BIGINT,COLOR VARCHAR(25))");
            preparedStatement1.execute();

            preparedStatement1.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void checkOnlineTime() {
        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                for (ProxiedPlayer proxiedPlayers : ProxyServer.getInstance().getPlayers()) {
                    PlayersAPI.addTime(proxiedPlayers.getUniqueId());
                    if(PlayersAPI.checkTimeReward(proxiedPlayers.getUniqueId()) == 1){
                        PlayersAPI.addCoins(proxiedPlayers.getUniqueId(), new Random().nextInt(500, 1000));
                    }else if(PlayersAPI.checkTimeReward(proxiedPlayers.getUniqueId()) == 2){
                        PlayersAPI.addCoins(proxiedPlayers.getUniqueId(), 2500);
                        proxiedPlayers.sendMessage(xCloud.getPrefix() + "§7Du hast §e" + "2500" + " §7Coins fürs Spielen erhalten");
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

}
