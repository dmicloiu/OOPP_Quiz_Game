package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * This class is the table housing activities for the game to extract and
 * turn into questions.
 */
public interface ActivityRepo extends JpaRepository<Activity, Long> {

    /**
     * This method is used to get activities with the most similar energy to the given value.
     * Exact matches are allowed, which is why this method ought to be used when generating
     * INCORRECT answers.
     *
     * @param energy the energy which we are searching for by comparing it to the energy
     *               value of all activities in the database
     *
     * @return the activity found to have the most matching energy consumption
     */
    @Query(value = "SELECT * FROM Activity a" +
        " ORDER BY ABS(:energy - a.consumption_in_wh) LIMIT 1",
        nativeQuery = true)
    Activity getByEnergy(@Param("energy") long energy);

    /**
     * This method serves to find activities with the most similar energy consumption
     * to the given value, but excludes identical answers. As such, this is to be used
     * by the description generator when creating an AsMuchAsQuestion, so that the correct
     * answer has a similar energy value, but it is not identical (so the database doesn't
     * accidentally return the activity itself, which would make no sense)
     *
     * @param energy the energy which we are searching for by comparing it to the energy
     *               value of all activities in the database
     *
     * @return the activity found to have the most matching energy consumption
     *         (excluding identical consumptions)
     */
    @Query(value =
            "SELECT * FROM Activity a " +
            "WHERE ABS(:energy - a.consumption_in_wh) > 0 " +
            "ORDER BY ABS(:energy - a.consumption_in_wh) LIMIT 1",
        nativeQuery = true)
    Activity getSimilar(@Param("energy") long energy);

}
