package com.game.futebol.Service;

import com.game.futebol.Dto.GameDto;
import com.game.futebol.Enum.GameResults;
import com.game.futebol.Model.Game;
import com.game.futebol.Repository.GameRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.game.futebol.Enum.GameResults.*;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public GameDto save(GameDto gameDto) {
        Game game = dtoToGame(gameDto);
        applyValidations(gameDto, game);
        Game savedGame = gameRepository.save(game);
        GameDto gameResult = gameToDto(savedGame);
        return gameResult;
    }

    public GameDto get(Long id) {
        Optional<Game> resultRepository = gameRepository.findById(id);
        if (resultRepository.isPresent()) {
            Game resultGame = resultRepository.get();
            GameDto resultGameDto = gameToDto(resultGame);
            return resultGameDto;
        } else {
            return null;
        }
    }

    public GameDto update(Long id, GameDto gameDto) {
        Optional<Game> optionalGame = gameRepository.findById(id);

        if (optionalGame.isPresent()) {
            Game game = dtoToGame(gameDto);
            applyValidations(gameDto, game);
            game.setId(id);
            Game savedGame = gameRepository.save(game);
            GameDto resultGameDto = gameToDto(savedGame);
            return resultGameDto;
        } else {
            return null;
        }
    }

    public void delete(Long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            gameRepository.deleteById(id);
        }
    }

    public List<GameDto> getManyGoals() {
        List<GameDto> result = new ArrayList<>();
        List<GameResults> tiposGoleadas = new ArrayList<>();
        tiposGoleadas.add(GOLEADA_MANDANTE);
        tiposGoleadas.add(GOLEADA_VISITANTE);
        List<Game> databaseResults = gameRepository.findAllByGameResultsIn(tiposGoleadas);
        for (Game game : databaseResults) {
            GameDto gameDto = gameToDto(game);
            result.add(gameDto);
        }
        return result;
    }

    public List<GameDto> getZeroToZero() {
        List<GameDto> result = new ArrayList<>();
        List<GameResults> tipoZeroAZero = new ArrayList<>();
        tipoZeroAZero.add(ZERO_ZERO);
        List<Game> databaseResults = gameRepository.findAllByGameResultsIn(tipoZeroAZero);
        for (Game game : databaseResults) {
            GameDto gameDto = gameToDto(game);
            result.add(gameDto);
        }
        return result;
    }

    public List<GameDto> getByStadium(String nomeDoEstadio) {
        List<GameDto> result = new ArrayList<>();
        List<Game> resultRepository = gameRepository.findAllByStadiumName(nomeDoEstadio);
        for (Game game : resultRepository) {
            GameDto gameDto = gameToDto(game);
            result.add(gameDto);
        }
        return result;
    }

    public List<GameDto> getByClubAndType(String nomeDoClube, String tipo) {
        List<GameDto> result = new ArrayList<>();
        List<Game> resultRepository;
        if (tipo.equals("mandante")) {
            resultRepository = gameRepository.findAllByNameHomeClub(nomeDoClube);
        } else if (tipo.equals("visitante")) {
            resultRepository = gameRepository.findAllByNameVisitingClub(nomeDoClube);
        } else {
            return result;
        }
        for (Game game : resultRepository) {
            GameDto gameDto = gameToDto(game);
            result.add(gameDto);
        }
        return result;
    }

    private Game dtoToGame(GameDto gameDto) {
        Game game = new Game();
        game.setNameHomeClub(gameDto.getNameHomeClub());
        game.setNameVisitingClub(gameDto.getNameVisitingClub());
        game.setStadiumName(gameDto.getStadiumName());
        game.setHomeGoals(gameDto.getHomeGoals());
        game.setVisitingGoals(gameDto.getVisitingGoals());
        game.setDateTimeGame(gameDto.getDateTimeGame());
        return game;
    }

    private static GameDto gameToDto(Game savedGame) {
        GameDto gameDto = new GameDto();
        gameDto.setId(savedGame.getId());
        gameDto.setNameHomeClub(savedGame.getNameHomeClub());
        gameDto.setNameVisitingClub(savedGame.getNameVisitingClub());
        gameDto.setHomeGoals(savedGame.getHomeGoals());
        gameDto.setVisitingGoals(savedGame.getVisitingGoals());
        gameDto.setStadiumName(savedGame.getStadiumName());
        gameDto.setDateTimeGame(savedGame.getDateTimeGame());
        return gameDto;
    }

    private void applyValidations(GameDto gameDto, Game game) {
        validateRequiredFilds(gameDto);

        LocalDateTime dateTimeGame = game.getDateTimeGame();
        LocalTime timeGame = dateTimeGame.toLocalTime();

        validateDeadline(timeGame);
        validateResults(gameDto, game);
        validateDateTime(gameDto);
        validateStadiumDate(gameDto);
        validateClubHomeDate(gameDto);
        validateClubVisitingDate(gameDto);
    }

    private static void validateRequiredFilds(GameDto gameDto) {
        if (Strings.isBlank(gameDto.getNameHomeClub()) || gameDto.getNameVisitingClub().isEmpty() || gameDto.getStadiumName().isEmpty() || gameDto.getDateTimeGame() == null) {
            throw new IllegalArgumentException("There are mandatory fields not filled in: name of the clubs, stadium and date and time of the game.");
        }
    }

    private void validateDeadline(LocalTime timeGame) {
        if (timeGame.isBefore(LocalTime.of(8, 0)) || timeGame.isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("A game outside of standard hours between 8am and 10pm is not permitted.");
        }
    }

    private static void validateResults(GameDto gameDto, Game game) {
        if (gameDto.getHomeGoals() > gameDto.getVisitingGoals() && gameDto.getVisitingGoals() > 0) {
            int dif = gameDto.getHomeGoals() - gameDto.getVisitingGoals();
            if (dif >= 3) {
                game.setGameResults(GOLEADA_MANDANTE);
            } else {
                game.setGameResults(VITORIA_MANDANTE);
            }
        } else if (gameDto.getHomeGoals() < gameDto.getVisitingGoals() && gameDto.getHomeGoals() > 0) {
            int dif = gameDto.getVisitingGoals() - gameDto.getHomeGoals();
            if (dif >= 3) {
                game.setGameResults(GOLEADA_VISITANTE);
            } else {
                game.setGameResults(VITORIA_VISITANTE);
            }
        } else {
            if (gameDto.getHomeGoals() == 0 && gameDto.getVisitingGoals() == 0) {
                game.setGameResults(ZERO_ZERO);
            } else if (gameDto.getHomeGoals() > 0 && gameDto.getHomeGoals() == gameDto.getVisitingGoals()) {
                game.setGameResults(EMPATE);
            } else if (gameDto.getHomeGoals() < 0 || gameDto.getVisitingGoals() < 0) {
                throw new IllegalArgumentException("The number of goals cannot be negative.");
            }
        }
    }

    private static void validateDateTime(GameDto gameDto) {
        if (gameDto.getDateTimeGame().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A game with a future date and time is not permitted.");
        }
    }

    public void validateStadiumDate(GameDto gameDto) {
        LocalDateTime dateTimeGame = gameDto.getDateTimeGame();
        LocalTime timeGame = dateTimeGame.toLocalTime();
        List<Game> databaseResult = gameRepository.findAllByStadiumNameContainingIgnoreCaseAndDateTimeGame(gameDto.getStadiumName(), gameDto.getDateTimeGame().toLocalDate().atTime(timeGame));
        if (!databaseResult.isEmpty()) {
            throw new IllegalArgumentException("No more than one game is allowed in the same stadium on the same day.");
        }
    }

    public void validateClubHomeDate(GameDto gameDto) {
        LocalDateTime dateHourLimit = gameDto.getDateTimeGame().minusHours(48);
        if (gameRepository.existsByNameHomeClubAndDateTimeGameAfter(gameDto.getNameHomeClub(), dateHourLimit)) {
            throw new IllegalArgumentException("No more than one game for the same team is allowed in less than 48 hours.");
        }
    }

    public void validateClubVisitingDate(GameDto gameDto) {
        LocalDateTime dateHourLimit = gameDto.getDateTimeGame().minusHours(48);
        if (gameRepository.existsByNameVisitingClubAndDateTimeGameAfter(gameDto.getNameVisitingClub(), dateHourLimit)) {
            throw new IllegalArgumentException("No more than one game for the same team is allowed in less than 48 hours.");
        }
    }
}
