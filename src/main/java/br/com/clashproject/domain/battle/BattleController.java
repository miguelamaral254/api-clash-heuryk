package br.com.clashproject.domain.battle;

// package: com.clashroyale.controller

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;

    @GetMapping
    public ResponseEntity<List<Battle>> getAll() {
        return ResponseEntity.ok(battleService.getAllBattles());
    }

    @PostMapping
    public ResponseEntity<Battle> save(@RequestBody Battle battle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(battleService.saveBattle(battle));
    }

    @GetMapping("/stats")
    public ResponseEntity<BattleStats> getBattleStatsByCard(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String cardName) {
        BattleStats stats = battleService.calculateWinLossPercentage(start, end, cardName);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/decks/winrates")
    public ResponseEntity<List<DeckWinRateDTO>> getDecksByWinRate(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") double minWinRate) {

        List<DeckWinRateDTO> stats = battleService.getDeckWinRates(start, end, minWinRate);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/defeats")
    public ResponseEntity<Long> getDefeatsByCardCombo(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> cardCombo) {

        long defeats = battleService.calculateDefeatsByCardCombo(start, end, cardCombo);
        return ResponseEntity.ok(defeats);
    }

    @GetMapping("/victories")
    public ResponseEntity<Long> getVictoriesWithConditions(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> cardCombo,
            @RequestParam int trophyPercentage) {

        long victories = battleService.calculateVictoriesWithConditions(start, end, cardCombo, trophyPercentage);
        return ResponseEntity.ok(victories);
    }
    @GetMapping("/combo-stats")
    public ResponseEntity<List<ComboStats>> getComboStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "8") int deckSize,
            @RequestParam(defaultValue = "4") int comboSize,
            @RequestParam(defaultValue = "50") double minWinPercentage) {

        List<ComboStats> stats = battleService.getComboStats(
                start,
                end,
                deckSize,
                comboSize,
                minWinPercentage
        );
        return ResponseEntity.ok(stats);
    }

}