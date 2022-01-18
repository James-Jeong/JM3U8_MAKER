import config.ConfigManager;
import ffmpeg.FfmpegManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AppInstance;

public class Jm3u8MakerMain {

    private static final Logger logger = LoggerFactory.getLogger(Jm3u8MakerMain.class);

    public static void main(String[] args) {
        logger.debug("[START]");

        if (args.length != 4) {
            logger.error(
                    "Argument Error. \n" +
                    "(&0: Jm3u8MakerMain, &1: config_path, \n" +
                    "&2: SourceFilePath, &3: DestinationFilePath, &4:FileTime)\n" +
                            "Ex) Jm3u8MakerMain \n" +
                            "~/config/user_conf.ini \n" +
                            "~/test1/Seoul.mp4 \n" +
                            "~/test2/Seoul.m3u8"
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

            FfmpegManager ffmpegManager = new FfmpegManager();
            long endTime = (long) ffmpegManager.getFileTime(srcFilePath);
            long fileTime = endTime / 10;
            if (fileTime < 10) {
                fileTime += 10;
            }
            logger.debug("HLS-INTERVAL: {}", endTime / 10);
            logger.debug("END-TIME: {}", endTime);

            if (endTime > 0) {
                ffmpegManager.convertMp4ToM3u8(
                        srcFilePath, // srcFilePath
                        destFilePath, // destTotalFilePath
                        fileTime, // fileTime
                        0, // startTime
                        endTime // endTime
                );
            } else {
                logger.error("File time is not positive. Fail to get m3u8. (endTime={})", endTime);
            }
        }

        logger.debug("[END]");
    }

}
