package com.game.futebol.Model;

import com.game.futebol.Enum.GameResults;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stadiumName;
    private String nameHomeClub;
    private String nameVisitingClub;
    private int homeGoals;
    private int visitingGoals;
    private GameResults gameResults;
    private LocalDateTime dateTimeGame;

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameHomeClub() {
        return nameHomeClub;
    }

    public void setNameHomeClub(String nomeDoClube) {
        this.nameHomeClub = nomeDoClube;
    }

    public String getNameVisitingClub() {
        return nameVisitingClub;
    }

    public void setNameVisitingClub(String nomeDoClubeVisitante) {
        this.nameVisitingClub = nomeDoClubeVisitante;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int resultadoDaPartida) {
        this.homeGoals = resultadoDaPartida;
    }

    public int getVisitingGoals() {
        return visitingGoals;
    }

    public void setVisitingGoals(int golsVisitante) {
        this.visitingGoals = golsVisitante;
    }

    public GameResults getGameResults() {
        return gameResults;
    }

    public void setGameResults(GameResults resultadoPartida) {
        this.gameResults = resultadoPartida;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String nomeDoEstadio) {
        this.stadiumName = nomeDoEstadio;
    }

    public LocalDateTime getDateTimeGame() {
        return dateTimeGame;
    }

    public void setDateTimeGame(LocalDateTime dataHoraPartida) {
        this.dateTimeGame = dataHoraPartida;
    }
}
