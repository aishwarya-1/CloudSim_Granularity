package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;


import java.util.LinkedList;

import org.cloudbus.cloudsim.Reader;
import org.cloudbus.cloudsim.Aisle;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Rack;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.Zone;
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
	
	private static List<Host> createHostList(int id, int num_hosts, List<List<Pe>> pe2D, List<Integer> rams, List<Integer> bws, List<Long> storages) 
	{
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
	    		);
        }
        return hostList;
	}
	
	private static List<Rack> createRackList(int rackid, int num_racks, List<List<Host>> host2D) {
        List<Rack> rackList = new ArrayList<Rack>();
        for(int i = 0; i<num_racks; i++)
        {
            rackList.add(new Rack(rackid+i, host2D.get(i)));
        }
        return rackList;
	}
	
	private static List<Aisle> createAisleList(int aisleid, int num_aisles, List<List<Rack>> racks2D) {
        List<Aisle> aisleList = new ArrayList<Aisle>();
        for(int i = 0; i<num_aisles; i++)
        {
            aisleList.add(new Aisle(aisleid+i, racks2D.get(i)));
        }
        return aisleList;
	}
	
	private static List<Zone> createZoneList(int zoneid, int num_zones, List<List<Aisle>> aisle2D) {
        List<Zone> zoneList = new ArrayList<Zone>();
        for(int i = 0; i<num_zones; i++)
        {
            zoneList.add(new Zone(zoneid+i, aisle2D.get(i)));
        }
        return zoneList;
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
		
		int hostid = 0;
		int rackid = 0;
		int aisleid = 0;
		int zoneid = 0;
		
		for(int i=0; i<read.getDatacenterCount(); i++)
		{
			List<Zone> zoneList = new ArrayList<Zone>();
			List<List<Aisle>> aisle2D = new ArrayList<List<Aisle>>();
			int nz = zoneid;
			
			for(int j=0; j<(read.getZoneList()).get(i); j++)
			{
				List<Aisle> aislelist = new ArrayList<Aisle>();
				List<List<Rack>> rack2D = new ArrayList<List<Rack>>();
				int na = aisleid;
				
				System.out.println("j"+ j);

				for(int k = 0; k<(read.getAisleList()).get(j); k++)
				{
					List<Rack> racklist = new ArrayList<Rack>();
					List<List<Host>> host2D = new ArrayList<List<Host>>();
					int nr = rackid;
					
					System.out.println("k"+ k);
					
					for(int l=0; l<(read.getRackList()).get(k); l++)
					{
						List<Host> hostlist = new ArrayList<Host>();
						List<List<Pe>> pe2D = new ArrayList<List<Pe>>();
						List<Integer> rams = new ArrayList<Integer>();
						List<Integer> bws = new ArrayList<Integer>();
						List<Long> storages = new ArrayList<Long>();
						
						int n = hostid;					
						
						while(hostid<(read.getHostList()).size()+n)
						{
							for(int o=1; o<=read.getTypeCount(); o++)
							{
								List<Dictionary<String, Object>> l1 = (List<Dictionary<String, Object>>)read.getTypeList();
																
								Dictionary<String, Object> dict1 = l1.get(o-1);								
								
								List<Integer> list1 = (List<Integer>) dict1.get("TYPE_"+ o);
								
								if(list1.contains(hostid) == true)
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
							hostid++;
						}
						
							//System.out.println(hostid);
							
							System.out.println(l);
							
							//System.out.println(read.getHostList().get(l));
							
						
						hostlist = createHostList(n, read.getHostList().get(l), pe2D, rams, bws, storages);
						host2D.add(hostlist);
						rackid++;
					}
					
					racklist = createRackList(nr, read.getRackList().get(k+j*i), host2D);
					rack2D.add(racklist);
					aisleid++;
				}
				aislelist = createAisleList(na, read.getAisleList().get(j+i), rack2D);
				aisle2D.add(aislelist);
				zoneid++;
				
			}
			zoneList = createZoneList(nz, read.getZoneList().get(i), aisle2D);
			dcList.add(createDatacenter(3.0, 0.05, 0.001, 0.0, zoneList));
		}
	
	return dcList.get(0);

	}
}