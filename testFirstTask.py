#!/usr/bin/env python3

"""
Run: python -m xmlrunner -o junit-reports test.py
"""

import unittest
import xmlrunner
from unittest.mock import patch
from io import StringIO
import sys
import re

class MyTestCase(unittest.TestCase):

    # Test 1 subtask in first script
    def test_getIps(self):
        file = open('../gold_solution/output/result_ips.txt', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/result_ips.txt', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)

    # Test 2 subtask in first script
    def test_page(self):
        file = open('../gold_solution/output/result_page.txt', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/result_page.txt', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)

    # Test 3 subtask in first script
    def test_requstsbyIps(self):
        file = open('../gold_solution/output/result_requstsbyIps.txt', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/result_requstsbyIps.txt', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)

    # Test 4 subtask in first script
    def test_noneExistPage(self):
        file = open('../gold_solution/output/result_noneExistPage.txt', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/result_noneExistPage.txt', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)

    # Test 5 subtask in first script
    def test_requstByTime(self):
        file = open('../gold_solution/output/result_requstByTime.txt', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/result_requstByTime.txt', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)

    # Test 6 subtask in first script
    def test_searchBots(self):
        file = open('../gold_solution/output/result_searchBots.txt', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/result_searchBots.txt', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)


if __name__ == '__main__':
    unittest.main(
        testRunner=xmlrunner.XMLTestRunner(output='test-reports'),
        # these make sure that some options that are not applicable
        # remain hidden from the help menu.
        failfast=False, buffer=False, catchbreak=False)