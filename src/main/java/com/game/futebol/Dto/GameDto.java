package com.game.futebol.Dto;

import java.time.LocalDateTime;

public class GameDto {

    private Long id;
    private String stadiumName;
    private String nameHomeClub;
    private String nameVisitingClub;
    private int homeGoals;
    private int visitingGoals;
    private LocalDateTime dateTimeGame;

    public GameDto() {
    }

    public GameDto(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getNameHomeClub() {
        return nameHomeClub;
    }

    public void setNameHomeClub(String nameHomeClub) {
        this.nameHomeClub = nameHomeClub;
    }

    public String getNameVisitingClub() {
        return nameVisitingClub;
    }

    public void setNameVisitingClub(String nameVisitingClub) {
        this.nameVisitingClub = nameVisitingClub;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getVisitingGoals() {
        return visitingGoals;
    }

    public void setVisitingGoals(int visitingGoals) {
        this.visitingGoals = visitingGoals;
    }

    public LocalDateTime getDateTimeGame() {
        return dateTimeGame;
    }

    public void setDateTimeGame(LocalDateTime dateTimeGame) {
        this.dateTimeGame = dateTimeGame;
    }
}
