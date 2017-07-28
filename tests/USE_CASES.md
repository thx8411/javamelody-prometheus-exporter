# USE CASES

## Introduction

To be production ready, this application must be fully tested. At this time,
tests still are manual.

## YML file

| case                     | expected result                  | test result |
| ------------------------ | -------------------------------- | ----------- |
| file missing             | no startup / Error               | not tested  |
| settings missing         | use default values / Warning     | not tested  |
| applications missing     | no startup / Error               | not tested  |
| mandatory fields missing | no startup / Error               | not tested  |
| optional fields missing  | ignore                           | not tested  |
| bad types in fields      | no startup / Error               | not tested  |
| wrong url/login/pass     | ignore / Warning                 | not tested  |
