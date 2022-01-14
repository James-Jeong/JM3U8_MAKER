import config.ConfigManager;
import ffmpeg.FfmpegManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AppInstance;

public class Jm3u8MakerMain {

    private static final Logger logger = LoggerFactory.getLogger(Jm3u8MakerMain.class);

    public static void main(String[] args) {
        if (args.length != 7) {
            System.out.println(
                    "Argument Error. \n" +
                    "(&0: Jm3u8MakerMain, &1: config_path, \n" +
                    "&2: SourceFilePath, &3: DestinationFilePath, \n" +
                    "&4: FileTime, &5: StartTime, &6: EndTime)\n" +
                            "Ex) Jm3u8MakerMain \n" +
                            "~/config/user_conf.ini \n" +
                            "~/test1/Seoul.mp4 \n" +
                            "~/test2/Seoul.m3u8 1 0 10"
            );
            return;
        } else {
            int index = 0;
            for (String argument : args) {
                logger.debug("[{}] [{}]", index++, argument);
            }
            logger.debug("\n");
        }

        String configPath = args[1].trim();
        logger.debug("| Config path: {}", configPath);
        ConfigManager configManager = new ConfigManager(configPath);

        AppInstance appInstance = AppInstance.getInstance();
        appInstance.setConfigManager(configManager);

        FfmpegManager ffmpegManager = new FfmpegManager();
        ffmpegManager.convertMp4ToM3u8(
                args[2].trim(), // srcFilePath
                args[3].trim(), // destTotalFilePath
                Long.parseLong(args[4].trim()), // fileTime
                Long.parseLong(args[5].trim()), // startTime
                Long.parseLong(args[6].trim()) // endTime
        );
        logger.debug("\n");
    }

}
