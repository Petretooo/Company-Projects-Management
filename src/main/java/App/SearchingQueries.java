package App;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static App.Connector.getConnection;

public class SearchingQueries {

    private static Connection connection;



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

    public static MyModel selectByOneCritery(String nameTable, String category, String oneCritery) {
        //String sql = "SELECT * FROM " + nameTable;

        String sql = "SELECT * FROM " + nameTable + " where " + category + "=?";

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            state.setString(1,oneCritery);
            result = state.executeQuery();
            model = new MyModel(nameTable, result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    public static MyModel selectByTwoCriteries(String nameTable, String category, String secondCategory, String oneCritery, String twoCritery) {



        String sql = "SELECT * FROM " + nameTable + " where " + category + "=?";

        sql = "select * from employees where " + category +" = ? and "+ secondCategory + " = ?";

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            state.setString(1,oneCritery);
            state.setString(2,twoCritery);
            result = state.executeQuery();
            model = new MyModel(nameTable, result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    public static MyModel selectByOneCriteryLike(String nameTable, String category, String oneCritery) {
        //String sql = "SELECT * FROM " + nameTable;

        String sql = "SELECT * FROM " + nameTable + " where " + category + " like '%" +oneCritery+ "%'";

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            //state.setString(1,oneCritery);
            result = state.executeQuery();
            model = new MyModel(nameTable, result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static MyModel selectByTwoCriteryLike(String nameTable, String category, String secondCategory, String oneCritery, String twoCritery) {
        //String sql = "SELECT * FROM " + nameTable;

        String sql = "SELECT * FROM " + nameTable + " where " + category + " like '%" +oneCritery+ "%' and " + secondCategory +" like '%" + twoCritery + "%'";

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            //state.setString(1,oneCritery);
            result = state.executeQuery();
            model = new MyModel(nameTable, result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static MyModel searchingBetweenTwoTables(String oneCritery, String twoCritery) {

        String sql = "SELECT m.first_name, m.last_name, p.name from managers as m\n" +
                "INNER JOIN projects as p\n" +
                "on p.manager_id = m.id\n" +
                "where m.first_name like '%" +oneCritery+"%'\n" +
                "and p.name like '%"+twoCritery+"%' \n" +
                "order by m.id;";

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            result = state.executeQuery();
            model = new MyModel("m1",result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static MyModel searchingBetweenFourTables(String oneCritery, String twoCritery) {

        String sql = "SELECT m.first_name, m.last_name, e.first_name, e.last_name, p.name from managers as m\n" +
                "INNER JOIN projects as p\n" +
                "on p.manager_id = m.id\n" +
                "INNER JOIN project_employees as pe\n" +
                "on p.id = pe.project_id\n" +
                "INNER JOIN employees as e\n" +
                "on pe.employee_id = e.id\n" +
                "where m.first_name like '%"+oneCritery+"%'\n" +
                "and e.first_name like '%"+twoCritery+"%' \n" +
                "order by m.id;";

        connection = getConnection();
        MyModel model = null;
        ResultSet result = null;

        try {
            PreparedStatement state = connection.prepareStatement(sql);
            //state.setString(1,oneCritery);
            result = state.executeQuery();
            model = new MyModel("m2",result);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
