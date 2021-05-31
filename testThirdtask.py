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
    def test_brforeDeleting(self):
        file = open('../gold_solution/output/backup_script_log_1', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/backup_script_log_1', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)

    # Test 2 subtask in first script
    def test_Deleting(self):
        file = open('../gold_solution/output/backup_script_log_2', 'r')
        expected_output = file.read()
        file.close()
        file = open('../student_solution/output/backup_script_log_2', 'r')
        received_output = file.read()
        file.close()
        self.assertEqual(received_output, expected_output)


if __name__ == '__main__':
    unittest.main(
        testRunner=xmlrunner.XMLTestRunner(output='test-reports'),
        # these make sure that some options that are not applicable
        # remain hidden from the help menu.
        failfast=False, buffer=False, catchbreak=False)