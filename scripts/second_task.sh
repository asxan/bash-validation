#!/bin/bash
# Creat script which calls with next keys:
# 1. When script is caled without parameters, output the line of keys and their description
# 2. Key --all outputs all ip addresses and symbol names all of network nodes in the subnet
# 3. Key --target=X.X.X.X output list of all open system TCP ports

function viewAllNames()
{
    #ip_addr="$(ip a | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*/[0-9]{2}'  | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v "127.*" | grep -Eo '([0-9]*\.){3}')"
    #ip_addr="$(ip a | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*/[0-9]{2}'  | awk '{print $2;}' | grep -v "127.*" | grep -Eo '([0-9]*\.){3}')"
    ip_addr="$(ip a | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*/[0-9]{2}'  | awk '{print $2;}' | grep -v "127.*" | grep -Eo '([0-9]*\.){3}')"
    ip_a="${ip_addr}"
    echo "$ip_a"
    #nmap -sn  $ip_a | grep -E '([0-9]*\.){3}[0-9]*' | column -t | awk '{print $(NF-1),$NF}' 
    #> output/second_allNames.txt 
}

function get_net_mask()
{
    ip_mask_cidr=
    net_mask=$(ipcalc -p 1.1.1.1 $1 | sed -n 's/^PREFIX=\(.*\)/\/\1/p')
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
        --all)
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