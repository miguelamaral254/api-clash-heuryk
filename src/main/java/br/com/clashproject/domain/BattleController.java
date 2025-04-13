package br.com.clashproject.domain;


import br.com.clashproject.domain.dtos.*;
import br.com.clashproject.domain.entities.Battle;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final BattleMapper battleMapper;

    @GetMapping
    public ResponseEntity<Page<BattleResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp,desc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(battleService.getAllBattles(pageable));
    }
    @PostMapping
    public ResponseEntity<Void> createBattle(
            @Valid @RequestBody BattleCreateDTO battleCreateDTO) {

        Battle savedBattle = battleService.createBattle(battleCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBattle.getId())
                .toUri();

        return ResponseEntity.created(location).build();
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
    public ResponseEntity<Page<DeckWinRateDTO>> getDecksByWinRate(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") double minWinRate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "winPercentage,desc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(battleService.getDeckWinRates(start, end, minWinRate, pageable));
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