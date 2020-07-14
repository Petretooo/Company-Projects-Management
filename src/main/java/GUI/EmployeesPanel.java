package GUI;

import App.Connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static App.Connector.getConnection;
import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;

public class EmployeesPanel {

    Connection connection = null;
    PreparedStatement statement = null;
    int id = -1;
    int idSecond = -1;
    //ProjectEmployeesPanel projectEmployees = new ProjectEmployeesPanel();
    public ProjectEmployeesPanel projectEm;


    JPanel leftPanelEmployees = new JPanel();
    JPanel rightPanelEmployees = new JPanel();

    JPanel upPanelEmployees = new JPanel();
    JPanel downPanelEmployees = new JPanel();

    JLabel firstNameLabelEmployee = new JLabel("First name");
    JLabel lastNameLabelEmployee = new JLabel("Last name");
    JLabel phoneNumberLabelEmployee = new JLabel("Phone number");
    JLabel emailLabelEmployee = new JLabel("Email");
    JLabel nationalityLabelEmployee = new JLabel("Nationality");
    JLabel dateOfBirthLabelEmployee = new JLabel("Date of Birth");
    JLabel positionLabelEmployee = new JLabel("Position");
    JLabel salaryLabelEmployee = new JLabel("Salary");

    JTextField firstNameTFieldEmployee = new JTextField();
    JTextField lastNameTFieldEmployee = new JTextField();
    JTextField phoneNumberTFieldEmployee = new JTextField();
    JTextField emailTFieldEmployee = new JTextField();
    JTextField nationalityTFieldEmployee = new JTextField();
    JTextField dateOfBirthTFieldEmployee = new JTextField();
    JTextField positionTFieldEmployee = new JTextField();
    JTextField salaryTFieldEmployee = new JTextField();

    JTable tableEmployees = new JTable();  //JTable([n],[r][n]);
    JScrollPane scrollerEmployees = new JScrollPane(tableEmployees);

    JButton addButtonEmployee = new JButton("Add");
    JButton deleteButtonEmployee = new JButton("Delete");
    JButton editButtonEmployee = new JButton("Edit");

    public EmployeesPanel() {
    }

    public EmployeesPanel(JPanel employeesPanel) throws SQLException {

        employeesPanel.setLayout(new GridLayout(1,2));
        employeesPanel.add(leftPanelEmployees, LEFT_ALIGNMENT);
        employeesPanel.add(rightPanelEmployees, RIGHT_ALIGNMENT);

        leftPanelEmployees.setLayout(new GridLayout(2,1));
        leftPanelEmployees.add(upPanelEmployees);
        leftPanelEmployees.add(downPanelEmployees);

        upPanelEmployees.setLayout(new GridLayout(8,1));
        upPanelEmployees.add(firstNameLabelEmployee);
        upPanelEmployees.add(firstNameTFieldEmployee);
        upPanelEmployees.add(lastNameLabelEmployee);
        upPanelEmployees.add(lastNameTFieldEmployee);
        upPanelEmployees.add(phoneNumberLabelEmployee);
        upPanelEmployees.add(phoneNumberTFieldEmployee);
        upPanelEmployees.add(emailLabelEmployee);
        upPanelEmployees.add(emailTFieldEmployee);
        upPanelEmployees.add(nationalityLabelEmployee);
        upPanelEmployees.add(nationalityTFieldEmployee);
        upPanelEmployees.add(dateOfBirthLabelEmployee);
        upPanelEmployees.add(dateOfBirthTFieldEmployee);
        upPanelEmployees.add(positionLabelEmployee);
        upPanelEmployees.add(positionTFieldEmployee);
        upPanelEmployees.add(salaryLabelEmployee);
        upPanelEmployees.add(salaryTFieldEmployee);

        downPanelEmployees.add(addButtonEmployee);
        addButtonEmployee.addActionListener(new AddEmployee());
        downPanelEmployees.add(deleteButtonEmployee);
        deleteButtonEmployee.addActionListener(new DeleteEmployee());
        deleteButtonEmployee.setEnabled(false);
        downPanelEmployees.add(editButtonEmployee);
        editButtonEmployee.addActionListener(new EditEmployee());
        editButtonEmployee.setEnabled(false);

        tableEmployees.setModel(Connector.selectAll("employees"));
        tableEmployees.removeColumn(tableEmployees.getColumnModel().getColumn(0));
        scrollerEmployees.setPreferredSize(new Dimension(800, 400));
        rightPanelEmployees.add(scrollerEmployees);
        tableEmployees.addMouseListener(new TableClickEmployees());
    }

    private class AddEmployee implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            String firstName = firstNameTFieldEmployee.getText();
            String lastName = lastNameTFieldEmployee.getText();
            String phoneNumber = phoneNumberTFieldEmployee.getText();
            String email = emailTFieldEmployee.getText();
            String nationality = nationalityTFieldEmployee.getText();
            Date date = Date.valueOf(dateOfBirthTFieldEmployee.getText());

            String position = positionTFieldEmployee.getText();
            double salary = Double.parseDouble(salaryTFieldEmployee.getText());

            String sql = "insert into employees(first_name,last_name,phone_number,email,nationality,date_of_birth,position,salary)\n" +
                    "        values(?, ?, ?, ?, ?, ?, ?, ?);";

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try{
                statement = connection.prepareStatement(sql);
                statement.setString(1,firstName);
                statement.setString(2,lastName);
                statement.setString(3,phoneNumber);
                statement.setString(4,email);
                statement.setString(5,nationality);
                statement.setDate(6, date);
                statement.setString(7,position);
                statement.setDouble(8,salary);
                try{
                    statement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(downPanelEmployees,
                            "We already have employee with this phone number or email.");
                    return;
                }
                tableEmployees.setModel(Connector.selectAll("employees"));
                tableEmployees.removeColumn(tableEmployees.getColumnModel().getColumn(0));

                projectEm.tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
                projectEm.tableProjectsEmployees.removeColumn(projectEm.tableProjectsEmployees.getColumnModel().getColumn(1));
                projectEm.tableProjectsEmployees.removeColumn(projectEm.tableProjectsEmployees.getColumnModel().getColumn(2));

            }catch (SQLException s){
                s.printStackTrace();
            }
            projectEm.fillEmployees();
            clearEmployeeFields();
        }
    }

    private class DeleteEmployee implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connector.deleteRow("employees", id);
            tableEmployees.setModel(Connector.selectAll("employees"));
            tableEmployees.removeColumn(tableEmployees.getColumnModel().getColumn(0));
            projectEm.tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
            projectEm.tableProjectsEmployees.removeColumn(projectEm.tableProjectsEmployees.getColumnModel().getColumn(1));
            projectEm.tableProjectsEmployees.removeColumn(projectEm.tableProjectsEmployees.getColumnModel().getColumn(2));
            id = -1;
            projectEm.fillEmployees();
            clearEmployeeFields();
            deleteButtonEmployee.setEnabled(false);
            editButtonEmployee.setEnabled(false);
        }
    }

    private class TableClickEmployees extends MouseAdapter {


        @Override
        public void mouseClicked(MouseEvent e) {

            int row = tableEmployees.getSelectedRow();
            Object value = tableEmployees.getModel().getValueAt(tableEmployees.getSelectedRow(),0);
            id = Integer.parseInt(value.toString());
            System.out.println(id);

            if(e.getClickCount() > 1){
                deleteButtonEmployee.setEnabled(true);
                editButtonEmployee.setEnabled(true);
                firstNameTFieldEmployee.setText(tableEmployees.getValueAt(row,0).toString());
                lastNameTFieldEmployee.setText(tableEmployees.getValueAt(row, 1).toString());
                phoneNumberTFieldEmployee.setText(tableEmployees.getValueAt(row,2).toString());
                emailTFieldEmployee.setText(tableEmployees.getValueAt(row,3).toString());
                nationalityTFieldEmployee.setText(tableEmployees.getValueAt(row,4).toString());
                dateOfBirthTFieldEmployee.setText(tableEmployees.getValueAt(row,5).toString());
                positionTFieldEmployee.setText(tableEmployees.getValueAt(row,6).toString());
                salaryTFieldEmployee.setText(tableEmployees.getValueAt(row,7).toString());
            }

//            if(SwingUtilities.isRightMouseButton(e)){
//
//            }


            tableEmployees.setModel(Connector.selectAll("employees"));
            tableEmployees.removeColumn(tableEmployees.getColumnModel().getColumn(0));
        }


    }

    private class EditEmployee implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            String firstName = firstNameTFieldEmployee.getText();
            String lastName = lastNameTFieldEmployee.getText();
            String phoneNumber = phoneNumberTFieldEmployee.getText();
            String email = emailTFieldEmployee.getText();
            String nationality = nationalityTFieldEmployee.getText();
            Date date = Date.valueOf(dateOfBirthTFieldEmployee.getText());

            String position = positionTFieldEmployee.getText();
            double salary = Double.parseDouble(salaryTFieldEmployee.getText());

            String sql = "UPDATE employees SET first_name = ?, last_name = ?, phone_number = ?, email = ?, nationality = ?, date_of_birth = ?, position = ?, salary = ? WHERE id = " + id;

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try{
                statement = connection.prepareStatement(sql);
                statement.setString(1,firstName);
                statement.setString(2,lastName);
                statement.setString(3,phoneNumber);
                statement.setString(4,email);
                statement.setString(5,nationality);
                statement.setDate(6, date);
                statement.setString(7,position);
                statement.setDouble(8,salary);
                try{
                    statement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(downPanelEmployees,
                            "We already have employee with this phone number or email.");
                    return;
                }
                tableEmployees.setModel(Connector.selectAll("employees"));
                tableEmployees.removeColumn(tableEmployees.getColumnModel().getColumn(0));
                projectEm.tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
                projectEm.tableProjectsEmployees.removeColumn(projectEm.tableProjectsEmployees.getColumnModel().getColumn(1));
                projectEm.tableProjectsEmployees.removeColumn(projectEm.tableProjectsEmployees.getColumnModel().getColumn(2));
                id = -1;
                clearEmployeeFields();
            }catch (SQLException s){
                s.printStackTrace();
            }

            projectEm.fillEmployees();
            clearEmployeeFields();
            editButtonEmployee.setEnabled(false);
            deleteButtonEmployee.setEnabled(false);
        }
    }


    public void clearEmployeeFields() {
        firstNameTFieldEmployee.setText(" ");
        lastNameTFieldEmployee.setText(" ");
        phoneNumberTFieldEmployee.setText(" ");
        emailTFieldEmployee.setText(" ");
        nationalityTFieldEmployee.setText(" ");
        dateOfBirthTFieldEmployee.setText(" ");
        positionTFieldEmployee.setText(" ");
        salaryTFieldEmployee.setText(" ");
    }
}
