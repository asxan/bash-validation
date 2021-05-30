#!/bin/bash
# Creat script which calls with next keys:
# 1. When script is caled without parameters,outputs the line of keys and their description
# 2. Key --all outputs all ip addresses and symbol names of nodes in the subnet
# 3. Key --target=X.X.X.X outputs list of all open system TCP ports

# Global variables
addresses=""

# Function for key --all
function viewAllNames()
{
    ip_addr="$(ip a | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*/[0-9]{2}'  | awk '{print $2;}' | grep -v "127.*" | sed 's/[[:digit:]]\{0,3\}\//*\//' )"
    addresses=$(nmap -sn  $ip_addr | grep -E '([0-9]*\.){3}[0-9]*' | awk '{print $(NF-1),$NF}' | column -t | sed 's/(//' | sed 's/)//' | tee output/second_allNames.txt | awk '{print $2;}' > output/ip_addrs.txt )
}


function openPorts()
{
    nmap -sT "$1"  | grep "open" | column -t >> output/tcp_ports.txt
}


function Main() 
{
    for i in "$@"
    do
    case $i in
        --all)
            viewAllNames
            shift
        ;;
        --target=?*)
            openPorts "${i#*=}"
            shift
        ;;
        *)
            echo "Parameter like this or value which has been given as an argument don't exist"
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