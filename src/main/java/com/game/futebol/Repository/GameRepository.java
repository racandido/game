package com.game.futebol.Repository;

import com.game.futebol.Enum.GameResults;
import com.game.futebol.Model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {


    List<Game> findAllByGameResultsIn(List<GameResults> gameResults);

    List<Game> findAllByStadiumName(String stadiumName);

    List<Game> findAllByNameVisitingClub(String nameHomeVisiting);

    List<Game> findAllByNameHomeClub(String nameHomeClub);

    List<Game> findAllByStadiumNameContainingIgnoreCaseAndDateTimeGame(String stadiumName, LocalDateTime localDateTime);

    boolean existsByNameHomeClubAndDateTimeGameAfter(String nameHomeClub, LocalDateTime dataHoraLimite);

    boolean existsByNameVisitingClubAndDateTimeGameAfter(String nameVisitingClub, LocalDateTime dataHoraLimite);
}
