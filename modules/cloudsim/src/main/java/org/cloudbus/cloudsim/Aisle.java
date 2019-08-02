package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.RackList;

@SuppressWarnings("unused")

public class Aisle {

	/** The id of the aisle. */
	private int id;

	/** The Racks of the aisle, that
         * represent the racks containing servers.*/
	private List<? extends Rack> rackList;

	/** Tells whether this zone is working properly or has failed. */
	//private boolean failed;

	/** The zone where the aisle is placed. */
	private Zone zone;

	/**
	 * Instantiates a new aisle.
	 * 
	 * @param id the aisle id
	 * @param rackList the aisle's Racks list
	 */
	public Aisle(int id, List<? extends Rack> rackList){
		setId(id);
		setRackList(rackList);
		setRack();
	}

	/**
	 * Gets the aisle id.
	 * 
	 * @return the aisle id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the aisle id.
	 * 
	 * @param id the new aisle id
	 */
	protected void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the rack list.
	 * 
	 * @param <T> the generic type
	 * @return the rack list
         * @todo check this warning below
	 */
	@SuppressWarnings("unchecked")
	public <T extends Rack> List<T> getRackList() {
		return (List<T>) rackList;
	}

	
	/**
	 * Sets the rack list.
	 * 
	 * @param <T> the generic type
	 * @param rackList the new rack list
	 */
	protected <T extends Rack> void setRackList(List<T> rackList) {
		this.rackList = rackList;
	}
	
	public void setRack() {
		for (Rack rack : getRackList()) {
			rack.setAisle(this);
		}
	}
	
	
	/**
	 * Gets the zone of the aisle.
	 * 
	 * @return the zone where the aisle is present
	 */
	public Zone getZone() {
		return zone;
	}

	/**
	 * Sets the zone of the aisle.
	 * 
	 * @param zone of the the aisle
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	/**
	 * 
	 * @return the address of the host.
	 */
	
	public String getAddress() {
		int aisleid = getId();
		int zoneid = getZone().getId();
		int dcid = getZone().getDatacenter().getId();
		String address = Integer.toString(dcid)+"_"+Integer.toString(zoneid)+"_"+Integer.toString(aisleid);
//		Log.printLine(address);
		return address;
	}


}
