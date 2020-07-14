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

public class ProjectsPanel {

    Connection connection = null;
    PreparedStatement statement = null;
    int id = -1;
    int idSecond = -1;
    ProjectEmployeesPanel projectEmployees;

    JPanel leftPanelProject = new JPanel();
    JPanel rightPanelProject  = new JPanel();

    JPanel upPanelProject  = new JPanel();
    JPanel downPanelProject = new JPanel();

    JLabel nameLabelProject = new JLabel("Name");
    JLabel descriptionLabelProject = new JLabel("Description");
    JLabel managerLabelProject = new JLabel("Manager");

    JTextField nameTFieldProject = new JTextField();
    JTextField descriptionTFieldProject = new JTextField();
    JComboBox managerName = new JComboBox();

    JTable tableProjects = new JTable();  //JTable([n],[r][n]);
    JScrollPane scrollerProjects = new JScrollPane(tableProjects);

    JButton addButtonProject = new JButton("Add");
    JButton deleteButtonProject = new JButton("Delete");
    JButton editButtonProject = new JButton("Edit");

    public ProjectsPanel() {
    }

    public ProjectsPanel(JPanel projectPanel) {
        projectPanel.setLayout(new GridLayout(1,2));
        projectPanel.add(leftPanelProject, LEFT_ALIGNMENT);
        projectPanel.add(rightPanelProject, RIGHT_ALIGNMENT);

        leftPanelProject.setLayout(new GridLayout(2,1));
        leftPanelProject.add(upPanelProject);
        leftPanelProject.add(downPanelProject);

        upPanelProject.setLayout(new GridLayout(4,1));
        upPanelProject.add(nameLabelProject);
        upPanelProject.add(nameTFieldProject);
        upPanelProject.add(descriptionLabelProject);
        upPanelProject.add(descriptionTFieldProject);
        upPanelProject.add(managerLabelProject);
        upPanelProject.add(managerName);
        fillManagers();

        downPanelProject.add(addButtonProject);
        addButtonProject.addActionListener(new AddProject());
        downPanelProject.add(deleteButtonProject);
        deleteButtonProject.addActionListener(new DeleteProject());
        deleteButtonProject.setEnabled(false);
        downPanelProject.add(editButtonProject);
        editButtonProject.addActionListener(new EditProject());
        editButtonProject.setEnabled(false);

        tableProjects.setModel(Connector.selectAll("projects"));
        tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(0));
        tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(2));
        scrollerProjects.setPreferredSize(new Dimension(500, 400));
        rightPanelProject.add(scrollerProjects);
        tableProjects.addMouseListener(new TableClickProjects());
    }

    private class AddProject implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameTFieldProject.getText();
            String description = descriptionTFieldProject.getText();

            String sample = managerName.getSelectedItem().toString();
            String nameManager = sample.substring(sample.indexOf("Name: "), sample.indexOf("Email: ")).replace("Name: ", "").replace("           ", "");
            String emailManager = sample.substring(sample.indexOf("Email: ")).replace("Email: ", "");

            String sqlId = "select id from managers where email = ?";
            int idManager = 0;
            try {
                Connector.createConnection();
                statement = connection.prepareStatement(sqlId);
                statement.setString(1,emailManager);
                ResultSet r = statement.executeQuery();
                while (r.next()){
                    idManager = r.getInt("id");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


            connection = getConnection();

            String sql = "insert into projects(name,description,manager_id,manager_name)\n" +
                    "        values(?, ?, ?, ?);";

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try {
                statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, description);
                statement.setInt(3,idManager);
                statement.setString(4, nameManager);
                try{
                    statement.execute();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(downPanelProject,
                            "We can't have the same project twice or more!");
                    return;
                }

                tableProjects.setModel(Connector.selectAll("projects"));
                tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(0));
                tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(2));
                projectEmployees.tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
                projectEmployees.tableProjectsEmployees.removeColumn(projectEmployees.tableProjectsEmployees.getColumnModel().getColumn(1));
                projectEmployees.tableProjectsEmployees.removeColumn(projectEmployees.tableProjectsEmployees.getColumnModel().getColumn(2));

            } catch (SQLIntegrityConstraintViolationException s) {
                s.printStackTrace();
            } catch (SQLException s){
                s.printStackTrace();
            }
            projectEmployees.fillProjects();
            clearProjectFields();
        }
    }

    private class DeleteProject implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connector.deleteRow("projects", id);
            tableProjects.setModel(Connector.selectAll("projects"));
            tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(0));
            tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(2));
            projectEmployees.tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
            projectEmployees.tableProjectsEmployees.removeColumn(projectEmployees.tableProjectsEmployees.getColumnModel().getColumn(1));
            projectEmployees.tableProjectsEmployees.removeColumn(projectEmployees.tableProjectsEmployees.getColumnModel().getColumn(2));
            id = -1;
            clearProjectFields();
            projectEmployees.fillProjects();
            deleteButtonProject.setEnabled(false);
            editButtonProject.setEnabled(false);
        }
    }

    private class EditProject implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameTFieldProject.getText();
            String description = descriptionTFieldProject.getText();

            String sample = managerName.getSelectedItem().toString();
            String nameManager = sample.substring(sample.indexOf("Name: "), sample.indexOf("Email: ")).replace("Name: ", "").replace("           ", "");
            String emailManager = sample.substring(sample.indexOf("Email: ")).replace("Email: ", "");

            String sqlId = "select id from managers where email = ?";
            int idManager = 0;
            try {
                Connector.createConnection();
                statement = connection.prepareStatement(sqlId);
                statement.setString(1,emailManager);
                ResultSet r = statement.executeQuery();
                while (r.next()){
                    idManager = r.getInt("id");
                    break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sql = "UPDATE projects SET name = ?, description = ?, manager_id = ?, manager_name = ? WHERE id = " + id;

            try {
                Connector.createConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            connection = getConnection();
            try{
                statement = connection.prepareStatement(sql);
                statement.setString(1,name);
                statement.setString(2,description);
                statement.setInt(3,idManager);
                statement.setString(4,nameManager);
                try{
                    statement.executeUpdate();
                }catch (SQLIntegrityConstraintViolationException s){
                    JOptionPane.showMessageDialog(downPanelProject,
                            "We can't have the same project twice or more!");
                    return;
                }
                tableProjects.setModel(Connector.selectAll("projects"));
                tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(0));
                tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(2));
                projectEmployees.tableProjectsEmployees.setModel(Connector.selectAll("project_employees"));
                projectEmployees.tableProjectsEmployees.removeColumn(projectEmployees.tableProjectsEmployees.getColumnModel().getColumn(1));
                projectEmployees.tableProjectsEmployees.removeColumn(projectEmployees.tableProjectsEmployees.getColumnModel().getColumn(2));
                id = -1;
                clearProjectFields();
            }catch (SQLException s){
                s.printStackTrace();
            }
            projectEmployees.fillProjects();
            clearProjectFields();
            editButtonProject.setEnabled(false);
            deleteButtonProject.setEnabled(false);
        }
    }

    private class TableClickProjects extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tableProjects.getSelectedRow();
            Object value = tableProjects.getModel().getValueAt(tableProjects.getSelectedRow(),0);
            id = Integer.parseInt(value.toString());
            System.out.println(id);

            if(e.getClickCount() > 1){
                deleteButtonProject.setEnabled(true);
                editButtonProject.setEnabled(true);
                nameTFieldProject.setText(tableProjects.getValueAt(row,0).toString());
                descriptionTFieldProject.setText(tableProjects.getValueAt(row, 1).toString());
                String name = tableProjects.getValueAt(row,2).toString();
                for (int i = 0; i < managerName.getItemCount(); i++) {
                    String sample = managerName.getItemAt(i).toString();
                    if(!sample.isEmpty()) {
                        String nameManager = sample.substring(sample.indexOf("Name: "), sample.indexOf("Email: ")).replace("Name: ", "").replace("           ","");
                        if(nameManager.equals(name)) {
                            managerName.setSelectedIndex(i);
                            break;
                        }
                    }

                }

            }

//            if(SwingUtilities.isRightMouseButton(e)){
//
//            }
            tableProjects.setModel(Connector.selectAll("projects"));
            tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(0));
            tableProjects.removeColumn(tableProjects.getColumnModel().getColumn(2));

        }
    }

    public void clearProjectFields(){
        nameTFieldProject.setText(" ");
        descriptionTFieldProject.setText(" ");
        managerName.setSelectedIndex(0);
    }

    public void fillManagers() {
        managerName.removeAllItems();
        String sql = "SELECT * FROM managers;";
        connection = getConnection();
        ResultSet result = null;
        managerName.addItem("");
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()){
                String fullManager ="Name: "+ result.getString(2) + " " + result.getString(3) + "           Email: " + result.getString(5);
                managerName.addItem(fullManager);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
}
