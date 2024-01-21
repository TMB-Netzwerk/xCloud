package de.xenodev.mysql;

import de.xenodev.xCloud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CoinAPI {

    private static boolean playerExists(UUID uuid){

        try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Coins WHERE UUID= '" + uuid + "'");

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void createPlayer(UUID uuid){
        if(!playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Coins(UUID, COINS) VALUES ('" + uuid + "', '0');");
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public static Integer getCoins(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Coins WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("COINS")) == null));
                return rs.getInt("COINS");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getCoins(uuid);
        }
        return null;
    }

    public static void setCoins(UUID uuid, Integer coins){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Coins` SET `COINS` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, coins);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setCoins(uuid, coins);
        }
    }

    public static void addCoins(UUID uuid, Integer coins){
        if(playerExists(uuid)){
            setCoins(uuid, Integer.valueOf(getCoins(uuid).intValue() + coins.intValue()));
        }else{
            createPlayer(uuid);
            addCoins(uuid, coins);
        }
    }

    public static void removeCoins(UUID uuid, Integer coins){
        if(playerExists(uuid)){
            setCoins(uuid, Integer.valueOf(getCoins(uuid).intValue() - coins.intValue()));
        }else{
            createPlayer(uuid);
            removeCoins(uuid, coins);
        }
    }
}
