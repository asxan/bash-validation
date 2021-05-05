# This is first Shell Script for diploma project
# File with example of part of apache log file is exist in the same directory with this shell sceneries
# Tasks: 
# 1. Get IP address from which was requested the most amount of requests.
# 2. Which page was the most amount of requested.
# 3. How much requests were from each request.
# 4. Which non-existent pages were requested.
# 5. Which hour was the most amount of requests.
# 6. Which search bots appealed to the web site.
# The path to apache log file is inherited as an argument. 

# Function for read the input file
filename=$1

function getIps(){
    awk '{ print $1;}' "$1" | sort -n | uniq -c | sort -n | tail -n 1 | column -t > result_ips.txt
}

function page(){
    awk '{ print $7;}' "$1" | sort  | uniq -c | sort -n | tail -n 1 | column -t  > result_page.txt
}

function numberOfRequstsByIp(){
    awk '{ print $1;}' "$1" | sort -n | uniq -c | sort -n  | column -t | sort -nr > result_requstsbyIps.txt
}

function noneExistPages(){
    awk '{ print $7" "$9 ;}' "$1"  | grep '404' | awk '{print $1}' | column -t | uniq -c | awk '{print $2}'   > result_noneExistPage.txt
}

function requestByTime(){
    awk '{ print $4; }' "$1" | awk -F ':' '{print $2; }' | uniq -c | sort -nr | column -t | head -n 1 | awk '{print $2}'> result_requstByTime.txt
}

function searchBots(){
    awk -F '"' '{ print $6, $1;}' example.log | grep -i 'bot' | awk -F ' -' '{ print $1}' | uniq -c | sed 's/^\s*[0-9].//'> result_searchBots.txt
}



# Main function in the script
function Main()
{   
    if [[ -e "$1" && -s "$1" && -f "$1" ]]
    then
    {
        getIps "$1"
        page "$1"
        numberOfRequstsByIp "$1"
        noneExistPages "$1"
        requestByTime "$1"
        searchBots "$1"
    }
    else
    {
        echo "File which was specified as argument don't exist or is empty or isn't simple text file"
        exit 1
    }
    fi
}

Main ${filename:?"Specify full path to the file as an argument of script"}