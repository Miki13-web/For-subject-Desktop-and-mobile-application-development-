package source.rest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import source.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StableController {

    private final StableManager manager;

    public StableController(StableManager manager) {
        this.manager = manager;
    }


    // ENDPOINTY
    // GET /api/stable - zwraca wszystkie stadniny
    @GetMapping("/stable")
    public List<Stable> getAllStables() {
        return manager.getAll();
    }

    // POST /api/stable - dodaje nową stadninę
    @PostMapping("/stable")
    public ResponseEntity<String> addStable(@RequestParam String name, @RequestParam int capacity) {
        try {
            manager.addStable(name, capacity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Stable created: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE /api/stable/:name - usuwa stadnine
    @DeleteMapping("/stable/{name}")
    public ResponseEntity<String> removeStable(@PathVariable String name) {
        try {
            manager.removeStable(name);
            return ResponseEntity.ok("Stable removed: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /api/stable/:id - zwraca konie w stadninie
    @GetMapping("/stable/{id}")
    public ResponseEntity<?> getHorsesInStable(@PathVariable Long id) {
        try {
            List<Horse> horses = manager.getHorsesFromStable(id);
            return ResponseEntity.ok(horses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stable not found");
        }
    }

    // GET /api/stable/:name/fill - zwraca zapełnienie
    @GetMapping("/stable/{name}/fill")
    public ResponseEntity<String> getStableLoad(@PathVariable String name) {
        Stable s = manager.getStable(name);
        if (s == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stable not found");

        double load = s.getLoad() * 100; // procenty
        return ResponseEntity.ok(String.format("Occupancy: %.2f%%", load));
    }

    // POST /api/horse - dodaje konia
    @PostMapping("/horse")
    public ResponseEntity<String> addHorse(@RequestParam String stableName, @RequestBody Horse horse) {
        try {
            manager.addHorseToStable(stableName, horse);
            return ResponseEntity.status(HttpStatus.CREATED).body("Horse added to " + stableName);
        } catch (StableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE /api/horse/:id - usuwa konia
    @DeleteMapping("/horse/{id}")
    public ResponseEntity<String> deleteHorse(@PathVariable Long id) {
        try {
            manager.removeHorse(id);
            return ResponseEntity.ok("Horse removed");
        } catch (StableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // POST /api/horse/rating - dodaje ocenę
    @PostMapping("/horse/rating")
    public ResponseEntity<String> addRating(@RequestParam Long horseId, @RequestParam int score, @RequestParam(required = false) String desc) {
        try {
            manager.addRatingToHorse(horseId, score, desc == null ? "" : desc);
            return ResponseEntity.ok("Rating added");
        } catch (StableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /api/horse/rating/:id - zwraca średnią ocenę konia
    @GetMapping("/horse/rating/{id}")
    public ResponseEntity<?> getHorseAvgRating(@PathVariable Long id) {
        for (Stable s : manager.getAll()) {
            for (Horse h : manager.getHorsesFromStable(s.getId())) {
                if (h.getId().equals(id)) {
                    return ResponseEntity.ok(h.getAverageRating());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Horse not found");
    }

    // GET /api/stable/:id/csv - zwraca plik CSV
    @GetMapping("/stable/{id}/csv")
    public ResponseEntity<Resource> downloadCsv(@PathVariable Long id) throws IOException {
        String filename = "temp_export_" + id + ".csv";
        manager.exportStableToCSV(id, filename);

        File file = new File(filename);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    // WŁASNE ENDPOINTY (DODATKOWE)

    // GET /api/stats/breeds - Statystyki ras (Criteria API)
    @GetMapping("/stats/breeds")
    public Map<String, Long> getBreedStats() {
        return manager.getHorseCountByBreed();
    }

    // GET /api/stable/empty - Znajdź puste stadniny
    @GetMapping("/stable/empty")
    public List<Stable> getEmptyStables() {
        return manager.findEmpty();
    }

    //  GET /api/search - Szukaj konia po nazwie w całym systemie
    // Np. /api/search?q=Roach
    @GetMapping("/search")
    public ResponseEntity<?> searchHorseGlobal(@RequestParam String q) {
        List<Stable> stables = manager.getAll();
        return ResponseEntity.ok("Search feature logic here (connect to manager.searchHorsesInStable for each stable)");
    }
}