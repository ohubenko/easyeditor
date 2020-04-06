package org.easyeditor;

import java.io.File;
import java.sql.*;


public class DBHandler {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:postgresql://localhost:5432/EasyEditor";
        Class.forName("org.postgresql.Driver");
        System.out.println("Driver load successful");
        dbConnection = DriverManager.getConnection(connectionString, System.getenv("dbUser"),
                System.getenv("dbPass"));
        return dbConnection;
    }

    public void add(File file, Timestamp timestamp) {
        String insert = "INSERT INTO " + System.getenv("TABLE") + "(" + System.getenv("FILE_STAMP") + ","
                + System.getenv("FILE_PATH") + ")" +
                "VALUES(?,?)";
        try {
            if (!check(file)) {
                PreparedStatement prSt = getDbConnection().prepareStatement(insert);
                prSt.setTimestamp(1, timestamp);
                prSt.setString(2, file.toString());
                prSt.executeUpdate();
                System.out.println("Add file");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public boolean check(File file) throws SQLException {
        ResultSet resultSet = getLastFiles();
        if (resultSet == null) return false;
        while (resultSet.next()) {
            String fileName = resultSet.getString("namefile");
            if (file.toString().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public ResultSet getLastFiles() {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + System.getenv("TABLE") + " ORDER BY id DESC LIMIT 10";
        try {
            PreparedStatement st = getDbConnection().prepareStatement(select);
            resultSet = st.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}

