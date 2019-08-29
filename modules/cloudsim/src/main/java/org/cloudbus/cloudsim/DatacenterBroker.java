/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.lists.VmList;

/**
 * DatacentreBroker represents a broker acting on behalf of a user. It hides VM management, as vm
 * creation, submission of cloudlets to VMs and destruction of VMs.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class DatacenterBroker extends SimEntity {
	
	/** The list of VM Ids for each Cloudlet*/
	public List <Integer> cloudlet_vmid = new ArrayList<Integer>();

	/** The list of VMs submitted to be managed by the broker. */
	protected List<? extends Vm> vmList;

	/** The list of VMs created by the broker. */
	protected List<? extends Vm> vmsCreatedList;

	/** The list of cloudlet submitted to the broker. 
         * @see #submitCloudletList(java.util.List) 
         */
	protected List<? extends Cloudlet> cloudletList;

	/** The list of submitted cloudlets. */
	protected List<? extends Cloudlet> cloudletSubmittedList;

	/** The list of received cloudlet. */
	protected List<? extends Cloudlet> cloudletReceivedList;

	/** The number of submitted cloudlets. */
	protected int cloudletsSubmitted;

	/** The number of requests to create VM. */
	protected int vmsRequested;

	/** The number of acknowledges (ACKs) sent in response to
         * VM creation requests. */
	protected int vmsAcks;

	/** The number of destroyed VMs. */
	protected int vmsDestroyed;

	/** The id's list of available datacenters. */
	protected List<Integer> datacenterIdsList;

	/** The list of datacenters where was requested to place VMs. */
	protected List<Integer> datacenterRequestedIdsList;

	/** The vms to datacenters map, where each key is a VM id
         * and each value is the datacenter id whwere the VM is placed. */
	protected Map<Integer, Integer> vmsToDatacentersMap;

	/** The datacenter characteristics map where each key
         * is a datacenter id and each value is its characteristics.. */
	protected Map<Integer, DatacenterCharacteristics> datacenterCharacteristicsList;

	/**
	 * Created a new DatacenterBroker object.
	 * 
	 * @param name name to be associated with this entity (as required by {@link SimEntity} class)
	 * @throws Exception the exception
	 * @pre name != null
	 * @post $none
	 */
	public DatacenterBroker(String name) throws Exception {
		super(name);

		setVmList(new ArrayList<Vm>());
		setVmsCreatedList(new ArrayList<Vm>());
		setCloudletList(new ArrayList<Cloudlet>());
		setCloudletSubmittedList(new ArrayList<Cloudlet>());
		setCloudletReceivedList(new ArrayList<Cloudlet>());

		cloudletsSubmitted = 0;
		setVmsRequested(0);
		setVmsAcks(0);
		setVmsDestroyed(0);

		setDatacenterIdsList(new LinkedList<Integer>());
		setDatacenterRequestedIdsList(new ArrayList<Integer>());
		setVmsToDatacentersMap(new HashMap<Integer, Integer>());
		setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());
	}

	/**
	 * This method is used to send to the broker the list with virtual machines that must be
	 * created.
	 * 
	 * @param list the list
	 * @pre list !=null
	 * @post $none
	 */
	public void submitVmList(List<? extends Vm> list) {
		getVmList().addAll(list);
	}

	/**
	 * This method is used to send to the broker the list of cloudlets.
	 * 
	 * @param list the list
	 * @pre list !=null
	 * @post $none
         * 
         * @todo The name of the method is confused with the {@link #submitCloudlets()},
         * that in fact submit cloudlets to VMs. The term "submit" is being used
         * ambiguously. The method {@link #submitCloudlets()} would be named "sendCloudletsToVMs"
         * 
         * The method {@link #submitVmList(java.util.List)} may have
         * be checked too.
	 */
	public void submitCloudletList(List<? extends Cloudlet> list) {
		getCloudletList().addAll(list);
	}

	/**
	 * Specifies that a given cloudlet must run in a specific virtual machine.
	 * 
	 * @param cloudletId ID of the cloudlet being bount to a vm
	 * @param vmId the vm id
	 * @pre cloudletId > 0
	 * @pre id > 0
	 * @post $none
	 */
	public void bindCloudletToVm(int cloudletId, int vmId) {
		CloudletList.getById(getCloudletList(), cloudletId).setVmId(vmId);
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
		// Resource characteristics request
			case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
				processResourceCharacteristicsRequest(ev);
				break;
			// Resource characteristics answer
			case CloudSimTags.RESOURCE_CHARACTERISTICS:
				processResourceCharacteristics(ev);
				break;
			// VM Creation answer
			case CloudSimTags.VM_CREATE_ACK:
				processVmCreate(ev);
				break;
			// A finished cloudlet returned
			case CloudSimTags.CLOUDLET_RETURN:
				processCloudletReturn(ev);
				break;
			// if the simulation finishes
			case CloudSimTags.END_OF_SIMULATION:
				shutdownEntity();
				break;
			// other unknown tags are processed by this method
			default:
				processOtherEvent(ev);
				break;
		}
	}

	/**
	 * Process the return of a request for the characteristics of a Datacenter.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != $null
	 * @post $none
	 */
	protected void processResourceCharacteristics(SimEvent ev) {
		DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
		getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

		if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
			setDatacenterRequestedIdsList(new ArrayList<Integer>());
			createVmsInDatacenter(getDatacenterIdsList().get(0));
		}
	}

	/**
	 * Process a request for the characteristics of a PowerDatacenter.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != $null
	 * @post $none
	 */
	protected void processResourceCharacteristicsRequest(SimEvent ev) {
		setDatacenterIdsList(CloudSim.getCloudResourceList());
		setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());

		Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Cloud Resource List received with ",
				getDatacenterIdsList().size(), " resource(s)");

		for (Integer datacenterId : getDatacenterIdsList()) {
			sendNow(datacenterId, CloudSimTags.RESOURCE_CHARACTERISTICS, getId());
		}
	}

	/**
	 * Process the ack received due to a request for VM creation.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != null
	 * @post $none
	 */
	protected void processVmCreate(SimEvent ev) {
		int[] data = (int[]) ev.getData();
		int datacenterId = data[0];
		int vmId = data[1];
		int result = data[2];

		if (result == CloudSimTags.TRUE) {
			getVmsToDatacentersMap().put(vmId, datacenterId);
			getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
//			List<Host> hostList = VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getHostList();
//			if(VmList.getById(getVmsCreatedList(), vmId).getHost().getId() > 0) {
//				List<Host> l_hostList = VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getHostList();
//			}
//			String a = VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getAisle().getZone().getAddress();
//			Log.printLine(a);
//			String b = VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getAisle().getAddress();
//			Log.printLine(b);
//			String c = VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getAddress();
//			Log.printLine(c);
			String d = VmList.getById(getVmsCreatedList(), vmId).getHost().getAddress();
			Log.printLine(d);
//			String e = VmList.getById(getVmsCreatedList(), vmId).getAddress();
//			Log.printLine(e);
			Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": VM #", vmId,
					" has been created in Datacenter #", datacenterId, 
					", Zone #", VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getAisle().getZone().getId(),
					", Aisle #", VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getAisle().getId(),
					", Rack #", VmList.getById(getVmsCreatedList(), vmId).getHost().getRack().getId(),
					" Host #", 	VmList.getById(getVmsCreatedList(), vmId).getHost().getId(),
					" Host Characteristics #", 	VmList.getById(getVmsCreatedList(), vmId).getHost().getCharacteristics()
//					" L Host # ", (VmList.getById(getVmsCreatedList(), vmId).getHost().getRack()).getL_host(VmList.getById(getVmsCreatedList(), vmId).getHost().getId(), hostlist),
//					" R Host # ", (VmList.getById(getVmsCreatedList(), vmId).getHost().getRack()).getR_host(VmList.getById(getVmsCreatedList(), vmId).getHost().getId(), hostlist)
					);
		} else {
			Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Creation of VM #", vmId,
					" failed in Datacenter #", datacenterId);
		}

		incrementVmsAcks();

		// all the requested VMs have been created
		if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) {
			submitCloudlets();
		} else {
			// all the acks received, but some VMs were not created
			if (getVmsRequested() == getVmsAcks()) {
				// find id of the next datacenter that has not been tried
				for (int nextDatacenterId : getDatacenterIdsList()) {
					if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
						createVmsInDatacenter(nextDatacenterId);
						return;
					}
				}

				// all datacenters already queried
				if (getVmsCreatedList().size() > 0) { // if some vm were created
					submitCloudlets();
				} else { // no vms created. abort
					Log.printLine(CloudSim.clock() + ": " + getName()
							+ ": none of the required VMs could be created. Aborting");
					finishExecution();
				}
			}
		}
	}

	/**
	 * Process a cloudlet return event.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != $null
	 * @post $none
	 */
	protected void processCloudletReturn(SimEvent ev) {
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		getCloudletReceivedList().add(cloudlet);
		Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Cloudlet ", cloudlet.getCloudletId(),
				" received");
		
		cloudlet_vmid.add(cloudlet.getVmId());
		
		cloudletsSubmitted--;
		if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all cloudlets executed
//			Log.printLine(getVmsCreatedList().size());
//			Log.printLine(cloudlet_vmid);
			
			int count_vmid[]= new int[getVmsCreatedList().size()];
			
			int hostid[] = new int[cloudlet_vmid.size()];
			List <Integer> host = new ArrayList<Integer>();
			Set<Integer> host_hash_Set = new HashSet<Integer>();
			
			int rackid[] = new int[cloudlet_vmid.size()];
			List <Integer> rack = new ArrayList<Integer>();
			Set<Integer> rack_hash_Set = new HashSet<Integer>();
			
			int aisleid[] = new int[cloudlet_vmid.size()];
			List <Integer> aisle = new ArrayList<Integer>();
			Set<Integer> aisle_hash_Set = new HashSet<Integer>();
			
			int zoneid[] = new int[cloudlet_vmid.size()];
			List <Integer> zone = new ArrayList<Integer>();
			Set<Integer> zone_hash_Set = new HashSet<Integer>();
			
			int dcid[] = new int[cloudlet_vmid.size()];
			List <Integer> dc = new ArrayList<Integer>();
			Set<Integer> dc_hash_Set = new HashSet<Integer>();
			
			for(int i = 0;i < cloudlet_vmid.size();i++) {
				count_vmid[cloudlet_vmid.get(i)]++;
				hostid[i] = VmList.getById(getVmsCreatedList(), cloudlet_vmid.get(i)).getHost().getId();
				host_hash_Set.add(hostid[i]);
				host.add(hostid[i]);
				rackid[i] = VmList.getById(getVmsCreatedList(), cloudlet_vmid.get(i)).getHost().getRack().getId();
				rack_hash_Set.add(rackid[i]);
				rack.add(rackid[i]);
				aisleid[i] = VmList.getById(getVmsCreatedList(), cloudlet_vmid.get(i)).getHost().getRack().getAisle().getId();
				aisle_hash_Set.add(aisleid[i]);
				aisle.add(aisleid[i]);
				zoneid[i] = VmList.getById(getVmsCreatedList(), cloudlet_vmid.get(i)).getHost().getRack().getAisle().getZone().getId();
				zone_hash_Set.add(zoneid[i]);
				zone.add(zoneid[i]);
				dcid[i] = VmList.getById(getVmsCreatedList(), cloudlet_vmid.get(i)).getHost().getRack().getAisle().getZone().getDatacenter().getId();
				dc_hash_Set.add(dcid[i]);
				dc.add(dcid[i]);
			}
			
//			Log.printLine("The array host is: "+ host);
//			Log.printLine("The set host hashset is: " + host_hash_Set);
//			Log.printLine("The array rack is: "+ rack);
//			Log.printLine("The set rack hashset is: " + rack_hash_Set);
//			Log.printLine("The array aisle is: "+ aisle);
//			Log.printLine("The set aisle hashset is: " + aisle_hash_Set);
//			Log.printLine("The array zone is: "+ zone);
//			Log.printLine("The set zone hashset is: " + zone_hash_Set);
//			Log.printLine("The array Datacenter is: "+ dc);
//			Log.printLine("The set Datacenter hashset is: " + dc_hash_Set);
			
			//int count_hostid[] = new int[host_hash_Set.size()];
			//int count_rackid[] = new int[rack_hash_Set.size()];
//			int count_aisleid[] = new int[aisle_hash_Set.size()];
//			int count_zoneid[] = new int[zone_hash_Set.size()];
			int count_hostid[] = new int[max(hostid)+1];
			int count_rackid[] = new int[max(rackid)+1];
			int count_aisleid[] = new int[max(aisleid)+1];
			int count_zoneid[] = new int[max(zoneid)+1];
			int count_dcid[] = new int[dc_hash_Set.size()];
			
			//int count_dcid[] = new int[max(dcid)+1];
			
			for (int i = 0; i < count_vmid.length; i++) {
				Log.printLine("Vm "+ i + " has " + count_vmid[i]+ " cloudlets");
			}
			
			for (int i = 0; i<hostid.length; i++) {
				count_hostid[hostid[i]]++;
			}
			
			for(int i= 0;i<count_hostid.length;i++) {
				Log.printLine("Host "+ i + " has " + count_hostid[i]+ " cloudlets");
			}
			
			for (int i = 0; i<rackid.length; i++) {
				count_rackid[rackid[i]]++;
			}
			
			
			for(int i= 0;i<count_rackid.length;i++) {
				Log.printLine("Rack "+ i + " has " + count_rackid[i]+ " cloudlets");
			}
			
			for (int i = 0; i<aisleid.length; i++) {
				count_aisleid[aisleid[i]]++;
			}
			
			for(int i= 0;i<count_aisleid.length;i++) {
				Log.printLine("Aisle "+ i + " has " + count_aisleid[i]+ " cloudlets");
			}
			
			for (int i = 0; i<zoneid.length; i++) {
				count_zoneid[zoneid[i]]++;
			}
			
			for(int i= 0;i<count_zoneid.length;i++) {
				Log.printLine("Zone "+ i + " has " + count_zoneid[i]+ " cloudlets");
			}
			
			for (int i = 0; i<dcid.length; i++) {
				count_dcid[dcid[i]-2]++;
			}
			
			for(int i= 0;i<count_dcid.length;i++) {
				int j = i + 2;
				Log.printLine("Datacenter "+ j + " has " + count_dcid[i]+ " cloudlets");
			}
						
			Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": All Cloudlets executed. Finishing...");
			clearDatacenters();
			finishExecution();
		} else { // some cloudlets haven't finished yet
			if (getCloudletList().size() > 0 && cloudletsSubmitted == 0) {
				// all the cloudlets sent finished. It means that some bount
				// cloudlet is waiting its VM be created
				clearDatacenters();
				createVmsInDatacenter(0);
			}

		}
	}

	private int max(int[] a) {
		// TODO Auto-generated method stub
		int max = a[0];
		for(int i = 1;i<a.length; i++)
		{
			if(a[i]>max)
			{
				max = a[i];
			}
		}
		return max;
	}

	/**
	 * Process non-default received events that aren't processed by
         * the {@link #processEvent(org.cloudbus.cloudsim.core.SimEvent)} method.
         * This method should be overridden by subclasses in other to process
         * new defined events.
	 * 
	 * @param ev a SimEvent object
	 * @pre ev != null
	 * @post $none
         * @todo to ensure the method will be overridden, it should be defined 
         * as abstract in a super class from where new brokers have to be extended.
	 */
	protected void processOtherEvent(SimEvent ev) {
		if (ev == null) {
			Log.printConcatLine(getName(), ".processOtherEvent(): ", "Error - an event is null.");
			return;
		}

		Log.printConcatLine(getName(), ".processOtherEvent(): Error - event unknown by this DatacenterBroker.");
	}

	/**
	 * Create the submitted virtual machines in a datacenter.
	 * 
	 * @param datacenterId Id of the chosen Datacenter
	 * @pre $none
	 * @post $none
         * @see #submitVmList(java.util.List) 
	 */
	protected void createVmsInDatacenter(int datacenterId) {
		// send as much vms as possible for this datacenter before trying the next one
		int requestedVms = 0;
		String datacenterName = CloudSim.getEntityName(datacenterId);
		for (Vm vm : getVmList()) {
			if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
				Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId()
						+ " in " + datacenterName);
				sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
				requestedVms++;
			}
		}

		getDatacenterRequestedIdsList().add(datacenterId);

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	}

	/**
	 * Submit cloudlets to the created VMs.
	 * 
	 * @pre $none
	 * @post $none
         * @see #submitCloudletList(java.util.List) 
	 */
	protected void submitCloudlets() {
		int vmIndex = 0;
		List<Cloudlet> successfullySubmitted = new ArrayList<Cloudlet>();
		for (Cloudlet cloudlet : getCloudletList()) {
			Vm vm=null;
			// if user didn't bind this cloudlet and it has not been executed yet
			if (cloudlet.getVmId() == -1) {
					//Checking if enough number of processing elements and enough storage capacity is available in VMs.
				int count = 0;
				while(count<getVmsCreatedList().size()) {
					vm = getVmsCreatedList().get(vmIndex);
					if(cloudlet.getNumberOfPes() > vm.getNumberOfPes()) {
						cloudlet.setVmId(-1);
						count++; //To check if we have iterated through all possible vms.
						vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
						
					}
					else if(cloudlet.getCloudletFileSize() > vm.getSize()) {
						cloudlet.setVmId(-1);
						count++;
						vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
					}
					else {
						Log.printLine(CloudSim.clock() + ": " + getName() + ": cloudlet "+cloudlet.getCloudletId() + " is being sent to Vm "+ vm.getId() );
						vm.setSize(vm.getSize() - cloudlet.getCloudletFileSize());//setting the new size once cloudlet is assigned to this vm.
						vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
						cloudlet.setVmId(vm.getId());//assigning the cloudlets to the vm.
						break;
					}
				}
				if(cloudlet.getVmId() == -1) {
					Log.printLine("Cloudlet "+cloudlet.getCloudletId()+" could not be assigned to any vm.");
					getCloudletList().remove(cloudlet.getCloudletId());
					break;
				}
				
			} else { // submit to the specific vm
				vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
				if (vm == null) { // vm was not created
					if(!Log.isDisabled()) {				    
					    Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Postponing execution of cloudlet ",
							cloudlet.getCloudletId(), ": bount VM not available");
					}
					continue;
				}
			}

			if (!Log.isDisabled()) {
			    Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Cloudlet sent ",
					cloudlet.getCloudletId(), " to VM #", vm.getId());
			}
			
			cloudlet.setVmId(vm.getId());
			sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
			cloudletsSubmitted++;
			//vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
			getCloudletSubmittedList().add(cloudlet);
			successfullySubmitted.add(cloudlet);
		}

		// remove submitted cloudlets from waiting list
		getCloudletList().removeAll(successfullySubmitted);
	}

	/**
	 * Destroy all virtual machines running in datacenters.
	 * 
	 * @pre $none
	 * @post $none
	 */
	protected void clearDatacenters() {
		for (Vm vm : getVmsCreatedList()) {
			Log.printConcatLine(CloudSim.clock(), ": " + getName(), ": Destroying VM #", vm.getId());
			sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.VM_DESTROY, vm);
		}

		getVmsCreatedList().clear();
	}

	/**
	 * Send an internal event communicating the end of the simulation.
	 * 
	 * @pre $none
	 * @post $none
	 */
	protected void finishExecution() {
		sendNow(getId(), CloudSimTags.END_OF_SIMULATION);
	}

	@Override
	public void shutdownEntity() {
		Log.printConcatLine(getName(), " is shutting down...");
	}

	@Override
	public void startEntity() {
		Log.printConcatLine(getName(), " is starting...");
		schedule(getId(), 0, CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST);
	}

	/**
	 * Gets the vm list.
	 * 
	 * @param <T> the generic type
	 * @return the vm list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Vm> List<T> getVmList() {
		return (List<T>) vmList;
	}

	/**
	 * Sets the vm list.
	 * 
	 * @param <T> the generic type
	 * @param vmList the new vm list
	 */
	protected <T extends Vm> void setVmList(List<T> vmList) {
		this.vmList = vmList;
	}

	/**
	 * Gets the cloudlet list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Cloudlet> List<T> getCloudletList() {
		return (List<T>) cloudletList;
	}

	/**
	 * Sets the cloudlet list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletList the new cloudlet list
	 */
	protected <T extends Cloudlet> void setCloudletList(List<T> cloudletList) {
		this.cloudletList = cloudletList;
	}

	/**
	 * Gets the cloudlet submitted list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet submitted list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Cloudlet> List<T> getCloudletSubmittedList() {
		return (List<T>) cloudletSubmittedList;
	}

	/**
	 * Sets the cloudlet submitted list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletSubmittedList the new cloudlet submitted list
	 */
	protected <T extends Cloudlet> void setCloudletSubmittedList(List<T> cloudletSubmittedList) {
		this.cloudletSubmittedList = cloudletSubmittedList;
	}

	/**
	 * Gets the cloudlet received list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet received list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Cloudlet> List<T> getCloudletReceivedList() {
		return (List<T>) cloudletReceivedList;
	}

	/**
	 * Sets the cloudlet received list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletReceivedList the new cloudlet received list
	 */
	protected <T extends Cloudlet> void setCloudletReceivedList(List<T> cloudletReceivedList) {
		this.cloudletReceivedList = cloudletReceivedList;
	}

	/**
	 * Gets the vm list.
	 * 
	 * @param <T> the generic type
	 * @return the vm list
	 */
	@SuppressWarnings("unchecked")
	public <T extends Vm> List<T> getVmsCreatedList() {
		return (List<T>) vmsCreatedList;
	}

	/**
	 * Sets the vm list.
	 * 
	 * @param <T> the generic type
	 * @param vmsCreatedList the vms created list
	 */
	protected <T extends Vm> void setVmsCreatedList(List<T> vmsCreatedList) {
		this.vmsCreatedList = vmsCreatedList;
	}

	/**
	 * Gets the vms requested.
	 * 
	 * @return the vms requested
	 */
	protected int getVmsRequested() {
		return vmsRequested;
	}

	/**
	 * Sets the vms requested.
	 * 
	 * @param vmsRequested the new vms requested
	 */
	protected void setVmsRequested(int vmsRequested) {
		this.vmsRequested = vmsRequested;
	}

	/**
	 * Gets the vms acks.
	 * 
	 * @return the vms acks
	 */
	protected int getVmsAcks() {
		return vmsAcks;
	}

	/**
	 * Sets the vms acks.
	 * 
	 * @param vmsAcks the new vms acks
	 */
	protected void setVmsAcks(int vmsAcks) {
		this.vmsAcks = vmsAcks;
	}

	/**
	 * Increment the number of acknowledges (ACKs) sent in response
         * to requests of VM creation.
	 */
	protected void incrementVmsAcks() {
		vmsAcks++;
	}

	/**
	 * Gets the vms destroyed.
	 * 
	 * @return the vms destroyed
	 */
	protected int getVmsDestroyed() {
		return vmsDestroyed;
	}

	/**
	 * Sets the vms destroyed.
	 * 
	 * @param vmsDestroyed the new vms destroyed
	 */
	protected void setVmsDestroyed(int vmsDestroyed) {
		this.vmsDestroyed = vmsDestroyed;
	}

	/**
	 * Gets the datacenter ids list.
	 * 
	 * @return the datacenter ids list
	 */
	protected List<Integer> getDatacenterIdsList() {
		return datacenterIdsList;
	}

	/**
	 * Sets the datacenter ids list.
	 * 
	 * @param datacenterIdsList the new datacenter ids list
	 */
	protected void setDatacenterIdsList(List<Integer> datacenterIdsList) {
		this.datacenterIdsList = datacenterIdsList;
	}

	/**
	 * Gets the vms to datacenters map.
	 * 
	 * @return the vms to datacenters map
	 */
	protected Map<Integer, Integer> getVmsToDatacentersMap() {
		return vmsToDatacentersMap;
	}

	/**
	 * Sets the vms to datacenters map.
	 * 
	 * @param vmsToDatacentersMap the vms to datacenters map
	 */
	protected void setVmsToDatacentersMap(Map<Integer, Integer> vmsToDatacentersMap) {
		this.vmsToDatacentersMap = vmsToDatacentersMap;
	}

	/**
	 * Gets the datacenter characteristics list.
	 * 
	 * @return the datacenter characteristics list
	 */
	protected Map<Integer, DatacenterCharacteristics> getDatacenterCharacteristicsList() {
		return datacenterCharacteristicsList;
	}

	/**
	 * Sets the datacenter characteristics list.
	 * 
	 * @param datacenterCharacteristicsList the datacenter characteristics list
	 */
	protected void setDatacenterCharacteristicsList(
			Map<Integer, DatacenterCharacteristics> datacenterCharacteristicsList) {
		this.datacenterCharacteristicsList = datacenterCharacteristicsList;
	}

	/**
	 * Gets the datacenter requested ids list.
	 * 
	 * @return the datacenter requested ids list
	 */
	protected List<Integer> getDatacenterRequestedIdsList() {
		return datacenterRequestedIdsList;
	}

	/**
	 * Sets the datacenter requested ids list.
	 * 
	 * @param datacenterRequestedIdsList the new datacenter requested ids list
	 */
	protected void setDatacenterRequestedIdsList(List<Integer> datacenterRequestedIdsList) {
		this.datacenterRequestedIdsList = datacenterRequestedIdsList;
	}

}
