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
    public static final String FIELD_HLS_LIST_SIZE = "HLS_LIST_SIZE";
    public static final String FIELD_HLS_TIME = "HLS_TIME";
    public static final String FIELD_DELETE_M3U8 = "DELETE_M3U8";
    public static final String FIELD_DELETE_TS = "DELETE_TS";

    // FFMPEG
    private String ffmpegPath = null;
    private String ffprobePath = null;
    private int hlsListSize = 0;
    private int hlsTime = 0;
    private boolean deleteM3u8 = true;
    private boolean deleteTs = true;

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

        this.hlsListSize = Integer.parseInt(getIniValue(SECTION_FFMPEG, FIELD_HLS_LIST_SIZE));
        if (this.hlsListSize <= 0) {
            logger.error("Fail to load [{}-{}]. ({})", SECTION_FFMPEG, FIELD_HLS_LIST_SIZE, hlsListSize);
            System.exit(1);
        }

        this.hlsTime = Integer.parseInt(getIniValue(SECTION_FFMPEG, FIELD_HLS_TIME));
        if (this.hlsTime <= 0) {
            logger.error("Fail to load [{}-{}]. ({})", SECTION_FFMPEG, FIELD_HLS_TIME, hlsTime);
            System.exit(1);
        }

        this.deleteM3u8 = Boolean.parseBoolean(getIniValue(SECTION_FFMPEG, FIELD_DELETE_M3U8));
        this.deleteTs = Boolean.parseBoolean(getIniValue(SECTION_FFMPEG, FIELD_DELETE_TS));

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

    public void setFfmpegPath(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }

    public String getFfprobePath() {
        return ffprobePath;
    }

    public void setFfprobePath(String ffprobePath) {
        this.ffprobePath = ffprobePath;
    }

    public int getHlsListSize() {
        return hlsListSize;
    }

    public void setHlsListSize(int hlsListSize) {
        this.hlsListSize = hlsListSize;
    }

    public int getHlsTime() {
        return hlsTime;
    }

    public void setHlsTime(int hlsTime) {
        this.hlsTime = hlsTime;
    }

    public boolean isDeleteM3u8() {
        return deleteM3u8;
    }

    public void setDeleteM3u8(boolean deleteM3u8) {
        this.deleteM3u8 = deleteM3u8;
    }

    public boolean isDeleteTs() {
        return deleteTs;
    }

    public void setDeleteTs(boolean deleteTs) {
        this.deleteTs = deleteTs;
    }
}
