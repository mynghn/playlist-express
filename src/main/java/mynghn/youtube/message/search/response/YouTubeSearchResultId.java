package mynghn.youtube.message.search.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class YouTubeSearchResultId {

    protected final String kind;
}
