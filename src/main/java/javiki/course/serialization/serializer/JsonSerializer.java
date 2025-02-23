package javiki.course.serialization.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonSerializer extends AbstractStateSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer() {
        super(".json", new JsonMapper());
    }
}
