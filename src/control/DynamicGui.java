package control;

import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import model.OurDatabase;

public class DynamicGui  {
	
	OurDatabase d = new OurDatabase();
	private ArrayList<String> databaseNames;
	private String currentSelected1;
	private String currentSelected2;
	private Object[][] data = null;
	private boolean check = false;
	
	public void selected(String query) {
		char c= query.charAt(0);
		if(c == 'c' || c == 'C'){
			createTable(query);
		}
		else if (c == 's' || c == 'S') {
			select(query);
			check = true;
		}
		else if (c == 'u' || c == 'U' || c == 'i' || c == 'I' || c == 'd' || c == 'D') {
			update(query);
		}
	}
	
	public void createDatabase(String databaseName){
		d.createDatabase(databaseName, true);
	}
	
	public void dropDatabase(String databaseName){
		try {
			
			d.executeStructureQuery("drop database " + databaseName + ';');
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dropTable(String tableName){
		try {
			
			d.executeStructureQuery("drop table " + tableName + ';');
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createTable(String query){
		try {
			
			d.executeStructureQuery(query);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void select(String query){
		try {
			data = d.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(String query){
		try {
			d.executeUpdateQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setCurrentDatabase() {
		d.current(currentSelected1);
	}
	
	public void dynamicDatabases(JPanel p,JPanel p1){
		p.removeAll();
		databaseNames = d.databaseNames();
		for(int i = 0 ; i < databaseNames.size() ; i++){
			JButton temp = new JButton(databaseNames.get(i));
			temp.setContentAreaFilled(false);
			temp.setFocusPainted(false);
			temp.setBorderPainted(false);
			temp.setForeground(Color.BLACK);
			temp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					currentSelected1 = temp.getText();
					setCurrentDatabase();
					dynamicTables(p1);
				}
			});
			p.add(temp);
			p.validate();
			p.repaint();
		}
		dynamicTables(p1);
	}
	
	public void dynamicTables(JPanel p){
		
		p.removeAll();
		ArrayList[] tableNames = d.tableNames();
		for(int k=0;k<databaseNames.size();k++){
			
			if(databaseNames.get(k).equalsIgnoreCase(currentSelected1)){
				
				for(int i = 0 ; i < tableNames[k].size() ; i++){
					
					JButton temp = new JButton((String)tableNames[k].get(i));
					temp.setContentAreaFilled(false);
					temp.setFocusPainted(false);
					temp.setBorderPainted(false);
					temp.setForeground(Color.BLACK);
					temp.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							currentSelected2 = temp.getText();
						}
					});
					p.add(temp);
					p.validate();
					p.repaint();
				}
			}
		}
		p.validate();
		p.repaint();
	}
	
	public void setData(JPanel p){
		p.removeAll();
		if(check){
			
			Object[] colNames = new Object[data[0].length]; 
			Object[][] Data = new Object[data.length-1][data[0].length];
			for(int i=0;i<colNames.length;i++){
				colNames[i] = data[0][i];
			}
			for(int i=1;i<data.length;i++){
				for(int j=0;j<data[0].length;j++){
					Data[i-1][j] = data[i][j];
				}
			}
			System.out.println(colNames[0]);
			JTable table = new JTable(Data, colNames);
			p.add(new JScrollPane(table));
			p.validate();
			p.repaint();
		}
	}
	
	
	

}
