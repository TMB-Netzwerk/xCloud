package de.xenodev;

import de.xenodev.commands.DailyCommand;
import de.xenodev.commands.TOPCommand;
import de.xenodev.mysql.CoinAPI;
import de.xenodev.mysql.MySQL;
import de.xenodev.mysql.TimeAPI;
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
            PreparedStatement preparedStatement1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Time(UUID VARCHAR(100),HOURS BIGINT,MINUTES INT,SECONDS INT)");
            PreparedStatement preparedStatement2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Coins(UUID VARCHAR(100),COINS BIGINT)");
            PreparedStatement preparedStatement3 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Bytes(UUID VARCHAR(100),BYTES BIGINT)");
            PreparedStatement preparedStatement4 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Tickets(UUID VARCHAR(100),TICKETS BIGINT)");
            PreparedStatement preparedStatement5 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Reward(UUID VARCHAR(100),TIME BIGINT, STREAK BIGINT)");
            preparedStatement1.execute();
            preparedStatement2.execute();
            preparedStatement3.execute();
            preparedStatement4.execute();
            preparedStatement5.execute();

            preparedStatement1.close();
            preparedStatement2.close();
            preparedStatement3.close();
            preparedStatement4.close();
            preparedStatement5.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void checkOnlineTime() {
        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                for (ProxiedPlayer proxiedPlayers : ProxyServer.getInstance().getPlayers()) {
                    TimeAPI.addSeconds(proxiedPlayers.getUniqueId(), 1);
                    if (TimeAPI.getSeconds(proxiedPlayers.getUniqueId()) == 60) {
                        TimeAPI.setSeconds(proxiedPlayers.getUniqueId(), 0);
                        TimeAPI.addMinutes(proxiedPlayers.getUniqueId(), 1);
                    }
                    if (TimeAPI.getMinutes(proxiedPlayers.getUniqueId()) == 10 && TimeAPI.getSeconds(proxiedPlayers.getUniqueId()) == 0) {
                        CoinAPI.addCoins(proxiedPlayers.getUniqueId(), new Random().nextInt(500, 1000));
                    }
                    if (TimeAPI.getMinutes(proxiedPlayers.getUniqueId()) == 60) {
                        TimeAPI.setMinutes(proxiedPlayers.getUniqueId(), 0);
                        TimeAPI.addHours(proxiedPlayers.getUniqueId(), 1);
                        CoinAPI.addCoins(proxiedPlayers.getUniqueId(), 2500);
                        proxiedPlayers.sendMessage(xCloud.getPrefix() + "§7Du hast §e" + "2500" + " §7Coins fürs Spielen erhalten");
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

}
