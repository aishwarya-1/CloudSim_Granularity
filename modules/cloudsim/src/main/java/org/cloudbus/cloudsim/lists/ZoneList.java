/* This file is to add zone level granularity to the data center. */

package org.cloudbus.cloudsim.lists;

import java.util.List;

import org.cloudbus.cloudsim.Zone;

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
			numberOfHosts += zone.getHostList().size();
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

//	public static <T extends Zone> int getNumberOfFreeHosts(List<T> zoneList) {
//		int numberOfFreeHosts = 0;
//		for (T zone : zoneList) {
//			numberOfFreeHosts += HostList.getNumberOfFreeHosts(zone.getHostList());
//		}
//		return numberOfFreeHosts;
//	}
//
//	/**
//	 * Gets the total number of <tt>BUSY</tt> Hosts for all Zones.
//	 * 
//	 * @param <T> the generic type
//	 * @param zoneList the list of existing zones
//	 * @return total number of busy hosts
//	 * @pre $none
//	 * @post $result >= 0
//	 */
//	public static <T extends Zone> int getNumberOfBusyHosts(List<T> zoneList) {
//		int numberOfBusyHosts = 0;
//		for (T zone : zoneList) {
//			numberOfBusyHosts += HostList.getNumberOfBusyHosts(zone.getHostList());
//		}
//		return numberOfBusyHosts;
//	}
//
//	/**
//	 * Gets the first zone with free hosts.
//	 * 
//	 * @param <T> the generic type
//	 * @param zoneList the list of existing zones.
//	 * @return a zone object or <tt>null</tt> if not found
//	 * @pre $none
//	 * @post $none
//	 */
//	public static <T extends Zone> T getZoneWithFreeHost(List<T> zoneList) {
//		return getZoneWithFreeHost(zoneList, 1);
//	}
//
//	/**
//	 * Gets the first Zone with a specified number of free Hosts.
//	 * 
//	 * @param <T> the generic type
//	 * @param zoneList the list of existing zones
//	 * @param hostNumber the host number
//	 * @return a Zone object or <tt>null</tt> if not found
//	 * @pre $none
//	 * @post $none
//	 */
//	public static <T extends Zone> T getZoneWithFreeHost(List<T> zoneList, int hostNumber) {
//		for (T zone : zoneList) {
//			if (ZoneList.getNumberOfFreeHosts(zone.getHostList()) >= hostNumber) {
//				return zone;
//			}
//		}
//		return null;
//	}

	/**
	 * Sets the status of a particular Host on a given Zone.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones.
	 * @param status the Host status, either <tt>Host.FREE</tt> or <tt>Host.BUSY</tt>
	 * @param zoneId the zone id
	 * @param hostId the id of the host to set the status
	 * @return <tt>true</tt> if the Host status has changed, <tt>false</tt> otherwise (zone id or
	 *         Host id might not be exist)
	 * @pre zoneId >= 0
	 * @pre hostId >= 0
	 * @post $none
	 */
//	public static <T extends Zone> boolean setHostStatus(List<T> zoneList, int status, int zoneId, int hostId) {
//		T zone = getById(zoneList, zoneId);
//		if (zone == null) {
//			return false;
//		}
//		return zone.setHostStatus(hostId, status);
//	}
}
