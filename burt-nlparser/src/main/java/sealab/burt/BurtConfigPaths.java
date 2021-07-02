package sealab.burt;

import java.nio.file.Path;

public class BurtConfigPaths {

    public static final String crashScopeDataPath = Path.of("..", "data", "CrashScope-Data").toString();
    public static final String nlParsersBaseFolder = Path.of("..", "burt-nlparser").toString();
    public static final String qualityCheckerResourcesPath = Path.of("..", "burt-quality-checker", "src", "main",
            "resources").toString();
    public static final String traceReplayerDataPath = Path.of("..", "data", "TraceReplayer-Data").toString();
}
