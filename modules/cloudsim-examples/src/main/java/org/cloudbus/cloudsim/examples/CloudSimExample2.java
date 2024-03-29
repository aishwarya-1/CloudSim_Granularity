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

import org.cloudbus.cloudsim.Aisle;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.ConfReader;
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
public class CloudSimExample2 {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;

	/**
	 * Creates main() to run this example
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Log.printLine("Starting CloudSimExample2...");

	        try {
	        	// First step: Initialize the CloudSim package. It should be called
	            	// before creating any entities.
	            	int num_user = 1;   // number of cloud users
	            	Calendar calendar = Calendar.getInstance();
	            	boolean trace_flag = false;  // mean trace events
	            	
	            	//String conf1 = args[0];
	            	//String conf2 = args[1];
	            	
	            	// Initialize the CloudSim library
	            	CloudSim.init(num_user, calendar, trace_flag);

	            	// Second step: Create Datacenters
	            	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
	            	//@SuppressWarnings("unused")
					//Conf_reader r0 = new Conf_reader();
	            	ConfReader r0 = new ConfReader();
					//Datacenter dc = r0.create_infrastructure("/home/ubuntu/Documents/GreenSim/CloudSim_Granularity/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/GreenSim.conf");
					Datacenter dc = r0.create_infrastructure("/home/ubuntu/Documents/GreenSim/CloudSim_Granularity/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/GreenSim.conf","/home/ubuntu/Documents/GreenSim/CloudSim_Granularity/modules/cloudsim-examples/src/main/java/org/cloudbus/cloudsim/examples/GreenSim1.conf");

	            	//Third step: Create Broker
	            	DatacenterBroker broker = createBroker();
	            	int brokerId = broker.getId();

	            	//Fourth step: Create one virtual machine
	            	vmlist = new ArrayList<Vm>();

	            	//VM description
	            	int vmid = 0;
	            	int mips = 100;
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

	            	vmid++;
	            	Vm vm5 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	            	
	            	//vmid++;
	            	//Vm vm6 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	            	
//	            	vmid++;
//	            	Vm vm7 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//	            	
//	            	vmid++;
//	            	Vm vm8 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//	            	
//	            	vmid++;
//	            	Vm vm9 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

	            	//add the VMs to the vmList
	            	vmlist.add(vm1);
	            	vmlist.add(vm2);
	            	vmlist.add(vm3);
	            	vmlist.add(vm4);
	            	vmlist.add(vm5);
	            	//vmlist.add(vm6);
	       
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
	            	UtilizationModel utilizationModel = new UtilizationModelFull();

	            	Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	            	cloudlet1.setUserId(brokerId);

	            	id++;
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
	            	cloudletList.add(cloudlet9);

	            	//submit cloudlet list to the broker
	            	broker.submitCloudletList(cloudletList);


	            	//bind the cloudlets to the vms. This way, the broker
	            	// will submit the bound cloudlets only to the specific VM
	            	//broker.bindCloudletToVm(cloudlet1.getCloudletId(),vm1.getId());
	            	//broker.bindCloudletToVm(cloudlet2.getCloudletId(),vm2.getId());
	            	//broker.bindCloudletToVm(cloudlet3.getCloudletId(),vm3.getId());

	            	// Sixth step: Starts the simulation
	            	CloudSim.startSimulation();


	            	// Final step: Print results when simulation is over
	            	List<Cloudlet> newList = broker.getCloudletReceivedList();
	            	List<Cloudlet> cloudletlist = broker.getCloudletList();
	            	for (Cloudlet cloudlet : cloudletlist) {
	            		int cloudlet_vmid = cloudlet.getVmId();
	            		Log.printLine(cloudlet_vmid);
	            		Log.printLine("imple"+VmList.getById(broker.getVmsCreatedList(), cloudlet_vmid).getHost().getId());
	            	}

	            	CloudSim.stopSimulation();

	            	printCloudletList(newList);

	            	Log.printLine("CloudSimExample2 finished!");
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            Log.printLine("The simulation has been terminated due to an unexpected error");
	        }
	    }

		@SuppressWarnings("unused")
		private static Datacenter createDatacenter(String name){

	        // Here are the steps needed to create a PowerDatacenter:
	        // 1. We need to create a list to store
	    	//    our machine
	    	List<Host> hostList = new ArrayList<Host>();
	    	List<Host> hostList1 = new ArrayList<Host>();
	    	List<Host> hostList2 = new ArrayList<Host>();

	        // 2. A Machine contains one or more PEs or CPUs/Cores.
	    	// In this example, it will have only one core.
	    	List<Pe> peList = new ArrayList<Pe>();

	    	int mips = 1000;

	        // 3. Create PEs and add these into a list.
	    	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

	        //4. Create Host with its id and list of PEs and add them to the list of machines
	        int hostId=0;
	        int ram = 2048; //host memory (MB)
	        long storage = 1000000; //host storage
	        int bw = 10000;

	        hostList.add(
	    			new Host(
	    				hostId,
	    				new RamProvisionerSimple(ram),
	    				new BwProvisionerSimple(bw),
	    				storage,
	    				peList,
	    				new VmSchedulerTimeShared(peList)
	    			)
	    		);// This is our machine
	        hostId++;
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
	        hostList1.add(
	    			new Host(
	    				0,
	    				new RamProvisionerSimple(ram),
	    				new BwProvisionerSimple(bw),
	    				storage,
	    				peList,
	    				new VmSchedulerTimeShared(peList)
	    			)
	    		);
	        hostId++;
	        hostList2.add(
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
	        hostList2.add(
	    			new Host(
	    				hostId,
	    				new RamProvisionerSimple(ram),
	    				new BwProvisionerSimple(bw),
	    				storage,
	    				peList,
	    				new VmSchedulerTimeShared(peList)
	    			)
	    		);
	        
	        List<Rack> rackList = new ArrayList<Rack>();
	        rackList.add(new Rack(0, hostList));
	        rackList.add(new Rack(1, hostList1));   
	        
	        List<Rack> rackList1 = new ArrayList<Rack>();
	        rackList1.add(new Rack(2, hostList2));
	        
	        List<Aisle> aisleList = new ArrayList<Aisle>();
	        aisleList.add(new Aisle(0, rackList));
	        aisleList.add(new Aisle(1, rackList1));
	        
	        List<Zone> zoneList = new ArrayList<Zone>();
	        zoneList.add(new Zone(0, aisleList));


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

	    }
}
