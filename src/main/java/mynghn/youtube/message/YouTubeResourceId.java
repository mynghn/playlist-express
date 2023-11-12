package mynghn.youtube.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class YouTubeResourceId {

    protected final String kind;
}
