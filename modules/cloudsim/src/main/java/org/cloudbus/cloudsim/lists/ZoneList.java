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
	 * Gets the total number of Aisles for all Hosts.
	 * 
	 * @param <T> the generic type
	 * @param zoneList the list of existing zones
	 * @return total number of aisles for all Zones
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Zone> int getNumberOfHosts(List<T> zoneList) {
		int numberOfAisles = 0;
		for (T zone : zoneList) {
			numberOfAisles += zone.getAisleList().size();
		}
		return numberOfAisles;
	}

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
