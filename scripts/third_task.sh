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
    echo "123"
}

function Main()
{
    if [[ "$#" -eq 0 ]]; then
    {
        
    }
    elif [[ "$#" -eq 2 ]]; then
    {
        # find ~ -type d -iname $(basename "$1") 1> /dev/null
        # code1=$(echo "$?")
        # find ~ -type d -iname $(basename "$2") 1> /dev/null
        # code2=$(echo "$?")
        # if [[ "$code1" -ne 0 ]]; then

        if [[ -d "$1"  &&  -d "$2" ]]; then 
        {
            tar -zcf "$(basename $1).tgz" $1 > /dev/null 2>&1
            cp -R "$(basename $1).tgz" "$2"
            rm -rf "$(basename $1).tgz"
        } 
        else
        {
            echo "One of the dirs doesn't exist"
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