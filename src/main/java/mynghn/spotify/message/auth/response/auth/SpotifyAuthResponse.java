package mynghn.spotify.message.auth.response.auth;

public record SpotifyAuthResponse(String accessToken, String tokenType, int expiresIn) {

}
