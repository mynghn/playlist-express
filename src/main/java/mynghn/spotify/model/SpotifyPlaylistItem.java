package mynghn.spotify.model;

import lombok.Getter;

@Getter
public abstract class SpotifyPlaylistItem {

    protected final SpotifyPlaylistItemType type;

    protected final String id;

    public SpotifyPlaylistItem(SpotifyPlaylistItemType type, String id) {
        this.type = type;
        this.id = id;
    }
}
