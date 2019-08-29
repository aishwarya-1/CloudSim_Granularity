package org.cloudbus.cloudsim;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.cloudbus.cloudsim.HostCharacteristics;
 
public class ReadConf {
   
    // The required variables 
	private static int ndatacenter;
	
    private int nzone;
   
    private List<Integer> aisleList;
   
    private List<Integer> rackList;
   
    private List<Integer> hostList;
   
    private Integer ntypes;
   
    private List<HostCharacteristics> types;
   
    protected Properties prop = new Properties();
   
    /**
     * Constructor to check for file exceptions and call initialize function
     * @param filename
     */
   
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
 
        Initialise();
    }
   
    /**
     * Function to initialize each characteristics given in the configuration file
     */
   
    public void Initialise() {
    	try {
    		ndatacenter = Integer.valueOf(prop.getProperty("NO_OF_DATACENTERS")); 
    	}
    	catch(Exception e){
    		
    	}
        nzone = Integer.valueOf(prop.getProperty("NO_OF_ZONES"));
        aisleList = getList(prop.getProperty("NO_OF_AISLES"));
        rackList = getList(prop.getProperty("NO_OF_RACKS"));
        hostList = getList(prop.getProperty("NO_OF_HOSTS"));
        ntypes = Integer.valueOf(prop.getProperty("NO_OF_TYPES"));
        setServerTypes();
       
    }
   
    /**
     * Function to initialize server type configurations and add it to the list
     */
    public void setServerTypes() {
       
        types = new ArrayList<HostCharacteristics>();
       
        // add each server types to variable 'types'
       
        for(int i=1; i<=ntypes; i++) {
           
            int cores = Integer.valueOf(prop.getProperty("CORE_"+i));
            int mips = Integer.valueOf(prop.getProperty("MIPS_"+i));
            int ram = Integer.valueOf(prop.getProperty("RAM_"+i));
            int storage = Integer.valueOf(prop.getProperty("STORAGE_"+i));
            int bw = Integer.valueOf(prop.getProperty("BW_"+i));
            List<Integer> type = getList(prop.getProperty("TYPE_"+i));
           
            types.add(new HostCharacteristics(cores, mips, ram, storage, bw, type));
           
        }
       
    }
   
    /**
     * Function to get the integer list inputs from the conf file as the contents passed would be string
     */
   
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
    
    /**
     * function to return the number of datacenters given in conf file
     * @return
     */
    
    public int getDatacenterCount() {
        return ndatacenter;
    }
   
    /**
     * function to return number of zone
     * @return
     */
   
    public int getZoneCount(){
        return nzone;
    }
   
    /**
     * function to return list of number of aisles per zone
     * @return
     */
   
    public List<Integer> getAisleList(){
        return aisleList;
    }
   
    /**
     * function to return list of number of racks per aisle
     * @return
     */
   
    public List<Integer> getRackList(){
        return rackList;
    }
   
    /**
     * function to return list of number of hosts per rack
     * @return
     */
   
    public List<Integer> getHostList(){
        return hostList;
    }
   
    /**
     * function to return the total number of server types
     * @return
     */
   
    public int getTypeCount() {
        return ntypes;
    }
   
    /**
     * function to  return list of each server configuration
     * @return
     */
   
    public List<HostCharacteristics> getTypeList(){
        return types;
    }
   
   
}
