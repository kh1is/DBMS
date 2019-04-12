package control;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Control {
	
	public String currentDataBase = "";
	public ArrayList<String> databaseNames = new ArrayList<String>();
	public ArrayList<String> tableNames = new ArrayList<String>();
	public ArrayList<String> tablesInDatabase = new ArrayList<String>();
	public String[] colNames;
	public ArrayList[] data;
	private String fs = File.separator;
	public String dir = System.getProperty("user.dir") + fs;
	
	private File database;
	
	public void setDatabaseName(String s){
		currentDataBase = s;
	}
	
	public void saveDatabaseNames(){
		try{
			FileOutputStream fos = new FileOutputStream(new File(dir + "databassename.xml"));
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(databaseNames.size());
			for(int i=0;i<databaseNames.size();i++){
				encoder.writeObject(databaseNames.get(i));
			}
			encoder.close();
			fos.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public void loadDatabaseNames(){
		databaseNames.clear();
		try{
			FileInputStream fis = new FileInputStream(new File(dir + "databassename.xml"));
			XMLDecoder decoder = new XMLDecoder(fis);
			int size = (int) decoder.readObject();
			for(int i=0;i<size;i++){
				databaseNames.add((String) decoder.readObject());
			}
			
			
			decoder.close();
			fis.close();
			
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	public void saveTableNames(){
		try{
			FileOutputStream fos = new FileOutputStream(new File(dir + "tablename.xml"));
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(tableNames.size());
			for(int i=0;i<tableNames.size();i++){
				encoder.writeObject(tablesInDatabase.get(i));
				encoder.writeObject(tableNames.get(i));
			}
			encoder.close();
			fos.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
		
	}
	
	public void loadTableNames(){
		tablesInDatabase.clear();
		tableNames.clear();
		try{
			FileInputStream fis = new FileInputStream(new File(dir + "tablename.xml"));
			XMLDecoder decoder = new XMLDecoder(fis);
			
			int size = (Integer) decoder.readObject();
			for(int i=0;i<size;i++){
				tablesInDatabase.add((String) decoder.readObject());
				tableNames.add((String) decoder.readObject());
			}
			
			
			decoder.close();
			fis.close();
			
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
		
	}
	
	public File createDatabase(){
		File f = new File(dir + "databassename.xml");
		if(f.exists()){
			loadDatabaseNames();
		}
		databaseNames.add(currentDataBase);
		database = new File(dir + "Databases" + fs + currentDataBase);
		database.mkdirs();
		saveDatabaseNames();
		return database;
		
	}
	
	public void createTable(String tableName,ArrayList<String> columneNames){
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		
		try {
			Formatter x = new Formatter(new File(dir + "Databases" + fs + currentDataBase + fs + tableName + ".txt"));
			for(int i=0 ; i<columneNames.size() ; i++){
				x.format("%s", columneNames.get(i));
				x.format(",");
			}
			x.close();
			
			File tableFile = new File(dir +"Databases" + fs + currentDataBase + fs + tableName + ".xml");
			FileOutputStream fos = new FileOutputStream(tableFile);
			
			XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();				
	        XMLStreamWriter xMLStreamWriter =
	        xMLOutputFactory.createXMLStreamWriter(fos);
	  
	        xMLStreamWriter.writeStartDocument();
	        xMLStreamWriter.writeStartElement("tablename");
	        xMLStreamWriter.writeEndDocument();

	        xMLStreamWriter.flush();
	        xMLStreamWriter.close();													
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tableNames.add(tableName);
		tablesInDatabase.add(currentDataBase);
		
		saveTableNames();
	}
	
	public void dropDatabase(String databaseName){
		// delete currentDataBase if it equals to the database name
		// which will be dropped
		File f = new File(dir + "databassename.xml");
		if(f.exists()){
			loadDatabaseNames();
		}
		File f1 = new File(dir + "tablename.xml");
		if(f1.exists()){
			loadTableNames();
		}
		
		for(int i=0;i<tablesInDatabase.size();i++){
			
			if(tablesInDatabase.get(i).equalsIgnoreCase(databaseName)){
				
				dropTable(tableNames.get(i));
				i--;
			}
		}
		if (currentDataBase.equalsIgnoreCase(databaseName)) {
			currentDataBase = "";
		}
	
		databaseNames.remove(databaseName);
		database = new File(dir + "Databases" + fs + databaseName);
		database.delete();
		saveDatabaseNames();
	}
	
	public void dropTable(String tableName){
		
		
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int j=0;j<tableNames.size();j++){
			if(tableNames.get(j).equalsIgnoreCase(tableName) && tablesInDatabase.get(j).equalsIgnoreCase(currentDataBase)){
			
				File tableFile = new File(dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml");
				tableFile.delete();
				
				File columnes = new File(dir + "Databases" + fs + currentDataBase + fs + tableName + ".txt");
				columnes.delete();
				
				for(int i=0;i<tableNames.size();i++){
					if(tableNames.get(i).equalsIgnoreCase(tableName)){
						tablesInDatabase.remove(i);
						break;
					}
				}
				
				tableNames.remove(tableName);
				
				saveTableNames();
				
			}
		}
		
	}
	
	public ArrayList[] selectFromTable(String tableName){
		
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int i=0;i<tableNames.size();i++){
			if(tableNames.get(i).equalsIgnoreCase(tableName)){
				try {
					String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
					Scanner s = new Scanner(new File(dir + "Databases" + fs + currentDataBase + fs + tableName + ".txt"));
					String op;
					op = s.nextLine();
					colNames = op.split(",");
					s.close();
					
					readFromXmlFiles(colNames,path);
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data;
			}
		}
		return null;
		
	}
	
	public ArrayList[] selectFromTable1(String tableName,ArrayList<String> columneNames){
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int j=0;j<tableNames.size();j++){
			if(tableNames.get(j).equalsIgnoreCase(tableName)){
				String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
				colNames = new String[columneNames.size()];
				for(int i=0;i<columneNames.size();i++){
					colNames[i] = columneNames.get(i);
				}
				readFromXmlFiles(colNames,path);
				
				
				return data;
			}
		}
		return null;
	}
	
	public int insertData(String tableName,ArrayList values){
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int j=0;j<tableNames.size();j++){
			if(tableNames.get(j).equalsIgnoreCase(tableName)){
				ArrayList[] data1;
				data1 = selectFromTable(tableName);
				
				for(int i=0;i<data1.length;i++){
					data1[i].add(values.get(i));
				}
				data = data1;
				String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
				writeInXmlFiles(path);
				
				return 1;
			}
		}
		return 0;
		
	}
	
	public int insertData1(String tableName,ArrayList values,ArrayList<String> columneNames){
		boolean check;
		File f = new File( dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int k=0;k<tableNames.size();k++){
			if(tableNames.get(k).equalsIgnoreCase(tableName)){
				ArrayList[] data1;
				data1 = selectFromTable(tableName);
				for(int i=0;i<data1.length;i++){
					check = false;
					for(int j=0;j<columneNames.size();j++){
						String s = (String) data1[i].get(0);
						if(s.equalsIgnoreCase(columneNames.get(j))){
							data1[i].add(values.get(j));
							check = true;
							break;
						}
					}
					if(check == false){
						data1[i].add("null");
					}
				}
				data = data1;
				String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
				writeInXmlFiles(path);
				
				return 1;
			}
		}
		return 0;
		
	}
	
	public int updateData(String tableName,ArrayList values,ArrayList<String> columneNames){
		int counter=0;
		
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int s=0;s<tableNames.size();s++){
			if(tableNames.get(s).equalsIgnoreCase(tableName) && tablesInDatabase.get(s).equalsIgnoreCase(currentDataBase)){
			
				ArrayList[] data1;
				data1 = selectFromTable(tableName);
				counter = data1[0].size()-1;
				
				for(int i=0;i<columneNames.size();i++){
					for(int j=0;j<data1.length;j++){
						String s1 = (String) data1[j].get(0);
						if(s1.equalsIgnoreCase(columneNames.get(i))){
							for(int k=1;k<data1[0].size();k++){
								data1[j].set(k, values.get(i));
								
							}
						}
					}
				}
				
				data = data1;
				String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
				writeInXmlFiles(path);
				
			}
		}
		return counter;
	}
	
	public int updateData1(String tableName,ArrayList values,ArrayList<String> columneNames,
							Object conditionValues,String conditionColumneNames){
		
		int counter=0;
		boolean b = false;
		
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int s=0;s<tableNames.size();s++){
			if(tableNames.get(s).equalsIgnoreCase(tableName) && tablesInDatabase.get(s).equalsIgnoreCase(currentDataBase)){
				
				ArrayList[] data1;
				data1 = selectFromTable(tableName);
				
				int mark = 0;
				
				for(int i=0;i<data1.length;i++){
					String s1 = (String) data1[i].get(0);
					if(s1.equalsIgnoreCase(conditionColumneNames)){
						mark = i;
					}
				}
				for(int i=0;i<columneNames.size();i++){
					for(int j=0;j<data1.length;j++){
						String s2 = (String) data1[j].get(0);
						if(s2.equalsIgnoreCase(columneNames.get(i))){
							for(int k=1;k<data1[0].size();k++){
								String s3 = (String) data1[mark].get(k);
								if(s3.equalsIgnoreCase((String) conditionValues)){
									data1[j].set(k, values.get(i));
									if(!b){
										counter++;
									}
								}
							}
							b = true;
						}
					}
				}
				
				data = data1;
				String path = dir + "Databases" + fs + currentDataBase + "\\" + tableName + ".xml";
				writeInXmlFiles(path);
			}
		}
		return counter;
		
		
		
	}
	
	public int deleteData(String tableName){
		int counter = 0;
		
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int s=0;s<tableNames.size();s++){
			if(tableNames.get(s).equalsIgnoreCase(tableName)){
				ArrayList[] data1;
				data1 = selectFromTable(tableName);
				counter = data1[0].size()-1;
				
				for(int i=0;i<data1.length;i++){
					data1[i].clear();
				}
				
				data = data1;
				String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
				writeInXmlFiles(path);
			}
		}
		return counter;
		
	}
	
	public int deleteData1(String tableName,Object conditionValues,String conditionColumneNames){
		
		int counter=0;
		File f = new File(dir + "tablename.xml");
		if(f.exists()){
			loadTableNames();
		}
		for(int s=0;s<tableNames.size();s++){
			if(tableNames.get(s).equalsIgnoreCase(tableName)){
				
				ArrayList[] data1;
				data1 = selectFromTable(tableName);
				
				
				
				for(int i=0;i<data1.length;i++){
					if(data1[i].get(0).equals(conditionColumneNames)){
						for(int j=1;j<data1[i].size();j++){
							if(data1[i].get(j).equals(conditionValues)){
								for(int k=0;k<data1.length;k++){
									data1[k].remove(j);
								}
								counter++;
								j--;
							}
						}
					}
					
				}
				
				
				
				data = data1;
				String path = dir + "Databases" + fs + currentDataBase + fs + tableName + ".xml";
				writeInXmlFiles(path);
			}
		}
		
		return counter;
		
	}
	
	public void readFromXmlFiles(String[] columneNames,String path){
		
		boolean[] colcheck = new boolean[columneNames.length];
		data = new ArrayList[columneNames.length];
		for(int j=0;j<columneNames.length;j++){
			data[j] = new ArrayList();
			data[j].add(columneNames[j]);
		}
		
		 try {
	    	  
	         XMLInputFactory factory = XMLInputFactory.newInstance();
	         XMLEventReader eventReader =
	         factory.createXMLEventReader(new FileReader(path));
	         
	         while(eventReader.hasNext()) {
	            XMLEvent event = eventReader.nextEvent();
	               
	            switch(event.getEventType()) {
	               
	               case XMLStreamConstants.START_ELEMENT:
	                  StartElement startElement = event.asStartElement();
	                  String qName = startElement.getName().getLocalPart();

	               if (qName.equalsIgnoreCase("row")) {
	                 
	               } else{
	            	   for(int i=0;i<columneNames.length;i++){
	            		   if (qName.equalsIgnoreCase(columneNames[i])){ 
	            			   colcheck[i] = true;
	            			   break;
	            		   }
	            	   }
	               }
	               break;

	               case XMLStreamConstants.CHARACTERS:
	                  Characters characters = event.asCharacters();
	                
	                  for(int i=0;i<colcheck.length;i++){
	            		   if (colcheck[i]){ 
	            			   data[i].add(characters.getData());
	            			   colcheck[i] = false;
	            			  // break;
	            		   }
	            	   }
	               
	              
	               break;

	               case XMLStreamConstants.END_ELEMENT:
	                  EndElement endElement = event.asEndElement();
	              
	               break;
	            } 
	         }
		 
	      } catch (FileNotFoundException e) {
	         e.printStackTrace();
	      } catch (XMLStreamException e) {
	         e.printStackTrace();
	      }
	}
	
	public void writeInXmlFiles(String path){
		try {
	
		         FileOutputStream fos = new FileOutputStream(new File(path));

		         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
		         XMLStreamWriter xMLStreamWriter =
		            xMLOutputFactory.createXMLStreamWriter(fos);
		   
		         xMLStreamWriter.writeStartDocument();
		         xMLStreamWriter.writeStartElement("tablename");
		   
		         for(int i=1;i<data[0].size();i++){
		        	 
		         xMLStreamWriter.writeStartElement("row");	
		      
			         for(int j=0;j<data.length;j++){
			      
				         xMLStreamWriter.writeStartElement((String) data[j].get(0));
				         xMLStreamWriter.writeCharacters((String) data[j].get(i));
				         xMLStreamWriter.writeEndElement();
				         
			         }
		         xMLStreamWriter.writeEndElement();
		         }
		         xMLStreamWriter.writeEndDocument();

		         xMLStreamWriter.flush();
		         xMLStreamWriter.close();

		      
		      } catch (XMLStreamException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		      }
	}
	
}
