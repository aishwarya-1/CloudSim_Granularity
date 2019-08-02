package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.HostList;

@SuppressWarnings("unused")

public class Rack {

	/** The id of the rack. */
	private int id;

	/** The hosts of the rack, that
         * represent the servers.*/
	private List<? extends Host> hostList;

	/** Tells whether this zone is working properly or has failed. */
	//private boolean failed;

	/** The Aisle where the rack is placed. */
	private Aisle aisle;

	/**
	 * Instantiates a new rack.
	 * 
	 * @param id the rack id
	 * @param hostList the rack's Hosts list
	 */
	public Rack(int id, List<? extends Host> hostList){
		setId(id);
		setHostList(hostList);
		setHost();
	}

	/**
	 * Gets the rack id.
	 * 
	 * @return the rack id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the rack id.
	 * 
	 * @param id the new rack id
	 */
	protected void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the host list.
	 * 
	 * @param <T> the generic type
	 * @return the host list
         * @todo check this warning below
	 */
	@SuppressWarnings("unchecked")
	public <T extends Host> List<T> getHostList() {
		return (List<T>) hostList;
	}

	
	/**
	 * Sets the host list.
	 * @param hostList 
	 * 
	 * @param <T> the generic type
	 * @param hostList the new host list
	 */
	protected <T extends Host> void setHostList(List<? extends Host> hostList) {
		this.hostList = hostList;
	}
	public void setHost() {
		for (Host host : getHostList()) {
			host.setRack(this);
		}
	}
	
	
	/**
	 * Gets the aisle of the rack.
	 * 
	 * @return the aisle where the rack is present
	 */
	public Aisle getAisle() {
		return aisle;
	}

	/**
	 * Sets the aisle of the rack.
	 * 
	 * @param aisle of the the rack
	 */
	public void setAisle(Aisle aisle) {
		this.aisle = aisle;
	}
	
	/**
	 * @param <T>
	 * @return 
	 * @return the l_host
	 */
	public int getL_host(int hostId) {
		if(hostId>0) {
			return (HostList.getById(getHostList(), hostId-1)).getId();
		}
		else{
			return -1;
		}
	}
	
	/**
	 * @param <T>
	 * @return 
	 * @return the l_host
	 */
	public int getR_host(int hostId) {
		if(hostId<HostList.size(getHostList())-1) {
			return (HostList.getById(getHostList(), hostId+1)).getId();
		}
		else{
			return -1;
		}
	}
	
	/**
	 * 
	 * @return the address of the host.
	 */
	
	public String getAddress() {
		int rackid = getId();
		int aisleid = getAisle().getId();
		int zoneid = getAisle().getZone().getId();
		int dcid = getAisle().getZone().getDatacenter().getId();
		String address = Integer.toString(dcid)+"_"+Integer.toString(zoneid)+"_"+Integer.toString(aisleid)+"_"+Integer.toString(rackid);
//		Log.printLine(address);
		return address;
	}


}
