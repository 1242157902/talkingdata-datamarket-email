#!/bin/bash

declare time_flag=$(date --date="1 days ago" +"%Y%m%d")
declare reportdata_home="/home/hadoop/analytics-reportdata"
declare log_home="$reportdata_home/logs"
declare jar_name="analytics-reportdata-1.0-SNAPSHOT.jar"

mkdir -p $log_home
/usr/java/jdk1.6.0_26/bin/java -cp $reportdata_home/$jar_name com.report.main.Main $time_flag report@tendcloud.com >> $log_home/$time_flag.log 2>&1
