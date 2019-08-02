package org.cloudbus.cloudsim.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadConf {

	// The required variables
	private static int ndatacenter;
	
	private static List<Integer> zoneList;
	
	private static List<Integer> aisleList;
	
	private static List<Integer> rackList;
	
	private static List<Integer> hostList;
	
	private static Integer ntypes;
	
	private static List<List<Integer>> types;
	
	Properties prop = new Properties();
	
	public ReadConf(String filename) {
		try {
			InputStream is = new FileInputStream(filename);
			prop.load(is);
		}
		catch(FileNotFoundException e){
			System.out.println("File not found");
		}
		catch(IOException e) {
			System.out.println("Prop failed");
		}
		//System.out.println(prop.getProperty("NO_OF_AISLES"));
		Initialise();
	}
	
	public void Initialise() {
		ndatacenter = Integer.valueOf(prop.getProperty("NO_OF_DATACENTERS"));
		zoneList = getList(prop.getProperty("NO_OF_ZONES"));
		aisleList = getList(prop.getProperty("NO_OF_AISLES"));
		rackList = getList(prop.getProperty("NO_OF_RACKS"));
		hostList = getList(prop.getProperty("NO_OF_HOSTS"));
		ntypes = Integer.valueOf(prop.getProperty("NO_OF_TYPES"));
		types = new ArrayList<List<Integer>>();
		for(int i=1; i<=ntypes; i++)
		{
			List<Integer> temp = new ArrayList<Integer>();
			temp = getList(prop.getProperty("TYPE_"+i));
			types.add(temp);
		}
		/*for(int i=0; i<types.size();i++)
		{
		List<Integer> temp = types.get(i);
		for(int j=0;j<temp.size();j++)
		{
		System.out.print(temp.get(j));
		}
		System.out.println();
		}*/
	
	}
	
	public List<Integer> getList(String values){
		List<Integer> list = new ArrayList<Integer>();
		
		values = values.trim();
		String[] val = values.split(",");
		for(int i=0;i<val.length;i++)
		{
			list.add(Integer.valueOf(val[i].trim()));
		}
		
		return list;
	}
	
	public int getDatacenterCount() {
		return ndatacenter;
	}
	
	public List<Integer> getZoneList(){
		return zoneList;
	}
	
	public List<Integer> getAisleList(){
		return aisleList;
	}
	
	public List<Integer> getRackList(){
		return rackList;
	}
	
	public List<Integer> getHostList(){
		return hostList;
	}
	
	public int getTypeCount() {
		return ntypes;
	}
	
	public List<List<Integer>> getTypeList(){
		return types;
	}

}