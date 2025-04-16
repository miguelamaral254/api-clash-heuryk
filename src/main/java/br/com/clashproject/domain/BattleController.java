package br.com.clashproject.domain;


import br.com.clashproject.core.entities.*;
import br.com.clashproject.domain.dtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Page<Battle> battles = battleService.getAllBattles(pageable);
        Page<BattleResponseDTO> battleResponseDTOs = battleMapper.toDto(battles);

        return ResponseEntity.ok(battleResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<Void> createBattle(
            @Valid @RequestBody BattleCreateDTO battleCreateDTO) {

        Battle battle = battleMapper.toEntityFromCreateDTO(battleCreateDTO);
        Battle savedBattle = battleService.createBattle(battle);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBattle.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<BattleStatsDTO> getBattleStatsByCard(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String cardName) {
        BattleStats stats = battleService.calculateWinLossPercentage(start, end, cardName);

        BattleStatsDTO statsDTO = battleMapper.toBattleStatsDTO(stats);

        return ResponseEntity.ok(statsDTO);
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

        Page<DeckWinRate> deckWinRatePage = battleService.getDeckWinRates(start, end, minWinRate, pageable);

        Page<DeckWinRateDTO> deckWinRateDTOs = deckWinRatePage.map(deckWinRate -> battleMapper.toDeckWinRateDTO(deckWinRate));

        return ResponseEntity.ok(deckWinRateDTOs);
    }

    @GetMapping("/decks/winrates/arena")
    public ResponseEntity<Page<DeckWinRateDTO>> getDecksByWinRatePerTrophiesAndArena(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") double minWinRate,
            @RequestParam(defaultValue = "ARENA_1") String arena,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "winPercentage,desc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<DeckWinRate> deckWinRatePage = battleService.getDeckWinRatesPerArena(start, end, minWinRate, arena, pageable);

        Page<DeckWinRateDTO> deckWinRateDTOs = deckWinRatePage.map(deckWinRate -> battleMapper.toDeckWinRateDTO(deckWinRate));

        return ResponseEntity.ok(deckWinRateDTOs);
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
    public ResponseEntity<Page<ComboStatsDTO>> getComboStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "8") int deckSize,
            @RequestParam(defaultValue = "4") int comboSize,
            @RequestParam(defaultValue = "50") double minWinPercentage,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "winPercentage,desc") String[] sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ComboStats> comboStatsPage = battleService.getComboStats(start, end, deckSize, comboSize, minWinPercentage, pageable);
        Page<ComboStatsDTO> comboStatsDTOs = battleMapper.toComboStatsDTOPage(comboStatsPage);

        return ResponseEntity.ok(comboStatsDTOs);
    }

    @GetMapping("/decks/winrates/lowlevel")
    public ResponseEntity<Page<DeckWinRateLowEloDTO>> getBetterCards(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam int arena, // Parâmetro para selecionar a arena de 1 a 13
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "winRate,desc") String[] sort) {

        // Definir a páginação com base nos parâmetros fornecidos
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        // Chama o método do serviço passando a arena para calcular minTrophies e maxTrophies
        Page<DeckWinRateLowElo> deckWinRateLowEloPage = battleService.getDeckWinRatesPerLowLevel(start, end, Arena.values()[arena - 1], pageable);

        // Mapeia para DeckWinRateLowEloDTO
        Page<DeckWinRateLowEloDTO> deckWinRateLowEloDTOs = battleMapper.toDeckWinRateLowEloDTOPage(deckWinRateLowEloPage);

        // Retorna a resposta com os dados mapeados
        return ResponseEntity.ok(deckWinRateLowEloDTOs);
    }
}