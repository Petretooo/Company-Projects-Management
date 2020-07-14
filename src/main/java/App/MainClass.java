package App;

import App.Connector;
import GUI.FirstFrame;

import java.sql.Connection;
import java.sql.SQLException;

public class MainClass {
    public static void main(String[] args) throws SQLException {


        Connection connection = Connector.createConnection();


        FirstFrame frame = new FirstFrame();
    	
        System.out.println("Done!");


    }
}
