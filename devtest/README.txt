

This module is thought to run tests in development mode in
order to speed up TDD.

- Put your tests in the DevTestRunner class.
- Call gwtSetup() and your test in the entryPoint.
- If you need any assertion method provided by the junit
  library put it in the class MyTestCase.
  There are already a bunch of them.
- Run the module in development mode: mvn gwt:run 
  or lauch it from eclipse
- See the output of the tests. If there is a failure
  a stacktrace should be shown in the output.
- Modify your test code and reload the application in your browser.
- When your test was ready put it in your test class extending
  GWTTestCase.

- If you prefer superdev mode run: mvn gwt:run-codeserver but
  you have to be familiar with debuging in javascript and use source maps.



