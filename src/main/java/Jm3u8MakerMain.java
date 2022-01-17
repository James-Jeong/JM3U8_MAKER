import config.ConfigManager;
import ffmpeg.FfmpegManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AppInstance;

public class Jm3u8MakerMain {

    private static final Logger logger = LoggerFactory.getLogger(Jm3u8MakerMain.class);

    public static void main(String[] args) {
        logger.debug("[START]");

        if (args.length != 7) {
            logger.error(
                    "Argument Error. \n" +
                    "(&0: Jm3u8MakerMain, &1: config_path, \n" +
                    "&2: SourceFilePath, &3: DestinationFilePath, &4:FileTime)\n" +
                            "Ex) Jm3u8MakerMain \n" +
                            "~/config/user_conf.ini \n" +
                            "~/test1/Seoul.mp4 \n" +
                            "~/test2/Seoul.m3u8 \n" +
                            "1"
            );
        } else {
            int index = 0;
            for (String argument : args) {
                if (argument == null) { continue; }
                logger.debug("[{}] [{}]", index++, argument);
            }

            String configPath = args[1].trim();
            logger.debug("| Config path: {}", configPath);
            ConfigManager configManager = new ConfigManager(configPath);

            AppInstance appInstance = AppInstance.getInstance();
            appInstance.setConfigManager(configManager);

            String srcFilePath = args[2].trim();
            String destFilePath = args[3].trim();
            long fileTime = Long.parseLong(args[4].trim());
            if (fileTime > 0) {
                FfmpegManager ffmpegManager = new FfmpegManager();
                ffmpegManager.convertMp4ToM3u8(
                        srcFilePath, // srcFilePath
                        destFilePath, // destTotalFilePath
                        fileTime, // fileTime
                        0, // startTime
                        (long) ffmpegManager.getFileTime(srcFilePath) // endTime
                );
            } else {
                logger.error("File time is negative. Fail to get m3u8. (fileTime={})", fileTime);
            }
        }

        logger.debug("[END]");
    }

}
