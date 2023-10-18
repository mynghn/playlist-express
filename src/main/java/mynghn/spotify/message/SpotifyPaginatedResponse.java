package mynghn.spotify.message;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public record SpotifyPaginatedResponse<T>(@SerializedName("limit") int pageSize,
                                          @SerializedName("next") String nextPageUrl,
                                          int offset,
                                          @SerializedName("previous") String prevPageUrl,
                                          int total,
                                          List<T> items) {

}
