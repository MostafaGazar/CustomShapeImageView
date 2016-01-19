# Code Coverage Report generation

To generate the code coverage report, execute the following command:
for debug:
> gradlew testDebugUnitTestCoverage 
for release:
> gradlew testReleaseUnitTestCoverage 

This will generate code coverage report in each of the modules. In order to view the same, open the following file in your browser.
> module/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html  - for debug build
> module/build/reports/jacoco/testReleaseUnitTestCoverage/html/index.html  - for release build


