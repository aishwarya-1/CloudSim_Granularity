package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.AisleList;

@SuppressWarnings("unused")

public class Zone {

	/** The id of the zone. */
	private int id;

	/** The Aisles of the zone, that
         * represent the row of racks containing servers.*/
	private List<? extends Aisle> aisleList;

	/** Tells whether this zone is working properly or has failed. */
	//private boolean failed;

	/** The datacenter where the zone is placed. */
	private Datacenter datacenter;

	/**
	 * Instantiates a new zone.
	 * 
	 * @param id the zone id
	 * @param hostList the zone's Hosts list
	 */
	public Zone(int id, List<? extends Aisle> aisleList){
		setId(id);
		setAisleList(aisleList);
		setAisle();
	}

	/**
	 * Gets the zone id.
	 * 
	 * @return the zone id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the zone id.
	 * 
	 * @param id the new zone id
	 */
	protected void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the aisle list.
	 * 
	 * @param <T> the generic type
	 * @return the aisle list
         * @todo check this warning below
	 */
	@SuppressWarnings("unchecked")
	public <T extends Aisle> List<T> getAisleList() {
		return (List<T>) aisleList;
	}

	
	/**
	 * Sets the aisle list.
	 * 
	 * @param <T> the generic type
	 * @param aisleList the new aisle list
	 */
	protected <T extends Aisle> void setAisleList(List<T> aisleList) {
		this.aisleList = aisleList;
	}
	
	public void setAisle() {
		for (Aisle aisle : getAisleList()) {
			aisle.setZone(this);
		}
	}
	
//	/**
//	 * @param <T>
//	 * @return 
//	 * @return the l_host
//	 */
//	public int getL_host(int hostId) {
//		if(hostId>0) {
//			return (HostList.getById(getHostList(), hostId-1)).getId();
//		}
//		else{
//			return -1;
//		}
//	}
//	
//	/**
//	 * @param <T>
//	 * @return 
//	 * @return the l_host
//	 */
//	public int getR_host(int hostId) {
//		if(hostId<HostList.size(getHostList())-1) {
//			return (HostList.getById(getHostList(), hostId+1)).getId();
//		}
//		else{
//			return -1;
//		}
//	}
	
	/**
	 * Gets the data center of the zone.
	 * 
	 * @return the data center where the host runs
	 */
	public Datacenter getDatacenter() {
		return datacenter;
	}

	/**
	 * Sets the data center of the zone.
	 * 
	 * @param datacenter the data center from this host
	 */
	public void setDatacenter(Datacenter datacenter) {
		this.datacenter = datacenter;
	}

}
