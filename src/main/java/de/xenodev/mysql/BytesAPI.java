package de.xenodev.mysql;

import de.xenodev.xCloud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BytesAPI {

    private static boolean playerExists(UUID uuid){

        try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Bytes WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Bytes(UUID, BYTES) VALUES ('" + uuid + "', '0');");
                preparedStatement.execute();
                preparedStatement.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public static Integer getBytes(UUID uuid){
        if(playerExists(uuid)){
            try (Connection connection = xCloud.getMySQL().dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Bytes WHERE UUID= '" + uuid + "'");

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
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `Bytes` SET `BYTES` = ? WHERE `UUID` = ?;");
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
}
