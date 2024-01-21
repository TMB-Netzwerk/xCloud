package de.xenodev.mysql;

import de.xenodev.xCloud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TicketAPI {

    private static boolean playerExists(UUID uuid){

        try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tickets(UUID, TICKETS) VALUES ('" + uuid + "', '0');");
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public static Integer getTickets(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Tickets` SET `TICKETS` = ? WHERE `UUID` = ?;");
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
}
