package de.xenodev.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class MySQL {

    private String host;
    private String database;
    private String user;
    private String password;
    public HikariDataSource dataSource;

    public MySQL(String host, String database, String user, String password){
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        initDatabaseConnectionPool();
    }

    public void initDatabaseConnectionPool() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + host + ":3306/" + database);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
    }

    public void closeDatabaseConnectionPool() {
        dataSource.close();
    }

    public void update(String qry){
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate(qry);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet query(String qry){
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(qry)) {
                ResultSet resultSet = statement.executeQuery();
                return resultSet;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
