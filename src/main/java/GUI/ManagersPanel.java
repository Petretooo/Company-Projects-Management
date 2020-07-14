package GUI;

import App.Connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static App.Connector.getConnection;
import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;

public class ManagersPanel {

    Connection connection = null;
    PreparedStatement statement = null;
    int id = -1;
    int idSecond = -1;
    public ProjectsPanel projects;

    JPanel leftPanelManagers = new JPanel();
    JPanel rightPanelManagers = new JPanel();

    JPanel upPanelManagers = new JPanel();
    JPanel downPanelManagers= new JPanel();

    JLabel firstNameLabelManager = new JLabel("First name");
    JLabel lastNameLabelManager = new JLabel("Last name");
    JLabel phoneNumberLabelManager = new JLabel("Phone number");
    JLabel emailLabelManager = new JLabel("Email");
    JLabel nationalityLabelManager = new JLabel("Nationality");
    JLabel salaryLabelManager = new JLabel("Salary");

    JTextField firstNameTFieldManager = new JTextField();
    JTextField lastNameTFieldManager = new JTextField();
    JTextField phoneNumberTFieldManager = new JTextField();
    JTextField emailTFieldManager = new JTextField();
    JTextField nationalityTFieldManager = new JTextField();
    JTextField salaryTFieldManager = new JTextField();

    JTable tableManagers= new JTable();  //JTable([n],[r][n]);
    JScrollPane scrollerManagers = new JScrollPane(tableManagers);

    JButton addButtonManager = new JButton("Add");
    JButton deleteButtonManager = new JButton("Delete");
    JButton editButtonManager = new JButton("Edit");

    public ManagersPanel(){

    }

    public ManagersPanel(JPanel managerPanel) {
        managerPanel.setLayout(new GridLayout(1,2));
        managerPanel.add(leftPanelManagers, LEFT_ALIGNMENT);
        managerPanel.add(rightPanelManagers, RIGHT_ALIGNMENT);

        leftPanelManagers.setLayout(new GridLayout(2,1));
        leftPanelManagers.add(upPanelManagers);
        leftPanelManagers.add(downPanelManagers);

        upPanelManagers.setLayout(new GridLayout(6,1));
        upPanelManagers.add(firstNameLabelManager);
        upPanelManagers.add(firstNameTFieldManager);
        upPanelManagers.add(lastNameLabelManager);
        upPanelManagers.add(lastNameTFieldManager);
        upPanelManagers.add(phoneNumberLabelManager);
        upPanelManagers.add(phoneNumberTFieldManager);
        upPanelManagers.add(emailLabelManager);
        upPanelManagers.add(emailTFieldManager);
        upPanelManagers.add(nationalityLabelManager);
        upPanelManagers.add(nationalityTFieldManager);
        upPanelManagers.add(salaryLabelManager);
        upPanelManagers.add(salaryTFieldManager);

        downPanelManagers.add(addButtonManager);
        addButtonManager.addActionListener(new AddManager());
        downPanelManagers.add(deleteButtonManager);
        deleteButtonManager.addActionListener(new DeleteManager());
        deleteButtonManager.setEnabled(false);
        downPanelManagers.add(editButtonManager);
        editButtonManager.addActionListener(new EditManager());
        editButtonManager.setEnabled(false);

        tableManagers.setModel(Connector.selectAll("managers"));
        tableManagers.removeColumn(tableManagers.getColumnModel().getColumn(0));
        scrollerManagers.setPreferredSize(new Dimension(800, 400));
        rightPanelManagers.add(scrollerManagers);
        tableManagers.addMouseListener(new TableClickManagers());
    }

    private class AddManager implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameTFieldManager.getText();
            String lastName = lastNameTFieldManager.getText();
            String phoneNumber = phoneNumberTFieldManager.getText();
            String email = emailTFieldManager.getText();
            String nationality = nationalityTFieldManager.getText();
            double salary = Double.parseDouble(salaryTFieldManager.getText());

            String sql = "insert into managers(first_name,last_name,phone_number,email,nationality,salary)\n" +
                    "        values(?, ?, ?, ?, ?, ?);";

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, phoneNumber);
                statement.setString(4, email);
                statement.setString(5, nationality);
                statement.setDouble(6, salary);
                try{
                    statement.execute();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(downPanelManagers,
                            "We already have manager with this phone number or email.");
                    return;
                }
                tableManagers.setModel(Connector.selectAll("managers"));
                tableManagers.removeColumn(tableManagers.getColumnModel().getColumn(0));
                projects.tableProjects.setModel(Connector.selectAll("projects"));
                projects.tableProjects.removeColumn(projects.tableProjects.getColumnModel().getColumn(0));
            } catch (SQLException s) {
                s.printStackTrace();
            }
            projects.fillManagers();
            clearManagerFields();
        }
    }

    private class DeleteManager implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connector.deleteRow("managers", id);
            tableManagers.setModel(Connector.selectAll("managers"));
            tableManagers.removeColumn(tableManagers.getColumnModel().getColumn(0));
            projects.tableProjects.setModel(Connector.selectAll("projects"));
            projects.tableProjects.removeColumn(projects.tableProjects.getColumnModel().getColumn(0));
            id = -1;
            projects.fillManagers();
            clearManagerFields();
            deleteButtonManager.setEnabled(false);
            editButtonManager.setEnabled(false);
        }
    }

    private class EditManager implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameTFieldManager.getText();
            String lastName = lastNameTFieldManager.getText();
            String phoneNumber = phoneNumberTFieldManager.getText();
            String email = emailTFieldManager.getText();
            String nationality = nationalityTFieldManager.getText();
            double salary = Double.parseDouble(salaryTFieldManager.getText());

            String sql = "UPDATE managers SET first_name = ?, last_name = ?, phone_number = ?, email = ?, nationality = ?, salary = ? WHERE id = " + id;

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
                statement.setDouble(6,salary);
                try{
                    statement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(downPanelManagers,
                            "We already have manager with this phone number or email.");
                    return;
                }
                tableManagers.setModel(Connector.selectAll("managers"));
                tableManagers.removeColumn(tableManagers.getColumnModel().getColumn(0));
                projects.tableProjects.setModel(Connector.selectAll("projects"));
                projects.tableProjects.removeColumn(projects.tableProjects.getColumnModel().getColumn(0));
                id = -1;
                clearManagerFields();
            }catch (SQLException s){
                s.printStackTrace();
            }
            projects.fillManagers();
            clearManagerFields();
            editButtonManager.setEnabled(false);
            deleteButtonManager.setEnabled(false);
        }
    }


    private class TableClickManagers extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tableManagers.getSelectedRow();
            Object value = tableManagers.getModel().getValueAt(tableManagers.getSelectedRow(),0);
            id = Integer.parseInt(value.toString());
            System.out.println(id);

            if(e.getClickCount() > 1){
                deleteButtonManager.setEnabled(true);
                editButtonManager.setEnabled(true);
                firstNameTFieldManager.setText(tableManagers.getValueAt(row,0).toString());
                lastNameTFieldManager.setText(tableManagers.getValueAt(row, 1).toString());
                phoneNumberTFieldManager.setText(tableManagers.getValueAt(row,2).toString());
                emailTFieldManager.setText(tableManagers.getValueAt(row,3).toString());
                nationalityTFieldManager.setText(tableManagers.getValueAt(row,4).toString());
                salaryTFieldManager.setText(tableManagers.getValueAt(row,5).toString());
            }

//            if(SwingUtilities.isRightMouseButton(e)){
//
//            }


            tableManagers.setModel(Connector.selectAll("managers"));
            tableManagers.removeColumn(tableManagers.getColumnModel().getColumn(0));
            projects.tableProjects.setModel(Connector.selectAll("projects"));
            projects.tableProjects.removeColumn(projects.tableProjects.getColumnModel().getColumn(0));
        }
    }

    public void clearManagerFields(){
        firstNameTFieldManager.setText(" ");
        lastNameTFieldManager.setText(" ");
        phoneNumberTFieldManager.setText(" ");
        emailTFieldManager.setText(" ");
        nationalityTFieldManager.setText(" ");
        salaryTFieldManager.setText(" ");
    }
}
