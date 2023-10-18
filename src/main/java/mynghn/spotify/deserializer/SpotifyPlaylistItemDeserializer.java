package mynghn.spotify.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import mynghn.spotify.model.SpotifyPlaylistItem;
import mynghn.spotify.model.SpotifyPlaylistItemType;
import mynghn.spotify.model.Track;

public class SpotifyPlaylistItemDeserializer implements JsonDeserializer<SpotifyPlaylistItem> {

    private static final String ITEM_TYPE_JSON_FIELD_NAME = "type";

    private static SpotifyPlaylistItemType extractItemType(JsonElement json) {
        return SpotifyPlaylistItemType.valueOf(json.getAsJsonObject()
                .getAsJsonPrimitive(ITEM_TYPE_JSON_FIELD_NAME).getAsString());
    }

    @Override
    public SpotifyPlaylistItem deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        return switch (extractItemType(json)) {
            case track -> context.deserialize(json, Track.class);
            case episode -> throw new UnsupportedOperationException(
                    "Episode type playlist item deserialization is currently not supported.");
        };
    }
}
