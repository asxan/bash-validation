#!/bin/bash

#   Script for buck uping data which get as a parameters next datas:
#   1. Path to synchronized dir
#   2. Path to directory in which will save file copies
#   In case if new files will be added or removed, script must add new
#   record to log-file with  information about time, type of operation
#   and name of file.
# Created by Vitalii Klymov

function back_up_data()
{   
    date +%d.%m.%Y%t%H:%M:%S >> output/backup_script_log
    rsync --verbose --archive --human-readable --compress --delete "$1"  "$2" >> output/backup_script_log
    echo "" >> output/backup_script_log
}

function Main()
{
    if [[ "$#" -eq 2 ]]; then
    {
        if [[ -e "$1" && -d "$1" ]]; then 
        {
            if [[ -e "$2" && -d "$2" ]]; then
            {
                back_up_data "$@"
            }
            else
            {
                echo "Directory at this path $2  does not exist!"
            }
            fi
        } 
        else
        {
            echo "Directory at this path $1  does not exist!"
        }
        fi
    }
    else
    {
        echo """You must specify two parameters for this script
        1. Full path to  folder which will synchronize
        2. Full path to directory for saving datas"""
    }
    fi
}

Main "$@"
