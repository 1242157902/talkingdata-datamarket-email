#!/bin/bash

declare time_flag=$(date --date="1 days ago" +"%Y%m%d")
declare reportdata_home="/home/hadoop/analytics-reportdata"
declare log_home="$td-emaillogs/logs"
declare jar_name="talkingdata-datamarket-email-1.0-SNAPSHOT.war"

mkdir -p $log_home
/usr/java/jdk1.6.0_26/bin/java -cp $reportdata_home/$jar_name com.report.main.Main $time_flag report@tendcloud.com >> $log_home/$time_flag.log 2>&1
