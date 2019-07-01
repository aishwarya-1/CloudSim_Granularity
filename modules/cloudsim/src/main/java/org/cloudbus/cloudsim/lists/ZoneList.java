/* This file is to add zone level granularity to the data center. */

package org.cloudbus.cloudsim.lists;

import java.util.List;

import org.cloudbus.cloudsim.Host;

public class ZoneList {

	/**
	 * Gets a {@link Zone} with a given id.
	 * 
	 * @param <T> the generic type
	 * @param ZoneList the list of existing zones
	 * @param id the zone ID
	 * @return a zone with the given ID or $null if not found
         * 
	 * @pre id >= 0
	 * @post $none
	 */
	public static <T extends Zone> T getById(List<T> zoneList, int id) {
		for (T zone : zoneList) {
			if (zone.getId() == id) {
				return zone;
			}
		}
		return null;
	}

	/**
	 * Gets the total number of Hosts for all Zones.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones
	 * @return total number of hosts for all Zones
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Zone> int getNumberOfHosts(List<T> zoneList) {
		int numberOfHosts = 0;
		for (T zone : zoneList) {
			numberOfHosts += zone.getHostsList().size();
		}
		return numberOfHosts;
	}

	/**
	 * Gets the total number of <tt>FREE</tt> (non-busy) Hosts for all Zones.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones
	 * @return total number of free Hosts
	 * @pre $none
	 * @post $result >= 0
	 */

	public static <T extends Zone> int getNumberOfFreeHosts(List<T> zoneList) {
		int numberOfFreeHosts = 0;
		for (T zone : zoneList) {
			numberOfFreeHosts += HostList.getNumberOfFreeHosts(zone.getHostList());
		}
		return numberOfFreeHosts;
	}

	/**
	 * Gets the total number of <tt>BUSY</tt> Hosts for all Zones.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones
	 * @return total number of busy hosts
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Zone> int getNumberOfBusyHosts(List<T> zoneList) {
		int numberOfBusyHosts = 0;
		for (T zone : zoneList) {
			numberOfBusyHosts += HostList.getNumberOfBusyHosts(zone.getHostList());
		}
		return numberOfBusyHosts;
	}

	/**
	 * Gets the first zone with free hosts.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones.
	 * @return a zone object or <tt>null</tt> if not found
	 * @pre $none
	 * @post $none
	 */
	public static <T extends Zone> T getZoneWithFreeHost(List<T> zoneList) {
		return getZoneWithFreeHost(zoneList, 1);
	}

	/**
	 * Gets the first Zone with a specified number of free Hosts.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones
	 * @param hostNumber the host number
	 * @return a Zone object or <tt>null</tt> if not found
	 * @pre $none
	 * @post $none
	 */
	public static <T extends Zone> T getZoneWithFreeHost(List<T> zoneList, int hostNumber) {
		for (T zone : zoneList) {
			if (ZoneList.getNumberOfFreeHosts(zone.getHostsList()) >= hostNumber) {
				return zone;
			}
		}
		return null;
	}

	/**
	 * Sets the status of a particular Host on a given Zone.
	 * 
	 * @param <T> the generic type
	 * @param hostList the list of existing hosts
	 * @param status the PE status, either <tt>Pe.FREE</tt> or <tt>Pe.BUSY</tt>
	 * @param hostId the host id
	 * @param peId the id of the PE to set the status
	 * @return <tt>true</tt> if the PE status has changed, <tt>false</tt> otherwise (host id or
	 *         PE id might not be exist)
	 * @pre hostId >= 0
	 * @pre peId >= 0
	 * @post $none
	 */
	public static <T extends Host> boolean setPeStatus(List<T> hostList, int status, int hostId, int peId) {
		T host = getById(hostList, hostId);
		if (host == null) {
			return false;
		}
		return host.setPeStatus(peId, status);
	}

}
