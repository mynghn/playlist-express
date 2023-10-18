package mynghn.youtube.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import mynghn.youtube.message.search.response.YouTubeSearchResultId;
import mynghn.youtube.message.search.response.YouTubeSearchResultKind;
import mynghn.youtube.message.search.response.YouTubeVideoId;

public class YouTubeSearchResultIdDeserializer implements JsonDeserializer<YouTubeSearchResultId> {

    private static final String SEARCH_RESULT_KIND_JSON_FIELD_NAME = "kind";

    private static YouTubeSearchResultKind extractKind(JsonElement json) {
        return YouTubeSearchResultKind.of(json.getAsJsonObject()
                .getAsJsonPrimitive(SEARCH_RESULT_KIND_JSON_FIELD_NAME).getAsString());
    }

    @Override
    public YouTubeSearchResultId deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        YouTubeSearchResultKind kind = extractKind(json);
        return switch (kind) {
            case VIDEO -> context.deserialize(json, YouTubeVideoId.class);
            case CHANNEL, PLAYLIST -> throw new UnsupportedOperationException(MessageFormat.format(
                    "{0} type search result id deserialization is currently not supported.", kind));
        };
    }
}
