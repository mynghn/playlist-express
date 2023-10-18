package mynghn.spotify.model;

import java.util.List;
import lombok.Builder;

@Builder
public record SpotifyPlaylist(String description, String id, String name, List<Track> tracks) {

}
