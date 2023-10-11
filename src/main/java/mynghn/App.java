package mynghn;

import java.util.concurrent.Callable;
import mynghn.spotify.facade.SpotifyPlaylistRetrievalProcessor;
import mynghn.spotify.model.SpotifyPlaylist;
import mynghn.youtube.facade.YouTubePlaylistCreationProcessor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "p-ex",
        version = "SpotifyPlaylist Express 0.1",
        description = "Transfer a Spotify playlist to your YouTube account.",
        mixinStandardHelpOptions = true)
public class App implements Callable<Integer> {

    @Option(names = {"-l", "--src-link"}, description = "Source playlist link (URL)",
            required = true)
    private String srcLink;

    @Override
    public Integer call() throws Exception {
        SpotifyPlaylistRetrievalProcessor retrievalProcessor = new SpotifyPlaylistRetrievalProcessor();
        SpotifyPlaylist spotifyPlaylist = retrievalProcessor.fetch(srcLink);

        YouTubePlaylistCreationProcessor creationProcessor = new YouTubePlaylistCreationProcessor();
        creationProcessor.create(spotifyPlaylist);

        return 0;
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new App()).execute(args));
    }
}
