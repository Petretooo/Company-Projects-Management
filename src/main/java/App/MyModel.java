package App;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


public class MyModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ResultSet result;	
	private int rowCount;	
	private int columnCount;
	private ArrayList<Object> data=new ArrayList<Object>();

	private String tableN;

	 public MyModel(ResultSet rs) throws Exception
	 {
		 setRS(rs);
	 }// end constructor

	public MyModel(String tableName, ResultSet rs) throws Exception {
	 	this(rs);
	 	this.tableN = tableName;
	}


	 public void setRS(ResultSet rs) throws Exception
	 {

		 this.result=rs;
		 ResultSetMetaData metaData=rs.getMetaData();
		 rowCount=0;
		 columnCount=metaData.getColumnCount();
		 while(rs.next()){

			 Object[] row=new Object[columnCount];

			 for(int j=0;j<columnCount;j++){
			 row[j]=rs.getObject(j+1);
			 }			 
			 data.add(row);
			 rowCount++;
		}// while

	 }// end setRS - filling ArrayList with ResultSet values

	 public int getColumnCount(){
		 return columnCount;
	 }
	 
	 public int getRowCount(){
		 return rowCount;
	 }
	 
	 public Object getValueAt(int rowIndex, int columnIndex){
		 Object[] row=(Object[]) data.get(rowIndex);
		 return row[columnIndex];
	 }

	String[] employees = {"Id", "First name", "Last name", "Phone Number", "Email", "Nationality", "Date of Birth", "Position", "Salary"};
	String[] managers = {"Id", "First name", "Last name", "Phone Number", "Email", "Nationality", "Salary"};
	String[] projects = {"Id", "Name", "Description", "Manager Id", "Manager Name"};
	String[] projectEmployees = {"Project name", "Id Project", "Employee Name", "Id Employee",};

	String[] m1 = {"Manager first name", "Manager last name", "Project"};
	String[] m2 = {"Manager first name", "Manager last name", "Employee first name", "Employee last name", "Project"};
	 public String getColumnName(int columnIndex){
		 try{
		 ResultSetMetaData metaData=result.getMetaData();

			 if(tableN.equals("employees")){
				 return employees[columnIndex];
			 }else if(tableN.equals("managers")){
				return managers[columnIndex];
			 }else if(tableN.equals("projects")){
				return projects[columnIndex];
			 }else if(tableN.equals("project_employees")){
				return projectEmployees[columnIndex];
			 }

			 if(tableN.equals("m1")){
			 	return m1[columnIndex];
			 }else if(tableN.equals("m2")){
			 	return m2[columnIndex];
			 }

			 return metaData.getColumnName(columnIndex+1);

		 }
		 catch(Exception e){
			 e.printStackTrace();
			 return null;
		 }
	 }// end getColumnName






} // end class App.MyModel