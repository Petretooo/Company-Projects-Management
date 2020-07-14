package GUI;

import App.Connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

import static App.Connector.getConnection;
import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;

public class ProjectEmployeesPanel {

    Connection connection = null;
    PreparedStatement statement = null;
    int id = -1;
    int idSecond = -1;
    //ProjectsPanel projects = new ProjectsPanel();


    JPanel leftPanelProjectsEmployees = new JPanel();
    JPanel rightPanelProjectsEmployees  = new JPanel();

    JPanel upPanelProjectsEmployees  = new JPanel();
    JPanel downPanelProjecstEmployees = new JPanel();

    JLabel employeeNameLabel = new JLabel("Employees");
    JLabel projectNameLabel = new JLabel("Projects");

    JComboBox employeesComboBox = new JComboBox();
    JComboBox projectsComboBox = new JComboBox();

    public JTable tableProjectsEmployees = new JTable();  //JTable([n],[r][n]);
    public JScrollPane scrollerProjectsEmployees = new JScrollPane(tableProjectsEmployees);

    JButton addButtonProjectsEmployees = new JButton("Add");
    JButton deleteButtonProjectsEmployees = new JButton("Delete");
    JButton editButtonProjectsEmployees = new JButton("Edit");

    public ProjectEmployeesPanel() {
    }

    public ProjectEmployeesPanel(JPanel projectsEmployeesPanel) {
        projectsEmployeesPanel.setLayout(new GridLayout(1,2));
        projectsEmployeesPanel.add(leftPanelProjectsEmployees, LEFT_ALIGNMENT);
        projectsEmployeesPanel.add(rightPanelProjectsEmployees, RIGHT_ALIGNMENT);

        leftPanelProjectsEmployees.setLayout(new GridLayout(2,1));
        leftPanelProjectsEmployees.add(upPanelProjectsEmployees);
        leftPanelProjectsEmployees.add(downPanelProjecstEmployees);

        upPanelProjectsEmployees.setLayout(new GridLayout(2,1));
        upPanelProjectsEmployees.add(projectNameLabel);
        upPanelProjectsEmployees.add(projectsComboBox);
        upPanelProjectsEmployees.add(employeeNameLabel);
        upPanelProjectsEmployees.add(employeesComboBox);
        fillEmployees();
        fillProjects();

        downPanelProjecstEmployees.add(addButtonProjectsEmployees);
        addButtonProjectsEmployees.addActionListener(new AddProjectEmployee());
        downPanelProjecstEmployees.add(deleteButtonProjectsEmployees);
        deleteButtonProjectsEmployees.addActionListener(new DeleteProjectEmployees());
        deleteButtonProjectsEmployees.setEnabled(false);
        downPanelProjecstEmployees.add(editButtonProjectsEmployees);
        editButtonProjectsEmployees.addActionListener(new EditProjectEmployees());
        editButtonProjectsEmployees.setEnabled(false);

        tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
        tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(1));
        tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(2));
        scrollerProjectsEmployees.setPreferredSize(new Dimension(500, 400));
        rightPanelProjectsEmployees.add(scrollerProjectsEmployees);
        tableProjectsEmployees.addMouseListener(new TableClickProjectsEmployees());
    }



    private class AddProjectEmployee implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String sampleEmployee = employeesComboBox.getSelectedItem().toString();
            String nameEmployee = sampleEmployee.substring(sampleEmployee.indexOf("Name: "), sampleEmployee.indexOf("Email: ")).replace("Name: ", "").replace("           ","");
            String emailEmployee = sampleEmployee.substring(sampleEmployee.indexOf("Email: ")).replace("Email: ", "");

            String sqlIdEmployee = "select id from employees where email = ?";
            int idEmplyee = 0;
            try {
                Connector.createConnection();
                statement = connection.prepareStatement(sqlIdEmployee);
                statement.setString(1,emailEmployee);
                ResultSet r = statement.executeQuery();
                while (r.next()){
                    idEmplyee = r.getInt("id");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sampleProject = projectsComboBox.getSelectedItem().toString();
            String nameProject = sampleProject.substring(sampleProject.indexOf("Name: "), sampleProject.indexOf("Description: ")).replace("Name: ", "").replace("       ", "");

            String sqlIdProject = "select id from projects where name = ?";
            int idProject = 0;
            try {
                Connector.createConnection();
                statement = connection.prepareStatement(sqlIdProject);
                statement.setString(1,nameProject);
                ResultSet r1 = statement.executeQuery();
                while (r1.next()){
                    idProject = r1.getInt("id");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


            connection = getConnection();

            String sql = "insert into project_employees(project_name,project_id,employee_name,employee_id)\n" +
                    "        values(?, ?, ?, ?);";

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, nameProject);
                statement.setInt(2, idProject);
                statement.setString(3,nameEmployee);
                statement.setInt(4, idEmplyee);
                //statement.execute();
                try{
                    statement.execute();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(leftPanelProjectsEmployees,
                            "This employee is already engaged with this project");
                    return;
                }
                tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
                tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(1));
                tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(2));

            } catch (SQLIntegrityConstraintViolationException s) {
                s.printStackTrace();
            } catch (SQLException s){
                s.printStackTrace();
            }

            clearProjectEmployeesFields();
        }
    }


    private class DeleteProjectEmployees implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Connector.deleteRowByTwoIds("project_employees", id, idSecond);
            tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
            tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(1));
            tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(2));
            id = -1;
            idSecond = -1;
            clearProjectEmployeesFields();
            deleteButtonProjectsEmployees.setEnabled(false);
            editButtonProjectsEmployees.setEnabled(false);
        }
    }

    private class TableClickProjectsEmployees extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tableProjectsEmployees.getSelectedRow();
            Object value = tableProjectsEmployees.getModel().getValueAt(tableProjectsEmployees.getSelectedRow(),1);
            Object valueSecond = tableProjectsEmployees.getModel().getValueAt(tableProjectsEmployees.getSelectedRow(),3);
            id = Integer.parseInt(value.toString());
            idSecond = Integer.parseInt(valueSecond.toString());
            System.out.println(id + " " + idSecond);

            if(e.getClickCount() > 1){
                deleteButtonProjectsEmployees.setEnabled(true);
                editButtonProjectsEmployees.setEnabled(true);
                String nameProjectFirst = tableProjectsEmployees.getValueAt(row,0).toString();
                for (int i = 0; i < projectsComboBox.getItemCount(); i++) {
                    String sample = projectsComboBox.getItemAt(i).toString();
                    if(!sample.isEmpty()) {
                        String nameProjectSecond = sample.substring(sample.indexOf("Name: "), sample.indexOf("Description: ")).replace("Name: ", "").replace("       ", "");
                        if(nameProjectSecond.equals(nameProjectFirst)) {
                            projectsComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                String nameEmployee = tableProjectsEmployees.getValueAt(row,1).toString();
                for (int i = 0; i < employeesComboBox.getItemCount(); i++) {
                    String sample = employeesComboBox.getItemAt(i).toString();
                    if(!sample.isEmpty()) {
                        String nameEmployeeSecond = sample.substring(sample.indexOf("Name: "), sample.indexOf("Email: ")).replace("Name: ", "").replace("           ","");
                        if(nameEmployeeSecond.equals(nameEmployee)) {
                            employeesComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }

            }

//            if(SwingUtilities.isRightMouseButton(e)){
//
//            }
            tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
            tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(1));
            tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(2));
        }
    }

    private class EditProjectEmployees implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String sampleEmployee = employeesComboBox.getSelectedItem().toString();
            String nameEmployee = sampleEmployee.substring(sampleEmployee.indexOf("Name: "), sampleEmployee.indexOf("Email: ")).replace("Name: ", "").replace("           ", "");
            String emailEmployee = sampleEmployee.substring(sampleEmployee.indexOf("Email: ")).replace("Email: ", "");

            String sqlIdEmployee = "select id from employees where email = ?";
            int idEmplyee = 0;
            try {
                Connector.createConnection();
                statement = connection.prepareStatement(sqlIdEmployee);
                statement.setString(1,emailEmployee);
                ResultSet r = statement.executeQuery();
                while (r.next()){
                    idEmplyee = r.getInt("id");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sampleProject = projectsComboBox.getSelectedItem().toString();
            String nameProject = sampleProject.substring(sampleProject.indexOf("Name: "), sampleProject.indexOf("Description: ")).replace("Name: ", "").replace("       ","");

            String sqlIdProject = "select id from projects where name = ?";
            int idProject = 0;
            try {
                Connector.createConnection();
                statement = connection.prepareStatement(sqlIdProject);
                statement.setString(1,nameProject);
                ResultSet r1 = statement.executeQuery();
                while (r1.next()){
                    idProject = r1.getInt("id");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sql = "UPDATE project_employees SET project_name = ?, project_id = ?, employee_name = ?, employee_id = ? WHERE project_id = ? and employee_id = ?";

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try{
                statement = connection.prepareStatement(sql);
                statement.setString(1,nameProject);
                statement.setInt(2,idProject);
                statement.setString(3,nameEmployee);
                statement.setInt(4,idEmplyee);
                statement.setInt(5,id);
                statement.setInt(6,idSecond);
                try{
                    statement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(leftPanelProjectsEmployees,
                            "This employee is already engaged with this project");
                    return;
                }
                //statement.executeUpdate();
                tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
                tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(1));
                tableProjectsEmployees.removeColumn(tableProjectsEmployees.getColumnModel().getColumn(2));
                id = -1;
                idSecond = -1;
                clearProjectEmployeesFields();
            }catch (SQLException s){
                s.printStackTrace();
            }
            clearProjectEmployeesFields();
            editButtonProjectsEmployees.setEnabled(false);
            deleteButtonProjectsEmployees.setEnabled(false);
        }
    }

    public void clearProjectEmployeesFields(){
        employeesComboBox.setSelectedIndex(0);
        projectsComboBox.setSelectedIndex(0);
    }


    public void fillEmployees(){
        employeesComboBox.removeAllItems();
        String sql = "SELECT * FROM employees;";
        connection = getConnection();
        ResultSet result = null;
        employeesComboBox.addItem("");
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()){
                String fullEmplyee ="Name: "+ result.getString(2) + " " + result.getString(3) + "           Email: " + result.getString(5);
                employeesComboBox.addItem(fullEmplyee);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public void fillProjects(){
        projectsComboBox.removeAllItems();
        String sql = "SELECT * FROM projects;";
        connection = getConnection();
        ResultSet result = null;
        projectsComboBox.addItem("");
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()){
                String fullProject ="Name: "+ result.getString(2) + "       Description: " + result.getString(3);
                projectsComboBox.addItem(fullProject);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
}
