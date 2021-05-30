import json
import sys
import os
import argparse


class ArgsWrongNumber(Exception):
    def __str__(self):
        print("Wrong number of arguments!")
        sys.exit(1)


class FileAbsence(Exception):
    def __str__(self):
        print("Can't open the file")
        sys.exit(1)


def get_argument():
    argv = sys.argv
    try:
        if len(argv) != 3:
            raise ArgsWrongNumber
        else:
            return sys.argv
    except ArgsWrongNumber as error:
        print(error)


def args_parser(argv):
    parser = argparse.ArgumentParser(description='This script parse json output of parsing xml test result')
    parser.add_argument('json_file_path', type=str, help='json_file')
    parser.add_argument('result_output_path', type=str, help='txt_file')
    args = parser.parse_args(argv[1:])

    return args


def read_json_file(json_file: str):
    try:
        with open(json_file, "r", encoding='utf-8') as file:
            data = json.load(file)
    except IOError:
        print("File can't be read!")
        sys.exit(1)

    return data


def parse_json(data):
    pretty = json.dumps(data["taskResult"]["details"], indent=4, sort_keys=True).strip("[]").strip("\n").strip("{  }").\
        split("\n")
    new_line = ""
    for line in pretty:
        if line != "":
            new_line = new_line + line.strip(" ")
        else:
            continue
    return new_line


def write_json(parse_data, file_path):
    try:
        with open(file_path, "w", encoding='utf-8') as file:
            file.write(parse_data)
    except:
        print("Unable to create file or rewrite on disk!")


def main():
    arguments = vars(args_parser(get_argument()))
    if arguments['json_file_path']:
        if not os.path.exists(arguments['json_file_path']):
            raise FileAbsence

    parse_data = parse_json(read_json_file(arguments['json_file_path']))
    
    write_json(parse_data, arguments['result_output_path'])


if __name__ == '__main__':
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
