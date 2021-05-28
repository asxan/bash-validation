import json
import sys
import argparse


class ArgsWrongNumber(Exception):
    def __str__(self):
        print("Wrong number of arguments!")
        sys.exit(1)


def get_argument():
    argv = sys.argv
    try:
        if len(argv) != 2:
            raise ArgsWrongNumber
        else:
            return sys.argv
    except ArgsWrongNumber as error:
        print(error)


def args_parser(argv):
    parser = argparse.ArgumentParser(description='This script parse json output of parsing xml test result')
    parser.add_argument('json_file_path', type=str, help='json_file')
    args = parser.parse_args(argv[1:])

    return args


def read_json_file(json_file):
    try:
        with open(json_file, "r", encoding='utf-8') as file:
            data = json.load(file)
    except IOError:
        print("File that passed as an argument has not found!")
        sys.exit(1)
    return data


def parse_json(data):
    pretty = json.dumps(data["taskResult"]["details"], indent=4, sort_keys=True).strip("[]").strip("\n").strip("{  }")
    return pretty


def write_json(parse_data):
    try:
        with open("./result_json_parse.txt", "w", encoding='utf-8') as file:
            file.write(parse_data)
    except:
        print("Unable to create file or rewrite on disk!")


def main():
    arguments = get_argument()
    parse_data = parse_json(read_json_file(arguments[1]))
    write_json(parse_data)


if __name__ == '__main__':
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
