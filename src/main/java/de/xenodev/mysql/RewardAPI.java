package de.xenodev.mysql;

import de.xenodev.xCloud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RewardAPI {

    private static boolean playerExists(UUID uuid){

        try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Reward WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Reward(UUID, TIME, STREAK) VALUES ('" + uuid + "', '0', '0');");
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public static Long getTime(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Reward WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Long.valueOf(rs.getLong("TIME")) == null));
                return rs.getLong("TIME");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getTime(uuid);
        }
        return null;
    }

    public static void setTime(UUID uuid){
        long time = System.currentTimeMillis()+86400000;
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Reward` SET `TIME` = ? WHERE `UUID` = ?;");
                preparedStatement.setLong(1, time);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setTime(uuid);
        }
    }

    public static Integer getStreak(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Reward WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("STREAK")) == null));
                return rs.getInt("STREAK");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getStreak(uuid);
        }
        return null;
    }

    public static void setStreak(UUID uuid, Integer streak){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Reward` SET `STREAK` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, streak);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setTime(uuid);
        }
    }

    public static void addStreak(UUID uuid, Integer streak){
        if(playerExists(uuid)){
            setStreak(uuid, Integer.valueOf(getStreak(uuid).intValue() + streak.intValue()));
        }else{
            createPlayer(uuid);
            addStreak(uuid, streak);
        }
    }

    public static String remainingTime(long time){
        long seconds = time/1000;
        long minutes = 0;
        while(seconds > 60) {
            seconds-=60;
            minutes++;
        }
        long hours = 0;
        while(minutes > 60) {
            minutes-=60;
            hours++;
        }
        return "§2" + hours + ":" + minutes + ":" + seconds;
    }

    public static Boolean allowReward(UUID uuid){
        long current = System.currentTimeMillis();
        long time = getTime(uuid);
        return current > time;
    }
}
