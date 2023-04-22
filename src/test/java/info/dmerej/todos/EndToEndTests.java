package info.dmerej.todos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
public class EndToEndTests {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );


    @Autowired
    private MockMvc mockMvc;

    private static Task parseTask(String json) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var created = objectMapper.readValue(json, Task.class);
        return created;
    }

    @Test
    void create_a_task_then_get_it() throws Exception {
        var json = doPost("/task", "new task").andReturn().getResponse().getContentAsString();
        var created = parseTask(json);
        assertThat(created.isDone()).isFalse();
        var id = created.getId();

        json = doGet("/task/" + id.toString()).andReturn().getResponse().getContentAsString();
        var saved = parseTask(json);
        assertThat(saved).isEqualTo(created);
    }

    @Test
    void create_a_task_then_mark_it_complete() throws Exception {
        var json = doPost("/task", "new task").andReturn().getResponse().getContentAsString();
        var created = parseTask(json);

        var id = created.getId();
        doPost("/task/" + id.toString() + "/complete", "");


        json = doGet("/task/" + id).andReturn().getResponse().getContentAsString();
        var saved = parseTask(json);
        var completedTask = new Task(id, "new task", true);
        assertThat(saved).isEqualTo(completedTask);
    }

    private ResultActions doGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    private ResultActions doPost(String url, String json) throws Exception {
        return mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
            .content(json));
    }

}
