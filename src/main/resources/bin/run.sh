#!/bin/sh

java -jar -Dlogback.configurationFile=/Users/jamesj/GIT_PROJECTS/JM3U8_MAKER/src/main/resources/config/logback.xml /Users/jamesj/GIT_PROJECTS/JM3U8_MAKER/out/artifacts/JM3U8_MAKER_jar/JM3U8_MAKER.jar Jm3u8MakerMain /Users/jamesj/GIT_PROJECTS/JM3U8_MAKER/src/main/resources/config/user_conf.ini $1 $2 $3 $4 $5