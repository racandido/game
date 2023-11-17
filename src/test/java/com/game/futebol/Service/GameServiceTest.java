package com.game.futebol.Service;

import com.game.futebol.Dto.GameDto;
import com.game.futebol.Model.Game;
import com.game.futebol.Repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    @DisplayName("Test whether the application is saving correctly.")
    void saveTest() {
    //Given
        GameDto gameDto = new GameDto();
        gameDto.setStadiumName("Maracana");
        gameDto.setNameHomeClub("São Paulo");
        gameDto.setNameVisitingClub("Palmeiras");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(1);
        gameDto.setDateTimeGame(LocalDateTime.parse("2023-10-10T09:02:00"));

        Game game = new Game();
        game.setId(1L);
        game.setStadiumName(gameDto.getStadiumName());
        game.setNameHomeClub(gameDto.getNameHomeClub());
        game.setNameVisitingClub(gameDto.getNameVisitingClub());
        game.setHomeGoals(gameDto.getHomeGoals());
        game.setVisitingGoals(gameDto.getVisitingGoals());
        game.setDateTimeGame(LocalDateTime.parse("2023-10-10T09:02:00"));

        //When
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        GameDto savedGameDto = gameService.save(gameDto);

        //Then
        assertNotNull(savedGameDto);
    }

    @Test
    @DisplayName("Tests whether mandatory fields have been filled in.")
    void testValidateRequiredFilds() {
        //Given
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setStadiumName("");
        gameDto.setNameHomeClub("");
        gameDto.setNameVisitingClub("");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(1);
        gameDto.setDateTimeGame(null);

        //When
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () ->
                gameService.save(gameDto));

        //Then
        assertEquals( "There are mandatory fields not filled in: name of the clubs, stadium and date and time of the game.", thrownException.getMessage());
    }

    @Test
    @DisplayName("Tests whether the game time is within the expected standard between 8 am and 10 pm.")
    void testValidateDeadline() {
        //Given
        GameDto gameDto = new GameDto();
        gameDto.setStadiumName("Maracana");
        gameDto.setNameHomeClub("São Paulo");
        gameDto.setNameVisitingClub("Palmeiras");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(1);
        gameDto.setDateTimeGame(LocalDateTime.parse("2023-10-10T07:02:00"));

        //When
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> gameService.save(gameDto));

        //Then
        assertEquals( "A game outside of standard hours between 8am and 10pm is not permitted.", thrownException.getMessage());
    }

    @Test
    @DisplayName("Test whether the application prevents the number of negative goals.")
    void testValidateResults() {
        //Given
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setStadiumName("Maracana");
        gameDto.setNameHomeClub("São Paulo");
        gameDto.setNameVisitingClub("Palmeiras");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(-1);
        gameDto.setDateTimeGame(LocalDateTime.parse("2023-10-10T10:02:00"));

        //When
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () ->
                gameService.save(gameDto));

        //Then
        assertEquals( "The number of goals cannot be negative.", thrownException.getMessage());
    }

    @Test
    @DisplayName("Tests whether the application prevents games in the future.")
    void testValidateDateTime() {
        //Given
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setStadiumName("Maracana");
        gameDto.setNameHomeClub("São Paulo");
        gameDto.setNameVisitingClub("Palmeiras");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(1);
        gameDto.setDateTimeGame(LocalDateTime.now().plusHours(1));

        //When
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () ->
                gameService.save(gameDto));

        //Then
        assertEquals( "A game with a future date and time is not permitted.", thrownException.getMessage());
    }

    @Test
    @DisplayName("Tests whether the application prevents more than one game at the same stadium and on the same day.")
    void testValidateStadioumDate() {
        //Given: a valid dto
        GameDto gameDto = new GameDto();
        gameDto.setStadiumName("Maracana");
        gameDto.setNameHomeClub("São Paulo");
        gameDto.setNameVisitingClub("Palmeiras");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(1);
        gameDto.setDateTimeGame(LocalDateTime.parse("2023-10-10T10:02:00"));

        //And: a valid response from repo
        List<Game> mockedRepoResult = new ArrayList<>();
        mockedRepoResult.add(new Game());

        //When
        when(gameRepository.findAllByStadiumNameContainingIgnoreCaseAndDateTimeGame(eq("Maracana"), any()))
                .thenReturn(mockedRepoResult);

        //Then
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () ->
            gameService.save(gameDto));
        assertEquals( "No more than one game is allowed in the same stadium on the same day.", thrownException.getMessage());
    }

    @Test
    @DisplayName("Tests whether the application prevents more than one home team game in less than 48 hours.")
    void testValidateClubDate() {
        //Given
        GameDto gameDto = new GameDto();
        gameDto.setStadiumName("Maracana");
        gameDto.setNameHomeClub("São Paulo");
        gameDto.setNameVisitingClub("Palmeiras");
        gameDto.setHomeGoals(4);
        gameDto.setVisitingGoals(1);
        gameDto.setDateTimeGame(LocalDateTime.parse("2023-10-10T10:02:00"));

        //When
        when(gameRepository.existsByNameHomeClubAndDateTimeGameAfter(any(), any()))
                .thenReturn(true);

        //Then
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () ->
            gameService.save(gameDto));
        assertEquals( "No more than one game for the same team is allowed in less than 48 hours.", thrownException.getMessage());
    }
}