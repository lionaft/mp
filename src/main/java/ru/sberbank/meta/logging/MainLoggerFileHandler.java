package ru.sberbank.meta.logging;

import java.io.IOException;
import java.util.logging.FileHandler;

public class MainLoggerFileHandler extends FileHandler {
    private static final String filePattern = "./OSLMS%g.%u.log";

    public MainLoggerFileHandler() throws IOException, SecurityException {
        super(filePattern, 1024 * 1024 * 10, 50, true);
        setFormatter(new FileFormatter());
    }

    public MainLoggerFileHandler(int limit, int count) throws IOException, SecurityException {
        super(filePattern, limit, count);
        setFormatter(new FileFormatter());
    }

    public MainLoggerFileHandler(String pattern) throws IOException, SecurityException {
        super(pattern);
        setFormatter(new FileFormatter());
    }

    public MainLoggerFileHandler(String pattern, boolean append) throws IOException, SecurityException {
        super(pattern, append);
        setFormatter(new FileFormatter());
    }

    public MainLoggerFileHandler(String pattern, int limit, int count) throws IOException, SecurityException {
        super(pattern, limit, count);
        setFormatter(new FileFormatter());
    }

    public MainLoggerFileHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
        super(pattern, limit, count, append);
        setFormatter(new FileFormatter());
    }

}
