package mynghn.youtube.enums;

public interface EndPointTemplates {
    String REQUEST_AUTH_DEVICE_N_USER_CODE = "POST /device/code";
    String OBTAIN_TOKEN = "POST /token";
    String SEARCH_LIST = "GET /search";
    String PLAYLISTS_INSERT = "POST /playlists";
    String PLAYLIST_ITEMS_INSERT = "POST /playlistItems";
}
