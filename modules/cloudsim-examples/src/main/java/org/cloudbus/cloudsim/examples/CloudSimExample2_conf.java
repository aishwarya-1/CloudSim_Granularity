/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

import org.cloudbus.cloudsim.Aisle;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Consts;
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
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemoryRam;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemory;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemoryBw;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.Zone;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


/**
 * A simple example showing how to create
 * a datacenter with one host and run two
 * cloudlets on it. The cloudlets run in
 * VMs with the same MIPS requirements.
 * The cloudlets will take the same time to
 * complete the execution.
 */
public class CloudSimExample2_conf {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	
	private static int nDatacenter;

	private static int nZones;

	private static int naisles;

	private static int nracks;

	private static int nhosts;

	private static List<Integer> A;

	private static List<Integer> B;

	private static List<Integer> C;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String conf;
		String workload;
		
		if (args.length >= 1 && args[0] != null && !args[0].isEmpty()) {
		conf = args[0];
		if (args.length >= 2 && args[1] != null && !args[1].isEmpty()) {
		workload = args[1];
		}
		else
		{
		workload = "";
		}
		}
		else {
		conf = "";
		workload = "";
		}
//		Runner runner = new Runner();
//		runner.start(conf,workload);
		start(conf, workload);
	}


	private static void getConfiguration(String conf) throws Exception
	{
		BufferedReader fr = new BufferedReader(new FileReader(conf));
		String line = fr.readLine();
		int i = 1;
		while(line != null)
		{
			String[] st = line.split(":");
			switch (i) {
				case 1:
					nDatacenter = Integer.valueOf(st[1]);
					break;
				case 2:
					nZones = Integer.valueOf(st[1]);
					break;
				case 3:
					naisles = Integer.valueOf(st[1]);
					break;
				case 4:
					nracks = Integer.valueOf(st[1]);
					break;
				case 5:
					nhosts = Integer.valueOf(st[1]);
					break;
				default:
					break;
			}
			if(i==6) {
				A = new ArrayList<Integer>();
				String[] sub = st[1].split(",");
				for(int j=0;j<sub.length;j++)
				{
				A.add(Integer.valueOf(sub[j].trim()));
				}
			}
			if(i==7) {
				B = new ArrayList<Integer>();
				String[] sub = st[1].split(",");
				for(int j=0;j<sub.length;j++)
				{
				B.add(Integer.valueOf(sub[j].trim()));
				}
			}
			if(i==8) {
				C = new ArrayList<Integer>();
				String[] sub = st[1].split(",");
				for(int j=0;j<sub.length;j++)
				{
				C.add(Integer.valueOf(sub[j].trim()));
				}
			}
			i++;
			line = fr.readLine();
		}
		fr.close();
		System.out.println(nDatacenter+" "+nZones+" "+naisles+" "+nracks+" "+nhosts);
		for(int k=0;k<A.size();k++)
		{
			System.out.println(A.get(k)+" "+B.get(k)+" "+C.get(k));
		}

	}

	/**
	 * Creates main() to run this example
	 */
	public static void start(String conf, String workload) {

		Log.printLine("Starting CloudSimExample2...");

		File input = new File(workload);
		Log.printLine(input);
		File[] files = input.listFiles();
		Log.printLine(files);
	        try {
	        	
//	        		String inputFolder = CloudSimExample2_conf.class.getClassLoader().getResource("workload/planetlab/20110303").getPath();
//	        		Log.printLine(inputFolder);
	        		getConfiguration(conf);
	        	// First step: Initialize the CloudSim package. It should be called
	            	// before creating any entities.
	            	int num_user = 1;   // number of cloud users
	            	Calendar calendar = Calendar.getInstance();
	            	boolean trace_flag = false;  // mean trace events

	            	// Initialize the CloudSim library
	            	CloudSim.init(num_user, calendar, trace_flag);

	            	// Second step: Create Datacenters
	            	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
	            	for (int i = 0;i < nDatacenter; i++) {
	            		@SuppressWarnings("unused")
	            		Datacenter datacenter = createDatacenter("Datacenter_"+i);
	            	}
//	            	@SuppressWarnings("unused")
//					Datacenter datacenter0 = createDatacenter("Datacenter_0");

	            	//Third step: Create Broker
	            	DatacenterBroker broker = createBroker();
	            	int brokerId = broker.getId();

	            	//Fourth step: Create one virtual machine
	            	vmlist = new ArrayList<Vm>();

	            	//VM description
	            	int vmid = 0;
	            	int mips = 250;
	            	long size = 10000; //image size (MB)
	            	int ram = 512; //vm memory (MB)
	            	long bw = 1000;
	            	int pesNumber = 1; //number of cpus
	            	String vmm = "Xen"; //VMM name

	            	//create two VMs
	            	Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

	            	vmid++;
	            	Vm vm2 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

	            	vmid++;
	            	Vm vm3 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

	            	vmid++;
	            	Vm vm4 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

//	            	vmid++;
//	            	Vm vm5 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//	            	
//	            	vmid++;
//	            	Vm vm6 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

	            	//add the VMs to the vmList
	            	vmlist.add(vm1);
	            	vmlist.add(vm2);
	            	vmlist.add(vm3);
	            	vmlist.add(vm4);
//	            	vmlist.add(vm5);
//	            	vmlist.add(vm6);

	            	//submit vm list to the broker
	            	broker.submitVmList(vmlist);


	            	//Fifth step: Create two Cloudlets
	            	cloudletList = new ArrayList<Cloudlet>();

	            	//Cloudlet properties
	            	int id = 0;
	            	pesNumber=1;
	            	long length = 250000;
	            	long fileSize = 300;
	            	long outputSize = 300;
	            	//UtilizationModel utilizationModel = new UtilizationModelFull();
	            	for(id=0 ; id<9 ;id++) {
	            		try {
	            	Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, new UtilizationModelPlanetLabInMemory(
	            			files[id].getAbsolutePath(),
	            			Consts.SCHEDULING_INTERVAL), new UtilizationModelPlanetLabInMemoryRam(files[id].getAbsolutePath(), Consts.SCHEDULING_INTERVAL) , new UtilizationModelPlanetLabInMemoryBw(files[id].getAbsolutePath(), Consts.SCHEDULING_INTERVAL));
	            	cloudlet1.setUserId(brokerId);
	            	cloudletList.add(cloudlet1);
	            		}
	            		catch(Exception e) {
	            			e.printStackTrace();
	            			System.exit(0);
	            		}
	            	}
	            	/*id++;
	            	Cloudlet cloudlet2 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet2.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet3 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet3.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet4 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet4.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet5 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet5.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet6 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet6.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet7 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet7.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet8 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet8.setUserId(brokerId);
	            	
	            	id++;
	            	Cloudlet cloudlet9 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet9.setUserId(brokerId);

	            	//add the cloudlets to the list
	            	cloudletList.add(cloudlet1);
	            	cloudletList.add(cloudlet2);
	            	cloudletList.add(cloudlet3);
	            	cloudletList.add(cloudlet4);
	            	cloudletList.add(cloudlet5);
	            	cloudletList.add(cloudlet6);
	            	cloudletList.add(cloudlet7);
	            	cloudletList.add(cloudlet8);
	            	cloudletList.add(cloudlet9);*/

	            	//submit cloudlet list to the broker
	            	broker.submitCloudletList(cloudletList);


	            	//bind the cloudlets to the vms. This way, the broker
	            	// will submit the bound cloudlets only to the specific VM
	            	//broker.bindCloudletToVm(cloudlet1.getCloudletId(),vm1.getId());
	            	//broker.bindCloudletToVm(cloudlet2.getCloudletId(),vm2.getId());
	            	//broker.bindCloudletToVm(cloudlet3.getCloudletId(),vm3.getId());

	            	// Sixth step: Starts the simulation
	            	Log.printLine("Before start simulation");
	            	CloudSim.startSimulation();
	            	Log.printLine("After start simulation");
	            	


	            	// Final step: Print results when simulation is over
	            	List<Cloudlet> newList = broker.getCloudletReceivedList();
//	            	List<Cloudlet> cloudletlist = broker.getCloudletList();
//	            	for (Cloudlet cloudlet : cloudletlist) {
//	            		int cloudlet_vmid = cloudlet.getVmId();
//	            		Log.printLine(cloudlet_vmid);
//	            		Log.printLine("imple"+VmList.getById(broker.getVmsCreatedList(), cloudlet_vmid).getHost().getId());
//	            	}

	            	CloudSim.stopSimulation();
	            	

	            	printCloudletList(newList);

	            	Log.printLine("CloudSimExample2 finished!");
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            Log.printLine("The simulation has been terminated due to an unexpected error");
	        }
	    }

		private static Datacenter createDatacenter(String name){
			
			int hostId = 0;
			int rackId = 0;
			int aisleId = 0;
	       
	    	List<Pe> peList = new ArrayList<Pe>();
	    	int mips = 1000;
	    	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
	        int ram; //host memory (MB)
	        long storage; //host storage
	        int bw;
	        
	        List<Zone> zoneList = new ArrayList<Zone>();
	        for(int i = 0;i<nZones;i++){
	        	 List<Aisle> aisleList = new ArrayList<Aisle>();
	        	for(int j = 0;j<naisles;j++) {
	        		List<Rack> rackList = new ArrayList<Rack>();
	        		for(int k = 0;k<nracks;k++) {
	        			List<Host> hostList = new ArrayList<Host>();
	        			for(int l = 0; l<nhosts;l++) {
	        				if(A.contains(hostId)) {
		        				ram = 2048; //host memory (MB)
		        			    storage = 1000000; //host storage
		        			    bw = 10000;
	        				}
	        				else if(B.contains(hostId)){
	        					ram = 2048*2; //host memory (MB)
		        			    storage = 2000000; //host storage
		        			    bw = 10000;
	        				}
	        				else {
	        					ram = 2048*4; //host memory (MB)
		        			    storage = 3000000; //host storage
		        			    bw = 10000;
	        				}        				
	        				hostList.add(
	        		    			new Host(
	        		    				hostId,
	        		    				new RamProvisionerSimple(ram),
	        		    				new BwProvisionerSimple(bw),
	        		    				storage,
	        		    				peList,
	        		    				new VmSchedulerTimeShared(peList)
	        		    			)
	        		    		);
	        				hostId++;
	        			}
	        			rackList.add(new Rack(rackId, hostList));
	        			rackId++;
	        			
	        		}
	        		aisleList.add(new Aisle(aisleId, rackList));
	        		aisleId++;
		        }
		        zoneList.add(new Zone(i, aisleList));
	        }
	        
//	        List<Zone> zoneList = new ArrayList<Zone>();
//	        zoneList.add(new Zone(0, aisleList));


	        // 5. Create a DatacenterCharacteristics object that stores the
	        //    properties of a data center: architecture, OS, list of
	        //    Machines, allocation policy: time- or space-shared, time zone
	        //    and its price (G$/Pe time unit).
	        String arch = "x86";      // system architecture
	        String os = "Linux";          // operating system
	        String vmm = "Xen";
	        double time_zone = 10.0;         // time zone this resource located
	        double cost = 3.0;              // the cost of using processing in this resource
	        double costPerMem = 0.05;		// the cost of using memory in this resource
	        double costPerStorage = 0.001;	// the cost of using storage in this resource
	        double costPerBw = 0.0;			// the cost of using bw in this resource
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

	    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	    //to the specific rules of the simulated scenario
	    private static DatacenterBroker createBroker(){

	    	DatacenterBroker broker = null;
	        try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	    	return broker;
	    }

	    /**
	     * Prints the Cloudlet objects
	     * @param list  list of Cloudlets
	     */
	    @SuppressWarnings("deprecation")
		private static void printCloudletList(List<Cloudlet> list) {
	        int size = list.size();
	        Cloudlet cloudlet;

	        String indent = "    ";
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
	                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = list.get(i);
	            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

	            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
	                Log.print("SUCCESS");

	            	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
	                     indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                             indent + indent + dft.format(cloudlet.getFinishTime()));
	            }
	        }
	        
	        for(int i=0;i<size;i++) {
	        	cloudlet = list.get(i);
	        	for(double Time = cloudlet.getExecStartTime(); Time<cloudlet.getFinishTime();Time=Time+10) {
	        		Log.printLine();
	        		Log.printLine("CPU Utilization at time "+Time+ ":"+ cloudlet.getUtilizationOfCpu(Time));
	        		Log.printLine("Ram Utilization at time "+Time+ ":"+ cloudlet.getUtilizationOfRam(Time));
	        		Log.printLine("Bw Utilization at time "+Time+ ":"+ cloudlet.getUtilizationOfBw(Time));
	        		Log.printLine("----------------------------------------------------");
	        	}
	        	Log.printLine("=======================================");
	        }

	    }
}
