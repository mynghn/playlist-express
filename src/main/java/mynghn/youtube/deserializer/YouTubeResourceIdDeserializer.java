package mynghn.youtube.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import mynghn.youtube.enums.YouTubeResourceKind;
import mynghn.youtube.message.YouTubeResourceId;
import mynghn.youtube.message.YouTubeVideoId;

public class YouTubeResourceIdDeserializer implements JsonDeserializer<YouTubeResourceId> {

    private static final String SEARCH_RESULT_KIND_JSON_FIELD_NAME = "kind";

    private static YouTubeResourceKind extractKind(JsonElement json) {
        return YouTubeResourceKind.of(json.getAsJsonObject()
                .getAsJsonPrimitive(SEARCH_RESULT_KIND_JSON_FIELD_NAME).getAsString());
    }

    @Override
    public YouTubeResourceId deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        YouTubeResourceKind kind = extractKind(json);
        return switch (kind) {
            case VIDEO -> context.deserialize(json, YouTubeVideoId.class);
            case CHANNEL, PLAYLIST -> throw new UnsupportedOperationException(MessageFormat.format(
                    "{0} type search result id deserialization is currently not supported.", kind));
        };
    }
}
