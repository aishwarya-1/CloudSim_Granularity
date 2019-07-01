package org.cloudbus.cloudsim.examples;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;



import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class CloudSimExample9
{
    /**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
    @SuppressWarnings("unused")
    
    public static void main(String[] args)
    {
        try
        {
            Class.forName("GeeksForGeeks"); 
            //Initialize the Cloudsim Package.

            int numUser = 1;
            Calendar cal = Calendar.getInstance(); //Starting time of the simulation.
            boolean traceFlag = false;
            
            CloudSim.init(numUser, cal, traceFlag);

            // Second step: Create Datacenters
            // Datacenters are the resource providers in CloudSim. We need at
            // list one of them to run a CloudSim simulation
            Datacenter dc = CreateDataCenter();
            DatacenterBroker dcb = null;

            //3.0 BROKER
            try {
                dcb = new DatacenterBroker("DatacenterBroker1");

                
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }

            //4.0 Cloudlets

            //create cloudlet list

            List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();

            long length = 40000;
            int pesNumber = 1; //1 core cpu
            long fileSize = 300;
            long outputSize = 400;
            UtilizationModelFull fullUtilize = new UtilizationModelFull();

            for(int id = 0; id<40; id++)
            {

                Cloudlet c = new Cloudlet(id, length, pesNumber, 
                                        fileSize, outputSize, 
                                        fullUtilize, 
                                        fullUtilize, 
                                        fullUtilize);

                c.setUserId(dcb.getId());
                //c.setVmId(vmid);                       
                cloudletList.add(c);                        
            }
        
        
            //5.0 VMs

            //create vm list
            List<Vm> vmList = new ArrayList<Vm>();

            // VM description
                
                int mips = 1000;
                long diskSize = 20000; // image size (MB)
                int ram = 2000; // vm memory (MB)
                long bw = 1000;
                int vcpu = 1; // number of cpus
                String vmm = "Xen"; // VMM name

                //create VM
                for(int vmid = 0; vmid<40; vmid++)
                {
                    Vm vm = new Vm(vmid, dcb.getId(), mips, 
                                    vcpu, ram, bw, diskSize, 
                                    vmm, new CloudletSchedulerTimeShared());
                    vmList.add(vm);
                }
                dcb.submitCloudletList(cloudletList);
                dcb.submitVmList(vmList);
            
            //6.0 START SIM
                CloudSim.startSimulation();
                List<Cloudlet> finalCloudletExecutionResults = dcb.getCloudletReceivedList();

            //7.0 STOP SIM
                CloudSim.stopSimulation();



            //Final step: Print results when simulation is over
            int cloudletNo = 0;
            for(Cloudlet c : finalCloudletExecutionResults)
            {
                
                Log.printLine("Result of cloudlet No" + cloudletNo);
                Log.printLine("##################################");
                Log.printLine("ID"+c.getCloudletId()+", VM:"+c.getVmId()+", status:"+c.getStatus()+", Submit: "+ c.getActualCPUTime() + ", Start:"+c.getExecStartTime()+", Finish:"+c.getFinishTime());
                Log.printLine("##################################");       
                
            }
        }
        // catch (Exception e) {
        //     e.printStackTrace();
        //     Log.printLine("Unwanted errors happen");
        // }
        catch (ClassNotFoundException ex) 
        { 
            ex.printStackTrace(); 
        } 

    }


    private static Datacenter CreateDataCenter() {

        //creating the processing element list.
        List<Pe> peList1 = new ArrayList<Pe>();

        // A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
        PeProvisionerSimple peProvisioner = new PeProvisionerSimple(1000); //1000 mips

        Pe core1 = new Pe(0, peProvisioner);
        peList1.add(core1);
        Pe core2 = new Pe(1, peProvisioner);
        peList1.add(core2);
        Pe core3 = new Pe(2, peProvisioner);
        peList1.add(core3);
        Pe core4 = new Pe(3, peProvisioner);
        peList1.add(core4);

        List<Host> hostList = new ArrayList<Host>();

        int ram = 8000; //8GB ram
        int bw = 8000; //8kbits/s
        long storage = 100000;
        //VmSchedulerSpaceShared vmScheduler = new VmSchedulerSpaceShared(peList1);

        Host host1 = new Host(0, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList1, new VmSchedulerSpaceShared(peList1));
        Host host2 = new Host(1, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList1, new VmSchedulerSpaceShared(peList1));
        Host host3 = new Host(2, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList1, new VmSchedulerSpaceShared(peList1));
        Host host4 = new Host(3, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList1, new VmSchedulerSpaceShared(peList1));

        hostList.add(host1);
        hostList.add(host2);
        hostList.add(host3);
        hostList.add(host4);

        String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 5.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 1.0; // the cost of using memory in this resource
		double costPerStorage = 0.05; // the cost of using storage in this
										// resource
		double costPerBw = 0.10; // the cost of using bw in this resource
		LinkedList<Storage> SANstorage = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);


        Datacenter dc = null;
        try {

            dc = new Datacenter("Datacenter1", characteristics, new VmAllocationPolicySimple(hostList), SANstorage, 1);
            
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
         

        return dc;
    }
}