package org.cloudbus.cloudsim;

import java.util.List;
 
public class HostCharacteristics {
   
    private int cores;
   
    private int mips;
   
    private int ram;
   
    private int storage;
   
    private int bw;
   
    private List<Integer> host_ids;
   
    public HostCharacteristics(int icores,int imips,int iram,int istorage,int ibw, List<Integer> ihost_ids) {
       
        // Initializing all the characteristics in the constructor function
        cores = icores;
        mips = imips;
        ram = iram;
        storage = istorage;
        bw = ibw;  
        host_ids = ihost_ids;
       
       
    }
   
    public int getCores() {
        return cores;
    }
   
    public int getMips() {
        return mips;
    }
   
    public int getRam() {
        return ram;
    }
   
    public int getStorage() {
        return storage;
    }
   
    public int getBw() {
        return bw;
    }
   
    public List<Integer> getHostIDs(){
        return host_ids;
    }
   
}
