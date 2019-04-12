package model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import control.Control;
import Interface.Database;

public class OurDatabase implements Database {

	
	private ArrayList<String> columnsNames = new ArrayList<String>();
	private ArrayList values = new ArrayList();
	private String conditionColumnsName = new String();
	private Object conditionValue = new Object();
	private File database;
	private String currentDataBase;
	
	private Control c = new Control();
	
	private Parser p = new Parser();

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		// TODO Auto-generated method stub

		databaseName = databaseName.toLowerCase();
		setCurrentDatabase();
		
		try {
			boolean dataBaseExist = c.databaseNames.contains(databaseName);
			if (dataBaseExist && dropIfExists) {
				executeStructureQuery("DROP DATABASE " + databaseName + ';');
				executeStructureQuery("CREATE DATABASE " + databaseName + ';');
				
			} 
			else {
				if (dataBaseExist) {
					currentDataBase = databaseName;
				}
				executeStructureQuery("CREATE DATABASE " + databaseName + ';');
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return database.getPath();
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		setCurrentDatabase();
		boolean b = false;
		boolean Check = false;
		String query1;
		query1 = p.create_dropCheck(query);
		//query1 = p.addSpaces(query);
		
		if (!query1.equals(null)) {
			
		
			// removing semicolon
			query1 = query1.substring(0, query1.length() - 1);
			String[] splitedQuery = query1.split("\\s+");
	
			if (splitedQuery[0].equalsIgnoreCase("create")) {
				
				if (splitedQuery[1].equalsIgnoreCase("DATABASE")) {
					
					currentDataBase = splitedQuery[2];
					
					c.setDatabaseName(currentDataBase);
					database = c.createDatabase();
					b = true;
				}
				else if (splitedQuery[1].equalsIgnoreCase("table")) {
					
					c.setDatabaseName(currentDataBase);
					columnsNames.clear();
					for(int i=4;i<splitedQuery.length;i+=3){
							if(splitedQuery[i].charAt(0) == ')'){
							break;
						}
						else if(splitedQuery[i].charAt(0) != ','){
							columnsNames.add(splitedQuery[i]);
						}
					}
					if(c.databaseNames.size()>0 && splitedQuery.length>=7){
						for(int i=0;i<c.tablesInDatabase.size();i++){
							if(c.tablesInDatabase.get(i).equalsIgnoreCase(currentDataBase)){
								if (c.tableNames.get(i).equalsIgnoreCase(splitedQuery[2])) {
									Check=true;
									b=false;
									break;
								}
							}
						}
						if(!Check){
							c.createTable(splitedQuery[2],columnsNames);		
						
							b = true;
						}
					}
					else{
						throw new SQLException();
					}
				
				}
			}
			else if (splitedQuery[0].equalsIgnoreCase("drop")) {
				if (splitedQuery[1].equalsIgnoreCase("database")) {
				
					
					
					c.setDatabaseName(splitedQuery[2]);
					c.dropDatabase(splitedQuery[2]);
					
					b = true;
				} 
				else if (splitedQuery[1].equalsIgnoreCase("table")) {	
					c.setDatabaseName(currentDataBase);
					
					if(c.databaseNames.size()>0){
						c.dropTable(splitedQuery[2]);	
					
						b = true;
					}
					else{
						b = false;
					}
					
				}
			}
			return b;
		}
		else{
			throw null;
		}
		
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		setCurrentDatabase();
		c.setDatabaseName(currentDataBase);
		
		String query1;
		query1 = p.selsctCheck(query);
		//query1 = p.addSpaces(query);
		
		if (!query1.equals(null)) {
			
		
			query1 = query1.substring(0, query1.length() - 1);
			String[] splitedQuery = query1.split("\\s+");
			
			Object[][] o;
			ArrayList[] data;
			
			if (splitedQuery[1].equalsIgnoreCase("*")) {
			
				data = c.selectFromTable(splitedQuery[splitedQuery.length-1]);
			}
			else{
				for(int i=1;i<splitedQuery.length;i++){
					if(splitedQuery[i].equalsIgnoreCase("from")){
					break;
				}
					else if(splitedQuery[i].charAt(0) != ','){
						columnsNames.add(splitedQuery[i]);
					}
				}
				
				data = c.selectFromTable1(splitedQuery[splitedQuery.length-1], columnsNames);
			}
			if(!data.equals(null)){
			o = new Object[data[0].size()][data.length];
			
			
				for(int i=0;i<data[0].size();i++){
					for(int j=0;j<data.length;j++){
						o[i][j] = data[j].get(i);
					}
				}
				return o;
			}
			else{
				throw new SQLException();
			}
			
		}
		else{
			throw new SQLException();
		}
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		//c.writeInXmlFiles(query);
		setCurrentDatabase();
		c.setDatabaseName(currentDataBase);
		int c1 = 0;
		String query1;
		query1 = p.insertCheck(query);
		//query1 = p.addSpaces(query);
		
		
		if (!query1.equals(null)) {
			query1 = query1.substring(0, query1.length() - 1);
			String[] splitedQuery = query1.split("\\s+");
			
			if (splitedQuery[0].equalsIgnoreCase("insert")) {
				values.clear();
				if (splitedQuery[3].equalsIgnoreCase("values")) {
					
					for(int i=5;i<splitedQuery.length;i++){
						if(splitedQuery[i].charAt(0) == ')'){
							break;
						}
						else if(splitedQuery[i].charAt(0) != ',' && splitedQuery[i].charAt(0) != '\''){
							values.add(splitedQuery[i]);
							String string = (String) values.get(values.size()-1);
							for(int k=i+1;k<splitedQuery.length;k++){
								i = k;
								if (splitedQuery[k].charAt(0) == '\'' || splitedQuery[k].charAt(0) == ')' || 
										splitedQuery[i].charAt(0) == ',') {
									break;
								}
								string = string + splitedQuery[k];
								values.set(values.size()-1, string);
							}
						}
					}
					
					
					
					c1 = c.insertData(splitedQuery[2], values);
				}
				else{
					int counter = 0;
					columnsNames.clear();
					
					for(int i=4;i<splitedQuery.length;i++){
						if(splitedQuery[i].charAt(0) == ')'){
							counter = i;
							break;
						}
						else if(splitedQuery[i].charAt(0) != ','){
							columnsNames.add(splitedQuery[i]);
						}
					}
					
					for(int i=counter+3;i<splitedQuery.length;i++){
						if(splitedQuery[i].charAt(0) == ')'){
							break;
						}
						else if(splitedQuery[i].charAt(0) != ',' && splitedQuery[i].charAt(0) != '\''){
							values.add(splitedQuery[i]);
							String string = (String) values.get(values.size()-1);
							for(int k=i+1;k<splitedQuery.length;k++){
								if (splitedQuery[k].charAt(0) == '\'' || splitedQuery[k].charAt(0) == ')' ||
										splitedQuery[i].charAt(0) != ',') {
									break;
								}
								string = string + splitedQuery[k];
								values.set(values.size()-1, string);
							}
						}
					}
					
					c1 = c.insertData1(splitedQuery[2], values, columnsNames);
				}
			}
			else if(splitedQuery[0].equalsIgnoreCase("update")){
				
				values.clear();
				columnsNames.clear();
				int counter = 0;
				boolean b1=false;
				boolean b2=false;
				
				columnsNames.add(splitedQuery[3]);
				
				for(int i=4;i<splitedQuery.length;i++){
					if (splitedQuery[i].equalsIgnoreCase("where")) {
						counter = i+1;
						b2 = true;
						break;
					}
					if(splitedQuery[i].equals(",")){
						columnsNames.add(splitedQuery[i+1]);
					}
					if(i == splitedQuery.length-1){
						b1 = true;
						break;
					}
				}
				
				for(int i=4;i<splitedQuery.length;i++){
					if (splitedQuery[i].equalsIgnoreCase("where")) {
						break;
					}
					if(splitedQuery[i].equals("=")){
						if(splitedQuery[i+1].equals("'")){
							values.add(splitedQuery[i+2]);
							String string = (String) values.get(values.size()-1);
							for(int k=i+3;k<splitedQuery.length;k++){
								i = k;
								if (splitedQuery[k].charAt(0) == '\'') {
									break;
								}
							string = string + splitedQuery[k];
							values.set(values.size()-1, string);
							}
						}
						else if(splitedQuery[i+1].equals("=")){
							values.add(splitedQuery[i+3]);
							String string = (String) values.get(values.size()-1);
							for(int k=i+4;k<splitedQuery.length;k++){
								i = k;
								if (splitedQuery[k].charAt(0) == '\'') {
									break;
								}
							string = string + splitedQuery[k];
							values.set(values.size()-1, string);
							}
						}
						else{
							values.add(splitedQuery[i+1]);
							i++;
						}
					}
				}
				
				if(b1 == true){
					c1 = c.updateData(splitedQuery[1], values, columnsNames);
					if(c1 == 0){
						throw new SQLException();
					}
				}
				else if(b2 == true){
					
					int i=counter,j=i+2;
					conditionColumnsName = splitedQuery[i];
					if(splitedQuery[j].equals("'")){
						conditionValue = splitedQuery[j+1];
						for(int k=j+2;k<splitedQuery.length;k++){
							if (splitedQuery[k].charAt(0) == '\'') {
								break;
							}
						conditionValue = conditionValue + splitedQuery[k];
						}
					}
					else {
						conditionValue = splitedQuery[j];
					}
					
					
					c1 = c.updateData1(splitedQuery[1], values, columnsNames, conditionValue, conditionColumnsName);
					if(c1 == 0){
						throw new SQLException();
					}
				}
				
			}
			else if(splitedQuery[0].equalsIgnoreCase("delete")){
				if(splitedQuery.length == 3 || splitedQuery.length == 4){
					c1 = c.deleteData(splitedQuery[splitedQuery.length-1]);
				}
				else if(splitedQuery.length == 7 || splitedQuery.length >= 9){
					conditionColumnsName = splitedQuery[4];
					if(splitedQuery.length == 7){
						conditionValue = splitedQuery[6];
					}
					else{
						conditionValue = splitedQuery[7];
						for(int j=8;j<splitedQuery.length;j++){
							if (splitedQuery[j].charAt(0) == '\'') {
								break;
							}
						conditionValue = conditionValue + splitedQuery[j];
						}
					}
					c1 = c.deleteData1(splitedQuery[2], conditionValue, conditionColumnsName);
				}
				else{
					throw new SQLException();
				}
			}
			
			return c1;
		}
		else{
			throw null;
		}
	}

	public void setCurrentDatabase(){
		File f = new File(c.dir + "databassename.xml");
		if(f.exists()){
			c.loadDatabaseNames();
		}
		File f1 = new File(c.dir + "tablename.xml");
		if(f1.exists()){
			c.loadTableNames();
		}
		
		
	}

	public void current(String s){
		currentDataBase = s;
	}

	
	public ArrayList<String> databaseNames(){
		File f = new File(c.dir + "databassename.xml");
		if(f.exists()){
			c.loadDatabaseNames();
		}
		
		return c.databaseNames;
		
	}
	
	
	public ArrayList[] tableNames(){
		
		
		File f = new File(c.dir + "databassename.xml");
		if(f.exists()){
			c.loadDatabaseNames();
		}
		
		int size = c.databaseNames.size();
		ArrayList[] arrayLists = new ArrayList[size];
		for(int i=0;i<size;i++){
			arrayLists[i] = new ArrayList();
		}
		
		File f1 = new File(c.dir + "tablename.xml");
		if(f1.exists()){
			c.loadTableNames();
		}
		
		for(int i=0;i<c.tablesInDatabase.size();i++){
			for(int j=0;j<size;j++){
				if(c.tablesInDatabase.get(i).equalsIgnoreCase(c.databaseNames.get(j))){
					arrayLists[j].add(c.tableNames.get(i));
				}
			}
		}
		
		return arrayLists;
		
	}
	
	
}
