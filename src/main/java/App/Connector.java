package App;

import App.MyModel;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Connector {

    private static Connection connection;

    public static String configuration(){
        File configurationFile = new File("Configuration.txt");
        String data = "";
        try {
            FileReader filerReader = new FileReader(configurationFile);
            BufferedReader bfr = new BufferedReader(filerReader);
            while (true) {
                try {
                    if (((data = bfr.readLine()) != null)) {
                        return data;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }


    public static MyModel selectAll(String nameTable) {
        String sql = "SELECT * FROM " + nameTable;

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            result = state.executeQuery();
            model = new MyModel(nameTable, result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static Connection createConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");
        try {
            connection = DriverManager.getConnection(configuration(), properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static Connection getConnection() {
        return connection;
    }

    public static void deleteRow(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id=?";
        connection = Connector.getConnection();
        PreparedStatement state = null;
        try {
            state = connection.prepareStatement(sql);
            state.setInt(1,id);
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRowByTwoIds(String tableName, int id, int idSecond){
        String sql = "DELETE FROM " + tableName + " WHERE project_id=? and employee_id=?";
        connection = Connector.getConnection();
        PreparedStatement state = null;
        try {
            state = connection.prepareStatement(sql);
            state.setInt(1,id);
            state.setInt(2,idSecond);
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
