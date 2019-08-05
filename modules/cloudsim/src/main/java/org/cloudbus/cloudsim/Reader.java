package org.cloudbus.cloudsim;
 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
 
public class Reader {
   
    // The required variables
    private static int ndatacenter;
   
    private static List<Integer> zoneList;
   
    private static List<Integer> aisleList;
   
    private static List<Integer> rackList;
   
    private static List<Integer> hostList;
   
    private static Integer ntypes;
   
    public static List<Dictionary<String, Object>> types;
   
    Properties prop = new Properties();
   
    public Reader(String filename) {
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
        setServerTypes();
       
    }
   
    public void setServerTypes() {
        types = new ArrayList<Dictionary<String,Object>>();
        for(int i=1; i<=ntypes; i++)
        {
            Dictionary<String, Object> temp = new Hashtable<String, Object>();
            String cores = "CORE_"+i;
            String mips = "MIPS_"+i;
            String ram = "RAM_"+i;
            String storage = "STORAGE_"+i;
            String bw = "BW_"+i;
            String type = "TYPE_"+i;
            
            //System.out.print(Integer.valueOf(prop.getProperty()));
            
            temp.put(cores, Integer.valueOf(prop.getProperty(cores)));
            temp.put(mips, Integer.valueOf(prop.getProperty(mips)));
            temp.put(ram, Integer.valueOf(prop.getProperty(ram)));
            temp.put(storage, Long.valueOf(prop.getProperty(storage)));
            temp.put(bw, Integer.valueOf(prop.getProperty(bw)));
            temp.put(type, getList(prop.getProperty(type)));
            
            //System.out.print(temp.get("TYPE_"+i));
 
            types.add(temp);
        }
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
   
    public List<Dictionary<String, Object>> getTypeList(){
        return types;
    }
   
}