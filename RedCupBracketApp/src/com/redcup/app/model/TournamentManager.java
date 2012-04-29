package com.redcup.app.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to maintain the list of tournaments.
 * 
 * @author Jackson Lamp
 */
public final class TournamentManager {
	private static final Map<String, Tournament> tournaments = new HashMap<String, Tournament>();

	private TournamentManager() {
	}

	/**
	 * Adds a {@code Tournament} to the manager.
	 * 
	 * @param t
	 *            the {@code Tournament} to add.
	 * @return the previous {@code Tournament} that {@code t} replaced, or
	 *         {@code null} if this did not occur.
	 */
	public static Tournament addTournament(Tournament t) {
		return tournaments.put(t.getName(), t);
	}

	/**
	 * Removes the {@code Tournament} with the given ID from the collection of
	 * tournaments.
	 * 
	 * @param id
	 *            the ID of the {@code Tournament} to remove.
	 */
	public static void deleteTournament(String id) {
		tournaments.remove(id);
	}

	/**
	 * Returns the {@code Tournament} with the given ID, or {@code null} if no
	 * such tournament exists.
	 * 
	 * @param id
	 *            the ID of the {@code Tournament} to look for.
	 * @return the {@code Tournament} with the given ID or {@code null} if none
	 *         exists.
	 */
	public static Tournament getTournament(String id) {
		return tournaments.get(id);
	}

	/**
	 * Returns {@code true} if and only if a {@code Tournament} with the given
	 * ID exists.
	 * 
	 * @param id
	 *            the ID of the {@code Tournament} to search for.
	 * @return {@code true} if the {@code Tournament} could be found,
	 *         {@code false} otherwise.
	 */
	public static boolean containsTournament(String id) {
		return tournaments.containsKey(id);
	}
}
