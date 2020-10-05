package com.adobe.project.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.adobe.project.server.HttpServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * Launcher for the server. This class contains the parser to parse and configure the user specified directory.
 */

@Slf4j
public class HttpServerLauncher {
    private static final String INPUT_ARGUMENT = "input";
    public static final String DEFAULT_DIRECTORY = "./pages/pages_1";

    public static void main(String[] args) throws IOException {

        String inputFilePath = Optional.ofNullable(getInputDirectory(args)).orElse(DEFAULT_DIRECTORY);
        log.info("The server is serving from the path: " + inputFilePath);

        HttpServer httpServer = new HttpServer(inputFilePath);
        httpServer.start();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        log.info("Press Enter to exit the server");
        inputReader.readLine();
        log.info("Stopping the server");
        httpServer.stop();
    }

    private static String getInputDirectory(String[] args) {
        Options options = new Options();

        Option input = new Option("i", INPUT_ARGUMENT, true, "input directory path");
        input.setRequired(false);
        options.addOption(input);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        String inputFilePath = DEFAULT_DIRECTORY;

        try {
            CommandLine cmd = parser.parse(options, args);
            if (args.length != 0) {
                inputFilePath = Optional.ofNullable(cmd.getOptionValue(INPUT_ARGUMENT))
                    .orElseThrow(() -> new ParseException("The inputFilePath is null"));
            }
            log.info("Path to the root containing the files you want the server to serve: " + inputFilePath);
        } catch (ParseException e) {
            log.error("Error parsing the arguments. Read the utility-name below. " + e.getMessage());
            formatter.printHelp("utility-name", options);
        }
        return inputFilePath;
    }
}
