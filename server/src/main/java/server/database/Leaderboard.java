package server.database;

import commons.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Leaderboard extends JpaRepository<LeaderboardEntry, Long> {

    @Query("select l " +
            "from LeaderboardEntry l " +
            "order by l.score DESC, l.date, l.name")
    List<LeaderboardEntry> findAllByOrderByScoreDescDateAscNameAsc();

    @Query("select l " +
            "from LeaderboardEntry l " +
            "where :name = l.name")
    List<LeaderboardEntry> findByName(@Param("name") String name);

    @Query("select l " +
            "from LeaderboardEntry l " +
            "where :name = l.name and :score = l.score")
    List<LeaderboardEntry> findByNameAndScore(
            @Param("name") String name, 
            @Param("score") int score
    );

}
