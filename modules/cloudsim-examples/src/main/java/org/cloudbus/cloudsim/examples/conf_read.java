package org.cloudbus.cloudsim.examples;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Aisle;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Rack;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.Zone;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class conf_read {
	
	private static List<Pe> createPEList(String name, int num_pes, int mips) {
		// 2. A Machine contains one or more PEs or CPUs/Cores.
    	// In this example, it will have only one core.
    	List<Pe> peList = new ArrayList<Pe>();

        // 3. Create PEs and add these into a list.
    	for(int i=0; i<num_pes; i++)
    	{
        	peList.add(new Pe(i, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
    	}
    	
    	return peList;
	}
	
	private static List<Host> createHostList(String name, int num_hosts, List<List<Pe>> pe2D, int ram, int bw, int storage) {
        List<Host> hostList = new ArrayList<Host>();
        for(int i = 0; i<num_hosts; i++)
        {
        	//4. Create Host with its id and list of PEs and add them to the list of machines
	        int hostId=0;
//	        int ram = 2048; //host memory (MB)
//	        long storage = 1000000; //host storage
//	        int bw = 10000;

	        hostList.add(
	    			new Host(
	    				i,
	    				new RamProvisionerSimple(ram),
	    				new BwProvisionerSimple(bw),
	    				storage,
	    				pe2D.get(i),
	    				new VmSchedulerTimeShared(pe2D.get(i))
	    			)
	    		);// This is our machine
        }
        return hostList;
	}
	
	private static List<Rack> createRackList(String name, int num_racks, List<List<Host>> host2D) {
        List<Rack> rackList = new ArrayList<Rack>();
        for(int i = 0; i<num_racks; i++)
        {
            rackList.add(new Rack(i, host2D.get(i)));
        }
        return rackList;
	}
	
	private static List<Aisle> createAisleList(String name, int num_aisles, List<List<Rack>> racks2D) {
        List<Aisle> aisleList = new ArrayList<Aisle>();
        for(int i = 0; i<num_aisles; i++)
        {
            aisleList.add(new Aisle(i, racks2D.get(i)));
        }
        return aisleList;
	}
	
	private static List<Zone> createZoneList(String name, int num_zones, List<List<Aisle>> aisle2D) {
        List<Zone> zoneList = new ArrayList<Zone>();
        for(int i = 0; i<num_zones; i++)
        {
            zoneList.add(new Zone(i, aisle2D.get(i)));
        }
        return zoneList;
	}
	
	private static Datacenter createDatacenter(String name, double cost, double costPerMem, double costPerStorage, double costPerBw, List<Zone> zoneList)
	{
		// 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
//        double cost = 3.0;              // the cost of using processing in this resource
//        double costPerMem = 0.05;		// the cost of using memory in this resource
//        double costPerStorage = 0.001;	// the cost of using storage in this resource
//        double costPerBw = 0.0;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, zoneList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(characteristics.getHostList()), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
	}	
}