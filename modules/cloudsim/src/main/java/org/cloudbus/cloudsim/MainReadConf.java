package org.cloudbus.cloudsim;

import org.cloudbus.cloudsim.ReadConf;

/**
 * Extended class created to read the first main configuration file
 * @author user
 *
 */
 
public class MainReadConf extends ReadConf {
 
    private int ndatacenter;
   
    public MainReadConf(String filename) {
        super(filename);
        // TODO Auto-generated constructor stub
        ndatacenter = Integer.valueOf(prop.getProperty("NO_OF_DATACENTERS"));
    }
 
    /**
     * function to return the number of datacenters given in conf file
     * @return
     */
   
    public int getDatacenterCount() {
        return ndatacenter;
    }
}
