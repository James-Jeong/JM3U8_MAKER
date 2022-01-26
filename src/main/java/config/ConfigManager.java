package config;

import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @class public class UserConfig
 * @brief UserConfig Class
 */
public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    public static final int MP2T_TYPE = 33; //RTP payload type for MJPEG video
    public static final String MP2T_TAG = "MP2T"; //RTP payload tag for MJPEG video

    private Ini ini = null;

    // Section String
    public static final String SECTION_FFMPEG = "FFMPEG"; // FFMPEG Section 이름

    // Field String
    public static final String FIELD_FFMPEG_PATH = "FFMPEG_PATH";
    public static final String FIELD_FFPROBE_PATH = "FFPROBE_PATH";
    public static final String FIELD_FPS = "FPS";
    public static final String FIELD_GOP = "GOP";

    // FFMPEG
    private String ffmpegPath = null;
    private String ffprobePath = null;
    private int fps = 0;
    private int gop = 0;

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @fn public AuditConfig(String configPath)
     * @brief AuditConfig 생성자 함수
     * @param configPath Config 파일 경로 이름
     */
    public ConfigManager(String configPath) {
        File iniFile = new File(configPath);
        if (!iniFile.isFile() || !iniFile.exists()) {
            logger.warn("Not found the config path. (path={})", configPath);
            return;
        }

        try {
            this.ini = new Ini(iniFile);

            loadFfmpegConfig();

            logger.info("Load config [{}]", configPath);
        } catch (IOException e) {
            logger.error("ConfigManager.IOException", e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @fn private void loadFfmpegConfig()
     * @brief FFMPEG Section 을 로드하는 함수
     */
    private void loadFfmpegConfig() {
        this.ffmpegPath = getIniValue(SECTION_FFMPEG, FIELD_FFMPEG_PATH);
        if (this.ffmpegPath == null) {
            logger.error("Fail to load [{}-{}].", SECTION_FFMPEG, FIELD_FFMPEG_PATH);
            System.exit(1);
        }

        this.ffprobePath = getIniValue(SECTION_FFMPEG, FIELD_FFPROBE_PATH);
        if (this.ffprobePath == null) {
            logger.error("Fail to load [{}-{}].", SECTION_FFMPEG, FIELD_FFPROBE_PATH);
            System.exit(1);
        }

        String fpsString = getIniValue(SECTION_FFMPEG, FIELD_FPS);
        if (fpsString == null) {
            logger.error("Fail to load [{}-{}].", SECTION_FFMPEG, FIELD_FPS);
            System.exit(1);
        } else {
            fps = Integer.parseInt(fpsString);
            if (fps <= 0) {
                logger.error("Fail to load [{}-{}]. FPS is not positive. (fps={})", SECTION_FFMPEG, FIELD_FPS, fps);
                System.exit(1);
            }

            if (fps > 1000) {
                fps = 30;
            }
        }

        // GOP Size is up to 30 (15 is also very common)
        String gopString = getIniValue(SECTION_FFMPEG, FIELD_GOP);
        if (gopString == null) {
            logger.error("Fail to load [{}-{}].", SECTION_FFMPEG, FIELD_GOP);
            System.exit(1);
        } else {
            gop = Integer.parseInt(gopString);
            if (gop < 0) {
                logger.error("Fail to load [{}-{}]. GOP is not positive. (fps={})", SECTION_FFMPEG, FIELD_GOP, gop);
                System.exit(1);
            }

            if (gop > 30) {
                gop = 16;
            }
        }

        logger.debug("Load [{}] config...(OK)", SECTION_FFMPEG);
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @fn private String getIniValue(String section, String key)
     * @brief INI 파일에서 지정한 section 과 key 에 해당하는 value 를 가져오는 함수
     * @param section Section
     * @param key Key
     * @return 성공 시 value, 실패 시 null 반환
     */
    private String getIniValue(String section, String key) {
        String value = ini.get(section,key);
        if (value == null) {
            logger.warn("[ {} ] \" {} \" is null.", section, key);
            System.exit(1);
            return null;
        }

        value = value.trim();
        logger.debug("\tGet Config [{}] > [{}] : [{}]", section, key, value);
        return value;
    }

    /**
     * @fn public void setIniValue(String section, String key, String value)
     * @brief INI 파일에 새로운 value 를 저장하는 함수
     * @param section Section
     * @param key Key
     * @param value Value
     */
    public void setIniValue(String section, String key, String value) {
        try {
            ini.put(section, key, value);
            ini.store();

            logger.debug("\tSet Config [{}] > [{}] : [{}]", section, key, value);
        } catch (IOException e) {
            logger.warn("Fail to set the config. (section={}, field={}, value={})", section, key, value);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String getFfmpegPath() {
        return ffmpegPath;
    }

    public String getFfprobePath() {
        return ffprobePath;
    }

    public int getFps() {
        return fps;
    }

    public int getGop() {
        return gop;
    }

}
