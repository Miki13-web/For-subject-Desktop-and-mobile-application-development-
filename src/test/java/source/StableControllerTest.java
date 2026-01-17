package source;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import source.rest.StableController;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StableController.class)
public class StableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StableManager stableManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnAllStables() throws Exception {
        Stable s1 = new Stable("Stajnia A", 10);
        Mockito.when(stableManager.getAll()).thenReturn(Arrays.asList(s1));

        mockMvc.perform(get("/api/stable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stableName").value("Stajnia A"));
    }

    @Test
    public void shouldAddStable() throws Exception {
        mockMvc.perform(post("/api/stable")
                        .param("name", "Nowa")
                        .param("capacity", "20"))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Stable created")));
    }

    @Test
    public void shouldRemoveStable() throws Exception {
        mockMvc.perform(delete("/api/stable/StajniaX"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Stable removed")));
    }

    @Test
    public void shouldReturnHorsesInStable() throws Exception {
        Horse h1 = new Horse("Płotka", "Mustang", HorseType.HotBlood, 5, Gender.Mare, "Brown", 500, 1000, HorseCondition.healthy);
        Mockito.when(stableManager.getHorsesFromStable(Long.valueOf(1L))).thenReturn(Arrays.asList(h1));

        mockMvc.perform(get("/api/stable/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Płotka"));
    }

    @Test
    public void shouldReturnStableLoad() throws Exception {
        Stable s = new Stable("Test", 10);
        s.getHorseList().add(new Horse());
        Mockito.when(stableManager.getStable("Test")).thenReturn(s);

        mockMvc.perform(get("/api/stable/Test/fill"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("%")));
    }

    @Test
    public void shouldAddHorse() throws Exception {
        Horse newHorse = new Horse("Bucefał", "Greek", HorseType.HotBlood, 10, Gender.Stallion, "Black", 600, 5000, HorseCondition.healthy);

        mockMvc.perform(post("/api/horse")
                        .param("stableName", "Hubertus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newHorse)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldDeleteHorse() throws Exception {
        mockMvc.perform(delete("/api/horse/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddRating() throws Exception {
        mockMvc.perform(post("/api/horse/rating")
                        .param("horseId", "1")
                        .param("score", "5")
                        .param("desc", "Great horse"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDownloadCsv() throws Exception {
        Mockito.doNothing().when(stableManager).exportStableToCSV(Long.valueOf(ArgumentMatchers.anyLong()), anyString());

        mockMvc.perform(get("/api/stable/1/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string(org.springframework.http.HttpHeaders.CONTENT_TYPE, "text/csv"));
    }
}