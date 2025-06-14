package dialogue.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dialogue.model.DialogueModel;
import java.io.InputStream;
import java.util.List;

public class DialogueLoader {
    public static List<DialogueModel> loadFromJson(String resourceName) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = DialogueLoader.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) throw new RuntimeException("Không tìm thấy file: " + resourceName);
        return mapper.readValue(is, new TypeReference<List<DialogueModel>>() {});
    }
}