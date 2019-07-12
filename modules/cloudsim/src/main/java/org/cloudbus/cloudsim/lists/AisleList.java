/* This file is to add zone level granularity to the data center. */

package org.cloudbus.cloudsim.lists;

import java.util.List;

import org.cloudbus.cloudsim.Aisle;
import org.cloudbus.cloudsim.Host;

public class AisleList {

	/**
	 * Gets a {@link Aisle} with a given id.
	 * 
	 * @param <T> the generic type
	 * @param AisleList the list of existing aisle
	 * @param id the aisle ID
	 * @return a aisle with the given ID or $null if not found
         * 
	 * @pre id >= 0
	 * @post $none
	 */
	public static <T extends Aisle> T getById(List<T> aisleList, int id) {
		for (T aisle : aisleList) {
			if (aisle.getId() == id) {
				return aisle;
			}
		}
		return null;
	}

	/**
	 * Gets the total number of Racks for all Aisles.
	 * 
	 * @param <T> the generic type
	 * @param aisleList the list of existing aisles
	 * @return total number of racks for all Aisles
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Aisle> int getNumberOfRacks(List<T> aisleList) {
		int numberOfRacks = 0;
		for (T aisle : aisleList) {
			numberOfRacks += aisle.getRackList().size();
		}
		return numberOfRacks;
	}
	
	/**
	 * Gets the total number of Aisles.
	 * 
	 * @param <T> the generic type
	 * @param aisleList the list of existing aisles
	 * @return total number of aisles for a zone(parent).
	 * @pre $none
	 * @post $result >= 0
	 */
	public static <T extends Aisle> int size(List<T> aisleList) {
		return aisleList.size();
	}


}
