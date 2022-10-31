package com.redhat.camel.route.coverage.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    private static final String OUTPUT_FILE = "%s.html";

    public String getLastElementOfPath(String path) {

        return Paths.get(path).getFileName().toString();
    }

    public String readFile(final String inputFile) throws IOException {

        Path inputFilePath = Paths.get(inputFile);

        return new String(Files.readAllBytes(Paths.get(inputFilePath.toString())), StandardCharsets.UTF_8);
    }

    public String write(final String rendered, final String inputFileName, final String outputPath) throws IOException {

        Path writeOutputPath = outputFile(inputFileName, outputPath);

        LOG.trace("\n" + rendered);

        Files.write(writeOutputPath, rendered.getBytes(StandardCharsets.UTF_8));

        return writeOutputPath.toString();
    }

    public Path outputFile(final String inputFileName, final String outputPath) {

        Path outputFileName = Paths.get(String.format(OUTPUT_FILE, removeFileExtension(inputFileName)));
        LOG.trace(outputFileName.toString());
        Path writeOutputPath = Paths.get(outputPath, outputFileName.toString());
        LOG.trace(writeOutputPath.toString());

        return writeOutputPath;
    }

    public String removeFileExtension(String filename) {

        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + "[^.]*$";

        return filename.replaceAll(extPattern, "");
    }

    public Set<String> filesInDirectory(String dir) throws IOException {

        Set<String> fileList = new HashSet<>();
        Path dirPath = Paths.get(dir);
        if (!Files.exists(dirPath)) {
            System.out.println("\nDirectory " + dirPath + " does not exist");
            return Collections.emptySet();
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.toString());
                }
            }
        }

        return fileList;
    }
}
