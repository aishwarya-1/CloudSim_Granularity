package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.HostList;

//import org.cloudbus.cloudsim.lists.PeList;
//import org.cloudbus.cloudsim.provisioners.BwProvisioner;
//import org.cloudbus.cloudsim.provisioners.RamProvisioner;

@SuppressWarnings("unused")

public class Zone {

	/** The id of the zone. */
	private int id;

	/** The Hosts of the zone, that
         * represent the servers.*/
	private List<? extends Host> hostList;

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
	public Zone(int id, List<? extends Host> hostList){
		setId(id);
		setHostList(hostList);
		//setFailed(false);
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
	 * 
	 * @param <T> the generic type
	 * @param hostList the new host list
	 */
	protected <T extends Host> void setHostList(List<T> hostList) {
		this.hostList = hostList;
	}
	

//	/**
//	 * Checks if the zone hosts have failed.
//	 * 
//	 * @return true, if the zone hosts have failed; false otherwise
//	 */
//	public boolean isFailed() {
//		return failed;
//	}
//
//	/**
//	 * Sets the Hosts of the zone to a FAILED status. NOTE: <tt>resName</tt> is used for debugging
//	 * purposes, which is <b>ON</b> by default. Use {@link #setFailed(boolean)} if you do not want
//	 * this information.
//	 * 
//	 * @param resName the name of the resource
//	 * @param failed the failed
//	 * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
//	 */
//	public boolean setFailed(String resName, boolean failed) {
//		// all the Hosts are failed (or recovered, depending on fail)
//		this.failed = failed;
//		setStatusFailed(getHostList(), resName, getId(), failed);
//		return true;
//	}
//
//	/**
//	 * Sets the Hosts of the zone to a FAILED status.
//	 * 
//	 * @param failed the failed
//	 * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
//	 */
//	public boolean setFailed(boolean failed) {
//		// all the Hosts are failed (or recovered, depending on fail)
//		this.failed = failed;
//		setStatusFailed(getHostList(), failed);
//		return true;
//	}
//	
//	public static void setStatusFailed(HostList hostList, int hostId, int status)
//	{
//		
//	}
//
//
//	/**
//	 * Sets the particular Host status on the zone.
//	 * 
//	 * @param hostId the host id
//	 * @param status Host status, either <tt>Host.FREE</tt> or <tt>Host.BUSY</tt>
//	 * @return <tt>true</tt> if the Host status has changed, <tt>false</tt> otherwise (Host id might not
//	 *         be exist)
//	 * @pre hostID >= 0
//	 * @post $none
//	 */
//	public boolean setHostStatus(int hostId, int status) {
//		return HostList.setHostStatus(getHostList(), hostId, status);
//	}

	/**
	 * Gets the data center of the host.
	 * 
	 * @return the data center where the host runs
	 */
	public Datacenter getDatacenter() {
		return datacenter;
	}

	/**
	 * Sets the data center of the host.
	 * 
	 * @param datacenter the data center from this host
	 */
	public void setDatacenter(Datacenter datacenter) {
		this.datacenter = datacenter;
	}

}
