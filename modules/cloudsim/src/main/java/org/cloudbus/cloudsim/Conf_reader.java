package org.cloudbus.cloudsim;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Reader;
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

public class Conf_reader {
	
	private static List<Pe> createPEList(int num_pes, int mips) {
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
	
	private static List<Host> createHostList(int id, int num_hosts, List<List<Pe>> pe2D, List<Integer> rams, List<Integer> bws, List<Long> storages) {
        List<Host> hostList = new ArrayList<Host>();
        for(int i = id; i<num_hosts; i++)
        {
        	//4. Create Host with its id and list of PEs and add them to the list of machines

	        hostList.add(
	    			new Host(
	    				i,
	    				new RamProvisionerSimple(rams.get(i-id)),
	    				new BwProvisionerSimple(bws.get(i-id)),
	    				storages.get(i-id),
	    				pe2D.get(i-id),
	    				new VmSchedulerTimeShared(pe2D.get(i-id))
	    			)
	    		);// This is our machine
        }
        return hostList;
	}
	
	private static List<Rack> createRackList(int num_racks, List<List<Host>> host2D) {
        List<Rack> rackList = new ArrayList<Rack>();
        for(int i = 0; i<num_racks; i++)
        {
            rackList.add(new Rack(i, host2D.get(i)));
        }
        return rackList;
	}
	
	private static List<Aisle> createAisleList(int num_aisles, List<List<Rack>> racks2D) {
        List<Aisle> aisleList = new ArrayList<Aisle>();
        for(int i = 0; i<num_aisles; i++)
        {
            aisleList.add(new Aisle(i, racks2D.get(i)));
        }
        return aisleList;
	}
	
	private static List<Zone> createZoneList(int num_zones, List<List<Aisle>> aisle2D) {
        List<Zone> zoneList = new ArrayList<Zone>();
        for(int i = 0; i<num_zones; i++)
        {
            zoneList.add(new Zone(i, aisle2D.get(i)));
        }
        return zoneList;
	}
	private void readPe()
	{
		
	}
	
	private static Datacenter createDatacenter(double cost, double costPerMem, double costPerStorage, double costPerBw, List<Zone> zoneList)
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
            datacenter = new Datacenter("Datacenter", characteristics, new VmAllocationPolicySimple(characteristics.getHostList()), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
	}	
	
	public Datacenter create_infrastructure(String str)
	{
		Reader read = new Reader(str);
		
		List<Datacenter> dcList = new ArrayList<Datacenter>();
		

		int m = 0;
		int rackid = 0;
		int aisleid = 0;
		int zoneid = 0;
		
		

		for(int i=0; i<read.getDatacenterCount(); i++)
		{
			List<Zone> zoneList = new ArrayList<Zone>();
			List<List<Aisle>> aisle2D = new ArrayList<List<Aisle>>();
			
			for(int j=0; j<(read.getZoneList()).get(i); j++)
			{
				List<Aisle> aislelist = new ArrayList<Aisle>();
				List<List<Rack>> rack2D = new ArrayList<List<Rack>>();

				for(int k = 0; k<(read.getAisleList()).get(j); k++)
				{
					List<Rack> racklist = new ArrayList<Rack>();
					List<List<Host>> host2D = new ArrayList<List<Host>>();
					
					for(int l=0; l<(read.getRackList()).get(k); l++)
					{
						List<Host> hostlist = new ArrayList<Host>();
						List<List<Pe>> pe2D = new ArrayList<List<Pe>>();
						List<Integer> rams = new ArrayList<Integer>();
						List<Integer> bws = new ArrayList<Integer>();
						List<Long> storages = new ArrayList<Long>();
						
						int n = m;					
						
						while(m<(read.getHostList()).size()+n)
						{
							for(int o=1; o<=read.getTypeCount(); o++)
							{
								List<Dictionary<String, Object>> l1 = (List<Dictionary<String, Object>>)read.getTypeList();
								
								//System.out.print(l1.get(o));
								
								Dictionary<String, Object> dict1 = l1.get(o-1);
								
								//System.out.print(dict1.get("TYPE_1"));
								
								String str1 = "TYPE_" + o;
								//System.out.print(str1);
								
								
								List<Integer> list1 = (List<Integer>) dict1.get("TYPE_"+ o);
								
								//System.out.print(list1.get(0));
								
								if(list1.contains(m) == true)
								{
									int cores = (int) read.getTypeList().get(o-1).get("CORE_"+o);
									int mips = (int) read.getTypeList().get(o-1).get("MIPS_"+o);
									int ram = (int) read.getTypeList().get(o-1).get("RAM_"+o);
									int bw = (int) read.getTypeList().get(o-1).get("BW_"+o);
									long storage = (long) read.getTypeList().get(o-1).get("STORAGE_"+o);
									List<Pe> peList = new ArrayList<Pe>();
									peList = createPEList(cores, mips);
									pe2D.add(peList);
									rams.add(ram);
									bws.add(bw);
									storages.add(storage);
								}
							}
							m++;							
						}
						
						hostlist = createHostList(n, read.getHostList().get(l), pe2D, rams, bws, storages);
						host2D.add(hostlist);
					}
					
					racklist = createRackList(read.getRackList().get(k), host2D);
					rack2D.add(racklist);
				}
				aislelist = createAisleList(read.getAisleList().get(j), rack2D);
				aisle2D.add(aislelist);
				
			}
			zoneList = createZoneList(read.getZoneList().get(i), aisle2D);
			//zoneList = createZoneList(read.getZoneList(), aisle2D);
			dcList.add(createDatacenter(3.0, 0.05, 0.001, 0.0, zoneList));
			//Datacenter dc = createDatacenter(3.0, 0.05, 0.001, 0.0, zoneList);
		}
	
	return dcList.get(0);

	}
}