package GUI;

import javax.swing.*;
import java.sql.*;

public class MainGUI extends JFrame {

    Connection connection = null;
    PreparedStatement statement = null;
    int id = -1;
    int idSecond = -1;

    JTabbedPane pane = new JTabbedPane();
    JPanel employeesPanel = new JPanel();
    JPanel managerPanel = new JPanel();
    JPanel projectPanel = new JPanel();
    JPanel projectsEmployeesPanel = new JPanel();
    JPanel searchPanel = new JPanel();

    EmployeesPanel employees = new EmployeesPanel(employeesPanel);
    ManagersPanel managers = new ManagersPanel(managerPanel);
    ProjectsPanel projects = new ProjectsPanel(projectPanel);
    ProjectEmployeesPanel projectEmployees = new ProjectEmployeesPanel(projectsEmployeesPanel);
    SearchPanel search = new SearchPanel(searchPanel);

    public MainGUI() throws SQLException {
        this.add(pane);
        pane.add(employeesPanel, "Employees");
        pane.add(managerPanel, "Managers");
        pane.add(projectPanel, "Projects");
        pane.add(projectsEmployeesPanel, "Employees Involved");
        pane.add(searchPanel, "Search");

        this.setVisible(true);
        this.setSize(1700,600);

        this.setDefaultCloseOperation(MainGUI.EXIT_ON_CLOSE);

        employees.projectEm = projectEmployees;
        managers.projects = projects;
        projects.projectEmployees = projectEmployees;

    }
}
