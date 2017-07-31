# USE CASES

## Introduction

To be production ready, this application must be fully tested. At this time,
tests still are manual.

## YML configuration file

| case                       | expected result                  | test result | Issues |
| -------------------------- | -------------------------------- | ----------- | ------ |
| file missing               | no startup / Error               | OK          | NaN    |
| settings (doc) missing     | use default values / Warning     | OK          | NaN    |
| applications (doc) missing | no startup / Error               | OK          | NaN    |
| applications missing       | no startup / Error               | OK          | NaN    |
| mandatory app name missing | no startup / Error               | OK          | NaN    | 
| mandatory url missing      | no startup / Error               | OK          | NaN    |
| mandatory metrics missing  | no startup / Error               | OK          | NaN    |
| mandatory m name missing   | no startup / Error               | OK          | NaN    |
| optional login missing     | ignore                           | OK          | NaN    |
| optional password missing  | ignore                           | OK          | NaN    |
| optional labels missing    | ignore                           | OK          | NaN    |
| wrong metric name          | no startup / Error               | OK          | NaN    |


## Metric scraping

| case                       | expected result                  | test result | Issues |
| -------------------------- | -------------------------------- | ----------- | ------ |
| wrong url                  | ignore / Warning                 | OK          | NaN    |
| wrong login                | ignore / Warning                 | not tested  |        |
| wrong password             | ignore / Warning                 | not tested  |        |
| non responding url         | ignore / Warning                 | OK          | NaN    |
| no data for metric         | use value -1 / Warning           | OK          | NaN    |

## Load tests

