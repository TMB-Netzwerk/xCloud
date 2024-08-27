package de.xenodev.mysql;

import de.xenodev.xCloud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayersAPI {

    private static boolean playerExists(UUID uuid){

        try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Players(UUID, TIME, COINS, BYTES, TICKETS, JOINS, REWARD_TIME, REWARD_STREAK, COLOR) VALUES ('" + uuid + "', '0', '0', '0', '0', '0', '0', '0', '0', 'GRAY');");
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public static String getColor(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (String.valueOf(rs.getString("COLOR")) == null));
                return rs.getString("COLOR");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getColor(uuid);
        }
        return null;
    }

    public static void setColor(UUID uuid, String color){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `COLOR` = ? WHERE `UUID` = ?;");
                preparedStatement.setString(1, color);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setColor(uuid, color);
        }
    }

    public static Integer getJoins(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("JOINS")) == null));
                return rs.getInt("JOINS");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getJoins(uuid);
        }
        return null;
    }

    public static void addJoins(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `JOINS` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, getJoins(uuid) + 1);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            addJoins(uuid);
        }
    }

    public static Integer getTime(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("TIME")) == null));
                return rs.getInt("TIME");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getTime(uuid);
        }
        return null;
    }

    public static void addTime(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `TIME` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, getTime(uuid) + 1);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            addTime(uuid);
        }
    }

    public static String formateTime(UUID uuid){
        Integer time = getTime(uuid);
        Integer seconds = 0, minutes = 0, hours = 0;

        while (time != 0){
            time--;
            seconds++;
            if(seconds >= 60){
                seconds = 0;
                minutes++;
            }
            if(minutes >= 60){
                minutes = 0;
                hours++;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        if(hours != 0){
            return stringBuilder.append(hours + "h").toString();
        }else if(minutes >= 0 && hours == 0){
            return stringBuilder.append(minutes + "m").toString();
        }else {
            return stringBuilder.append(seconds + "s").toString();
        }
    }

    public static String formateTimeByTime(Integer time){
        Integer seconds = 0, minutes = 0, hours = 0;

        while (time != 0){
            time--;
            seconds++;
            if(seconds >= 60){
                seconds = 0;
                minutes++;
            }
            if(minutes >= 60){
                minutes = 0;
                hours++;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        if(hours != 0){
            return stringBuilder.append(hours + "h").toString();
        }else if(minutes >= 0 && hours == 0){
            return stringBuilder.append(minutes + "m").toString();
        }else {
            return stringBuilder.append(seconds + "s").toString();
        }
    }

    public static Integer checkTimeReward(UUID uuid){
        Integer time = getTime(uuid);
        Integer seconds = 0, minutes = 0, hours = 0;

        while (time != 0){
            time--;
            seconds++;
            if(seconds >= 60){
                seconds = 0;
                minutes++;
            }
            if(minutes >= 60){
                minutes = 0;
                hours++;
            }
        }

        if(seconds == 0 && minutes == 0){
            return 2;
        }else if(seconds == 0 && minutes == 30){
            return 1;
        }else{
            return 0;
        }
    }

    public static Integer getCoins(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `COINS` = ? WHERE `UUID` = ?;");
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

    public static Integer getBytes(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("BYTES")) == null));
                return rs.getInt("BYTES");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getBytes(uuid);
        }
        return null;
    }

    public static void setBytes(UUID uuid, Integer bytes){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `BYTES` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, bytes);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setBytes(uuid, bytes);
        }
    }

    public static void addBytes(UUID uuid, Integer bytes){
        if(playerExists(uuid)){
            setBytes(uuid, Integer.valueOf(getBytes(uuid).intValue() + bytes.intValue()));
        }else{
            createPlayer(uuid);
            addBytes(uuid, bytes);
        }
    }

    public static void removeBytes(UUID uuid, Integer bytes){
        if(playerExists(uuid)){
            setBytes(uuid, Integer.valueOf(getBytes(uuid).intValue() - bytes.intValue()));
        }else{
            createPlayer(uuid);
            removeBytes(uuid, bytes);
        }
    }

    public static Integer getTickets(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("TICKETS")) == null));
                return rs.getInt("TICKETS");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getTickets(uuid);
        }
        return null;
    }

    public static void setTickets(UUID uuid, Integer tickets){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `TICKETS` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, tickets);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setTickets(uuid, tickets);
        }
    }

    public static void addTickets(UUID uuid, Integer tickets){
        if(playerExists(uuid)){
            setTickets(uuid, Integer.valueOf(getTickets(uuid).intValue() + tickets.intValue()));
        }else{
            createPlayer(uuid);
            addTickets(uuid, tickets);
        }
    }

    public static void removeTickets(UUID uuid, Integer tickets){
        if(playerExists(uuid)){
            setTickets(uuid, Integer.valueOf(getTickets(uuid).intValue() - tickets.intValue()));
        }else{
            createPlayer(uuid);
            removeTickets(uuid, tickets);
        }
    }

    public static Long getRewardTime(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Long.valueOf(rs.getLong("REWARD_TIME")) == null));
                return rs.getLong("REWARD_TIME");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getRewardTime(uuid);
        }
        return null;
    }

    public static void setRewardTime(UUID uuid){
        long time = System.currentTimeMillis()+86400000;
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `REWARD_TIME` = ? WHERE `UUID` = ?;");
                preparedStatement.setLong(1, time);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setRewardTime(uuid);
        }
    }

    public static Integer getRewardStreak(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (Integer.valueOf(rs.getInt("REWARD_STREAK")) == null));
                return rs.getInt("REWARD_STREAK");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getRewardStreak(uuid);
        }
        return null;
    }

    public static void setRewardStreak(UUID uuid, Integer streak){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Players` SET `REWARD_STREAK` = ? WHERE `UUID` = ?;");
                preparedStatement.setInt(1, streak);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            setRewardStreak(uuid, streak);
        }
    }

    public static void addRewardStreak(UUID uuid, Integer streak){
        if(playerExists(uuid)){
            setRewardStreak(uuid, Integer.valueOf(getRewardStreak(uuid).intValue() + streak.intValue()));
        }else{
            createPlayer(uuid);
            addRewardStreak(uuid, streak);
        }
    }

    public static String remainingReward(long time){
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

    public static Boolean checkReward(UUID uuid){
        long current = System.currentTimeMillis();
        long time = getRewardTime(uuid);
        return current > time;
    }

    public static String getFirstJoin(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

                ResultSet rs = preparedStatement.executeQuery();
                if((!rs.next()) || (String.valueOf(rs.getString("FIRST_JOIN")) == null));
                return rs.getString("FIRST_JOIN");
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else{
            createPlayer(uuid);
            getFirstJoin(uuid);
        }
        return null;
    }

}
