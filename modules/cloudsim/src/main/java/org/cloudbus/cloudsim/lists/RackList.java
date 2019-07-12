/* This file is to add zone level granularity to the data center. */

package org.cloudbus.cloudsim.lists;

import java.util.List;

import org.cloudbus.cloudsim.Rack;

public class RackList {

	/**
	 * Gets a {@link Rack} with a given id.
	 * 
	 * @param <T> the generic type
	 * @param RackList the list of existing racks
	 * @param id the rack ID
	 * @return a rack with the given ID or $null if not found
         * 
	 * @pre id >= 0
	 * @post $none
	 */
	public static <T extends Rack> T getById(List<T> rackList, int id) {
		for (T rack : rackList) {
			if (rack.getId() == id) {
				return rack;
			}
		}
		return null;
	}

	/**
	 * Gets the total number of Hosts for all Racks.
	 * 
	 * @param <T> the generic type
	 * @param rackList the list of existing racks
	 * @return total number of hosts for all racks
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Rack> int getNumberOfHosts(List<T> rackList) {
		int numberOfHosts = 0;
		for (T rack : rackList) {
			numberOfHosts += rack.getHostList().size();
		}
		return numberOfHosts;
	}
	
	/**
	 * Gets the total number of Racks.
	 * 
	 * @param <T> the generic type
	 * @param rackList the list of existing racks
	 * @return total number of racks for an aisle(parent).
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Rack> int size(List<T> rackList) {
		return rackList.size();
	}

}
