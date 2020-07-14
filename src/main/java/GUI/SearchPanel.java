package GUI;

import App.Connector;
import App.SearchingQueries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SearchPanel {

    //SearchingQueries searchingQueries = null;
    Connection connection = null;
    PreparedStatement statement = null;
    int id = -1;
    int idSecond = 01;

    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    //MID PANEL
    JPanel midLeftPanel = new JPanel();
    JPanel midRightPanel = new JPanel();
    //MID PANEL

    //searching by two criterias
    JLabel tableLabel = new JLabel("Table");
    JLabel categporyLabel = new JLabel("Category");
    JLabel textLabel = new JLabel("Text");
    JLabel secondCategporyLabel = new JLabel("Category");
    JLabel secondTextLabel = new JLabel("Text");

    String[] tables = {"", "Employees", "Managers", "Projects", "Employees Involved"};
    JComboBox tableComboBox = new JComboBox(tables);;
    String[] employeesCategory = {"", "First name", "Last name", "Phone number", "Email", "Nationality", "Position"};
    String[] managersCategory = {"", "First name", "Last name", "Phone number", "Email", "Nationality", "Position"};
    String[] projectCategory = {"", "Name", "Manager name"};
    String[] projectEmployeesCategpry = {"", "Project name", "Employee name"};
    JComboBox categoryComboBox = new JComboBox();
    JTextField textTField = new JTextField();
    JComboBox secondCategoryComboBox = new JComboBox();
    JTextField secondTextField = new JTextField();
    //Searhing by two criterias END

    //First table from the two
    JLabel twoTableLabel = new JLabel("Table");
    //JLabel twoCategoryLabel = new JLabel("Category");
    JLabel twoTextLabel = new JLabel("Text");

    String[] twoTables = {"","Managers"};
    JComboBox twoTableComboBox = new JComboBox(twoTables);
    //JComboBox twoCategoryComboBox = new JComboBox();
    JTextField twoTextFiled = new JTextField();
    //end

    //second table from the two
    JLabel threeTableLabel = new JLabel("Table");
    //JLabel threeCategoryLabel = new JLabel("Category");
    JLabel threeTextLabel = new JLabel("Text");

    String[] threeTables = {"","Employees","Projects"};
    JComboBox threeTableComboBox = new JComboBox(threeTables);
    //JComboBox threeCategoryComboBox = new JComboBox();
    JTextField threeTextFiled = new JTextField();
    //end

    JTable tableSearch = new JTable();  //JTable([n],[r][n]);
    JScrollPane scrollerSearch = new JScrollPane(tableSearch);

    //Buttons
    JCheckBox enableTables = new JCheckBox("Enable Tables");
    JButton searchButton = new JButton("Search");
    //end

    public SearchPanel(JPanel serchPanel) {

        serchPanel.setLayout(new GridLayout(1, 2));
        serchPanel.add(leftPanel, Component.LEFT_ALIGNMENT);
        serchPanel.add(rightPanel, Component.RIGHT_ALIGNMENT);

        //Searching by two parameters
        leftPanel.setLayout(new GridLayout(3, 1));
        leftPanel.add(upPanel);
        leftPanel.add(midPanel);
        leftPanel.add(downPanel);

        upPanel.setLayout(new GridLayout(6, 1));
        upPanel.add(tableLabel);
        upPanel.add(tableComboBox);
        tableComboBox.addActionListener(new ChooseCategory());
        upPanel.add(categporyLabel);
        upPanel.add(categoryComboBox);
        categoryComboBox.addActionListener(new unclockSecondCategory());
        upPanel.add(textLabel);
        upPanel.add(textTField);
        upPanel.add(secondCategporyLabel);
        upPanel.add(secondCategoryComboBox);
        secondCategoryComboBox.setEnabled(false);
        upPanel.add(secondTextLabel);
        secondTextField.setEnabled(false);
        upPanel.add(secondTextField);
        //Searching by two parameters END

        //The two tables
        midPanel.setLayout(new GridLayout(1,2));
        midPanel.add(midLeftPanel, Component.LEFT_ALIGNMENT);
        midPanel.add(midRightPanel, Component.RIGHT_ALIGNMENT);
        midLeftPanel.setLayout(new GridLayout(3,1));
        midLeftPanel.add(twoTableLabel);
        midLeftPanel.add(twoTableComboBox);
        midLeftPanel.add(twoTextLabel);
        midLeftPanel.add(twoTextFiled);
        midRightPanel.setLayout(new GridLayout(3,1));
        midRightPanel.add(threeTableLabel);
        midRightPanel.add(threeTableComboBox);
        midRightPanel.add(threeTextLabel);
        midRightPanel.add(threeTextFiled);

        twoTableComboBox.setEnabled(false);
        twoTextFiled.setEnabled(false);
        threeTableComboBox.setEnabled(false);
        threeTextFiled.setEnabled(false);
        //The two tables

        //Buttons
        downPanel.add(enableTables);
        enableTables.addActionListener(new UnlockSecondTable());
        downPanel.add(searchButton);
        searchButton.addActionListener(new searching());
        //Butoons

        scrollerSearch.setPreferredSize(new Dimension(800, 400));
        rightPanel.add(scrollerSearch);
    }

    private class searching implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (checkIfFiledsNull()) {
                return;
            }



            if(!enableTables.isSelected()) {

                String tableName = tableComboBox.getSelectedItem().toString();
                String category = categoryComboBox.getSelectedItem().toString();
                String inputedText = textTField.getText();


                if (categoryComboBox.getSelectedItem() == null || categoryComboBox.getSelectedItem().toString().isEmpty() && textTField.getText().isEmpty()) {
                    onlyTablesSelected(tableName);
                } else if ((categoryComboBox.getSelectedItem() != null && !textTField.getText().isEmpty()) && (secondCategoryComboBox.getSelectedItem() != null && !secondTextField.getText().isEmpty())) {
                    searchingByTwo(tableName, category, inputedText);

                } else if (categoryComboBox.getSelectedItem() != null && !textTField.getText().isEmpty()) {
                    searchingByOne(tableName, category, inputedText);
                }
            }else{
                //TODO

                String twoTableName = twoTableComboBox.getSelectedItem().toString();
                String twoInputedText = twoTextFiled.getText();

                String threeTableName = threeTableComboBox.getSelectedItem().toString();
                String threeInputedText = threeTextFiled.getText();

                if(twoTableName.equals("Managers")){
                    if(threeTableName.equals("Employees")){
                        tableSearch.setModel(SearchingQueries.searchingBetweenFourTables(twoInputedText, threeInputedText));
                    }else if(threeTableName.equals("Projects")){
                        tableSearch.setModel(SearchingQueries.searchingBetweenTwoTables(twoInputedText, threeInputedText));
                    }
                }



            }



        }
    }

    private boolean checkIfFiledsNull() {
        if(!enableTables.isSelected()) {
            if (tableComboBox.getSelectedItem() == null || categoryComboBox.getSelectedItem() == null || textTField.getText() == null) {
                JOptionPane.showMessageDialog(downPanel,
                        "Ups.. Write something, damn it! And be cool ;)");
                return true;
            }
        }else{
            if(twoTableComboBox.getSelectedItem() == null || twoTextFiled.getText() == null){
                JOptionPane.showMessageDialog(downPanel,
                        "Ups.. Write something, damn it! And be cool ;)");
                return true;
            }else  if(threeTableComboBox.getSelectedItem() == null || threeTextFiled == null){
                JOptionPane.showMessageDialog(downPanel,
                        "Ups.. Write something, damn it! And be cool ;)");
                return true;
            }
        }
        return false;
    }

    private void searchingByOne(String tableName, String category, String inputedText) {
        if (tableName.isEmpty()) {
            //DO NOTHING
        } else if (tableName.equals("Employees")) {
            tableName = tableName.toLowerCase();

            if (category.equals("First name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "first_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Last name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "last_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Phone number")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "phone_number", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Email")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "email", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Position")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "position", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            }else if(category.equals("Nationality")){
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "nationality", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            }

        } else if (tableName.equals("Managers")) {
            tableName = tableName.toLowerCase();

            if (category.equals("First name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "first_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Last name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "last_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Phone number")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "phone_number", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Email")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "email", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            } else if (category.equals("Position")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "position", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            }else if(category.equals("Nationality")){
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "nationality", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            }

        } else if (tableName.equals("Projects")) {
            tableName = tableName.toLowerCase();

            if (category.equals("Name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
            } else if (category.equals("Manager name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCriteryLike(tableName, "manager_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
            }
        } else if (tableName.equals("Employees Involved")) {
            tableName = "project_employees";

            if (category.equals("Project name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCritery(tableName, "project_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
            } else if (category.equals("Employee name")) {
                tableSearch.setModel(SearchingQueries.selectByOneCriteryLike(tableName, "employee_name", inputedText));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
                tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
            }
        }
    }

    private void searchingByTwo(String tableName, String category, String inputedText) {
        String inputedSecondText = secondTextField.getText();
        if (tableName.isEmpty()) {
            //DO NOTHING
        } else if (tableName.equals("Employees")) {
            tableName = tableName.toLowerCase();

            if (category.equals("First name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Last name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Phone number")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Email")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Position")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            }else if(category.equals("Nationality")){
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            }

        } else if (tableName.equals("Managers")) {
            tableName = tableName.toLowerCase();

            if (category.equals("First name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "first_name", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Last name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "last_name", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Phone number")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "phone_number", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Email")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "email", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            } else if (category.equals("Position")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "position", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            }else if(category.equals("Nationality")){
                if(secondCategoryComboBox.getSelectedItem().toString().equals("First name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "first_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Last name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "last_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Phone number")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "phone_number", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Email")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "email", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Nationality")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "nationality", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("position")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "nationality", "position", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                }
            }

        } else if (tableName.equals("Projects")) {
            tableName = tableName.toLowerCase();

            if (category.equals("Name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("Name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "name", "name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Manager name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteryLike(tableName, "name", "manager_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }
            } else if (category.equals("Manager name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("Name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteryLike(tableName, "manager_name", "name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Manager name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteryLike(tableName, "manager_name", "manager_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }

            }
        } else if (tableName.equals("Employees Involved")) {
            tableName = "project_employees";

            if (category.equals("Project name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("Project name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteries(tableName, "project_name", "project_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Employee name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteryLike(tableName, "project_name", "employee_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }
            } else if (category.equals("Employee name")) {
                if(secondCategoryComboBox.getSelectedItem().toString().equals("Project name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteryLike(tableName, "employee_name", "project_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }else if(secondCategoryComboBox.getSelectedItem().toString().equals("Employee name")){
                    tableSearch.setModel(SearchingQueries.selectByTwoCriteryLike(tableName, "employee_name", "employee_name", inputedText, inputedSecondText));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
                    tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
                }
            }
        }
    }

    private void onlyTablesSelected(String tableName) {
        if (tableName.isEmpty()) {
            //DO NOTHING
        } else if (tableName.equals("Employees")) {
            tableName = tableName.toLowerCase();

            tableSearch.setModel(Connector.selectAll(tableName));
            tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
        } else if (tableName.equals("Managers")) {
            tableName = tableName.toLowerCase();

            tableSearch.setModel(Connector.selectAll(tableName));
            tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
        } else if (tableName.equals("Projects")) {
            tableName = tableName.toLowerCase();

            tableSearch.setModel(Connector.selectAll(tableName));
            tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(0));
            tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
        } else if (tableName.equals("Employees Involved")) {
            tableName = "project_employees";

            tableSearch.setModel(Connector.selectAll(tableName));
            tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(1));
            tableSearch.removeColumn(tableSearch.getColumnModel().getColumn(2));
        }
    }


    private class ChooseCategory implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            categoryComboBox.removeAllItems();
            String tableName = tableComboBox.getSelectedItem().toString();
            if (tableName.isEmpty()) {
                //DO NOTHING
            } else if (tableName.equals("Employees")) {
                for (String s : employeesCategory) {
                    categoryComboBox.addItem(s);
                }
            } else if (tableName.equals("Managers")) {
                for (String s : managersCategory) {
                    categoryComboBox.addItem(s);
                }
            } else if (tableName.equals("Projects")) {
                for (String s : projectCategory) {
                    categoryComboBox.addItem(s);
                }
            } else if (tableName.equals("Employees Involved")) {
                for (String s : projectEmployeesCategpry) {
                    categoryComboBox.addItem(s);
                }
            }
        }
    }

    private class unclockSecondCategory implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            secondCategoryComboBox.removeAllItems();
            String tableName = tableComboBox.getSelectedItem().toString();
            if(categoryComboBox.getSelectedItem() != null) {
                if (tableName.isEmpty() || categoryComboBox.getSelectedItem().toString().isEmpty()) {
                    //DO NOTHING
                    secondCategoryComboBox.setEnabled(false);
                    secondTextField.setEnabled(false);
                } else if (tableName.equals("Employees")) {
                    secondCategoryComboBox.setEnabled(true);
                    secondTextField.setEnabled(true);

                    for (String s : employeesCategory) {
                        secondCategoryComboBox.addItem(s);
                    }
                } else if (tableName.equals("Managers")) {
                    secondCategoryComboBox.setEnabled(true);
                    secondTextField.setEnabled(true);

                    for (String s : managersCategory) {
                        secondCategoryComboBox.addItem(s);
                    }
                } else if (tableName.equals("Projects")) {
                    secondCategoryComboBox.setEnabled(true);
                    secondTextField.setEnabled(true);

                    for (String s : projectCategory) {
                        secondCategoryComboBox.addItem(s);
                    }
                } else if (tableName.equals("Employees Involved")) {
                    secondCategoryComboBox.setEnabled(true);
                    secondTextField.setEnabled(true);

                    for (String s : projectEmployeesCategpry) {
                        secondCategoryComboBox.addItem(s);
                    }
                }
            }


        }

    }


    private class UnlockSecondTable implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if(enableTables.isSelected()){

                tableComboBox.setEnabled(false);
                categoryComboBox.setEnabled(false);
                textTField.setEnabled(false);
                secondCategoryComboBox.setEnabled(false);
                secondTextField.setEnabled(false);


                twoTableComboBox.setEnabled(true);
                twoTextFiled.setEnabled(true);
                threeTableComboBox.setEnabled(true);
                threeTextFiled.setEnabled(true);
            }else {

                tableComboBox.setEnabled(true);
                categoryComboBox.setEnabled(true);
                textTField.setEnabled(true);
                secondCategoryComboBox.setEnabled(true);
                secondTextField.setEnabled(true);

                twoTableComboBox.setEnabled(false);
                twoTextFiled.setEnabled(false);
                threeTableComboBox.setEnabled(false);
                threeTextFiled.setEnabled(false);
            }

        }
    }









}
