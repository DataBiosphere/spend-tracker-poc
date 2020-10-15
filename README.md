# spend-tracker-poc
This project contains POC code for the Spend Tracker component.

Relevant documents:
* [Spend Tracking Summary](https://docs.google.com/document/d/1Iz5DrGIC5VgGgonGqNqNAznMHoVzO6slyMa9pRxvt2Y/)
* [Spend Tracking POC](https://docs.google.com/document/d/1Dt16-Af9TtUsW5rXcirIHPcDcXMKxGJgi6g_TlAe1II/)

Relevant code:
* [Test Runner README](https://github.com/DataBiosphere/jade-data-repo/blob/develop/datarepo-clienttests/README.md)

Jump to sections below:
* [POC Code Overview](#POC-Code-Overview)
* [Running POC Code](#Running-POC-Code)
  * [Example Commands](#Example-Commands)
* [Example Setup](#Example-Setup)

## POC Code Overview
This POC code was written as scripts for the Test Runner infrastructure.
This made it easier to use different credentials (e.g. test users, service account) and to use the Data Repo client 
library, because there was already utility code for both of these things in the Test Runner infrastructure.

Below is a list of all the classes and files written for this POC.

These classes and test configuration files create cloud resources directly.
  * testscripts.spendtracker.SpendAlert : Creates a budget and alert threshold.
  * testscripts.spendtracker.SpendDataBigQuery : Creates a BigQuery dataset and tables, populates and labels them.
  * testscripts.spendtracker.SpendDataBigQueryJob : Runs a BigQuery job and labels it.
  * testscripts.spendtracker.SpendDataStorage : Creates a bucket, populates and labels it.
  * resources/suites/SpendTracker.json : A list of all the test configurations that exercise the above classes.
     * resources/configs/spendtracker/SpendAlertProject.json
     * resources/configs/spendtracker/SpendDataBigQuery.json
     * resources/configs/spendtracker/SpendDataBigQueryJob.json
     * resources/configs/spendtracker/SpendDataStorage.json
     * resources/configs/spendtracker/SpendDataStorageRequestorPays.json

These classes and test configuration files call Data Repo to create cloud resources.
  * testscripts.baseclasses.CreateProfile : Creates a Data Repo billing profile.
  * testscripts.CreateDataset : Creates a dataset, optionally uploads files and tabular data with their references.
  * resources/suites/SpendTrackerDataRepo.json : A list of all the test configurations that exercise the above classes.
     * resources/configs/spendtracker/DatasetNoFiles.json
     * resources/configs/spendtracker/DatasetWithFile.json
     * resources/configs/spendtracker/DatasetWithFileRequestorPays.json

These classes are utility classes, the ones most likely to be helpful the next time this gets picked up.
  * common.utils.LabelUtils : Methods for validating labels.
  * common.utils.BudgetUtils : Methods for creating budgets.

## Running POC Code
Running the POC code is the same as running any Test Runner code.
The command outline is below.
```
./gradlew runTest --args="configOrSuiteFileName outputDirectoryName"
```
More details are in the [Test Runner README](https://github.com/DataBiosphere/jade-data-repo/blob/develop/datarepo-clienttests/README.md)
 and in the output of the printHelp Gradle task.
```
./gradlew printHelp
```

#### Example Commands
Below are some example commands that use the test suite and configuration files listed above.
```
./gradlew runTest --args="configs/spendtracker/SpendAlertProject.json /tmp/TestRunnerResults/spendtrackerpoc_1"
./gradlew runTest --args="configs/spendtracker/SpendDataBigQuery.json /tmp/TestRunnerResults/spendtrackerpoc_2"
./gradlew runTest --args="configs/spendtracker/SpendDataBigQueryJob.json /tmp/TestRunnerResults/spendtrackerpoc_3"
./gradlew runTest --args="configs/spendtracker/SpendDataStorage.json /tmp/TestRunnerResults/spendtrackerpoc_4"
./gradlew runTest --args="configs/spendtracker/SpendDataStorageRequestorPays.json /tmp/TestRunnerResults/spendtrackerpoc_5"

./gradlew runTest --args="configs/spendtracker/DatasetNoFiles.json /tmp/TestRunnerResults/spendtrackerpoc_6"
./gradlew runTest --args="configs/spendtracker/DatasetWithFile.json /tmp/TestRunnerResults/spendtrackerpoc_7"
./gradlew runTest --args="configs/spendtracker/DatasetWithFileRequestorPays.json /tmp/TestRunnerResults/spendtrackerpoc_8"
```

## Example Setup
I ran the scripts that don't use Data Repo in two clean projects under No Organization: 
[spend-tracker-poc-2](https://console.cloud.google.com/home/dashboard?project=spend-tracker-poc-2) and 
[spend-tracker-poc-3](https://console.cloud.google.com/home/dashboard?project=spend-tracker-poc-3).

Spend data is set up to export into the BigQuery dataset [spend-tracker-poc-2.billing_export](https://console.cloud.google.com/bigquery?project=spend-tracker-poc-2&p=spend-tracker-poc-2&d=billing_export&page=dataset).

These two projects are funded by a non-Broad billing account [01C5F5-9F88EC-850831](https://console.cloud.google.com/billing/01C5F5-9F88EC-850831) 
setup with my work GCard as the payment method. 
