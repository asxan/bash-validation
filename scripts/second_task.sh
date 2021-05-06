#!/bin/bash
# Creat script which calls with next keys:
# 1. When script is caled without parameters, output the line of keys and their description
# 2. Key --all outputs all ip addresses and symbol names all of network nodes in the subnet
# 3. Key --target=X.X.X.X output list of all open system TCP ports

function viewAllNames()
{
    nmap -Sp
}

function openPorts()
{
    echo "$1"
}


function Main() 
{
    for i in "$@"
    do
    case $i in
        --all=?*)
            viewAllNames
            shift
        ;;
        --target=?*)
            openPorts "${i#*=}"
            shift
        ;;
        *)
            echo "Argument such as this don't exist"
        ;;
    esac
    done
}

if [[ "$#" -eq 1 ]]; then
{
    Main "$@"
}
else 
{
    echo "Usage: second_task [ OPTIONS ]"
    echo ""
    echo -e "\t OPTIONS := { --all {All ip address and symbols names in subnet}"
    echo -e "\t\t      --target=X.X.X.X Shows list of open system TCP ports   }" 
}
fi