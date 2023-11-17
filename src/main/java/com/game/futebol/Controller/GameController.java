package com.game.futebol.Controller;

import com.game.futebol.Dto.GameDto;
import com.game.futebol.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/partida")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping()
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto) {
        GameDto result = gameService.save(gameDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    GameDto getGameById(@PathVariable Long id) {
        GameDto gameDto = gameService.get(id);
        return gameDto;
    }

    @PutMapping("/{id}")
    public @ResponseBody
    GameDto updateGame(@PathVariable Long id, @RequestBody GameDto gameUpdated) {
        GameDto gameDto = gameService.update(id, gameUpdated);
        return gameDto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGame(@PathVariable Long id) {
        gameService.delete(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/busca/goleada")
    public @ResponseBody
    List<GameDto> getManyGoals() {
        return gameService.getManyGoals();
    }

    @GetMapping("/busca/zeroazero")
    public @ResponseBody
    List<GameDto> getZeroToZero() {
        return gameService.getZeroToZero();
    }

    @GetMapping("/busca/estadio/{nameStadium}")
    public @ResponseBody
    List<GameDto> getByStadium(@PathVariable String nameStadium) {
        return gameService.getByStadium(nameStadium);
    }

    @GetMapping("/busca/clube/{nomeClub}/{type}")
    public @ResponseBody
    List<GameDto> getByClubAndType(@PathVariable String nomeClub, @PathVariable String type) {
        return gameService.getByClubAndType(nomeClub, type);
    }
}
