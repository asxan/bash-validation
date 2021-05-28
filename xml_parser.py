#!/usr/bin/env python3

"""
Run without parameters in CLI.
python3 <xml_log_file> <output_json_file>
"""
import argparse
import os
import xmltodict
import json
import sys


def args_parser(argv):
    parser = argparse.ArgumentParser(description='This script process tests results.')
    parser.add_argument('file_path', type=str, help='xml_file')
    parser.add_argument('file_output', type=str, help='json_file')
    args = parser.parse_args(argv[1:])

    return args


# Converts .xml to Dictionary
def get_data_dict(xml_file: str):
    with open(xml_file) as xml_file:
        data_dict = xmltodict.parse(xml_file.read())
    return data_dict


# Extracts all required for analyzing data
def data_parsing(data: dict):
    results_list = []

    for testsuite in data.values():
        for testsuite_key, testsuite_value in testsuite.items():
            if isinstance(testsuite_value, list):

                for testcase in testsuite_value:
                    for testcase_key, testcase_value in testcase.items():
                        if isinstance(testcase_value, dict):

                            temp_event = []
                            for action_key, action_value in testcase_value.items():
                                temp_event.append({action_key: action_value})
                            results_list.append({testcase_key: temp_event})
            else:
                if testsuite_key == "@tests" \
                        or testsuite_key == "@failures" \
                        or testsuite_key == "@errors" \
                        or testsuite_key == "@skipped":
                    results_list.append({testsuite_key: testsuite_value})

    return results_list


# 1st test results analyser
def static_analyser(pars_data: list):
    in_data = pars_data
    tests_quantity = None
    failures = None
    errors = None
    skipped = None
    events = []

    for field in in_data:
        for key, value in field.items():
            if isinstance(value, list):
                events.append({key: value})
            else:
                if key == "@tests":
                    tests_quantity = int(value)
                if key == "@failures":
                    failures = int(value)
                if key == "@errors":
                    errors = int(value)
                if key == "@skipped":
                    skipped = int(value)

    result = dict()
    not_passed_tests = failures + errors + skipped

    result["name"] = "Static analysis"

    if not_passed_tests == 0:
        status = "OK"
    else:
        status = "CRITICAL_ERROR"
    result["status"] = status

    if not_passed_tests > 0:
        pers_per_test = 100 / tests_quantity
        score = (tests_quantity - not_passed_tests) * pers_per_test
    else:
        score = 100
    result["score"] = int(score)

    result["maxScore"] = 100

    if not_passed_tests > 0:
        all_text = ''
        for event in events:

            for event_key, event_value in event.items():
                all_text += event_key + " : "

                for event_field in event_value:
                    for key, value in event_field.items():
                        if key == "@message":
                            all_text += value + "\n"

        result["comment"] = all_text
    else:
        result["comment"] = ""

    return result


# Total tests results to Dictionary
def total_result(static_analysing_res: dict):
    result = dict()
    static_analysing_data = static_analysing_res
    static_analysing_status = None
    static_analysing_score = None

    for sad_key, sad_value in static_analysing_data.items():
        if sad_key == "status":
            static_analysing_status = sad_value
        if sad_key == "score":
            static_analysing_score = sad_value

    result["version"] = "1.0"
    result["taskResult"] = {
        "status": static_analysing_status,
        "score": static_analysing_score,
        "details": [static_analysing_data]
    }

    return result


# Export to json file
def export_to_json(filename: str, data_dict: dict):
    with open(filename, "w") as json_file:
        json_file.write(json.dumps(data_dict, indent=4))


def main():
    args = vars(args_parser(sys.argv))

    if args['file_path']:
        if not os.path.exists(args['file_path']):
            raise ValueError('Can\'t open the file!')
    filename = args['file_path']

    export_file_name = args['file_output']

    data = dict(get_data_dict(filename))

    static_analysing_res = static_analyser(data_parsing(data))
    result = total_result(static_analysing_res)

    export_to_json(export_file_name, result)


if __name__ == "__main__":
    main()
