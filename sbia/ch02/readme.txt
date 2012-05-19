Code source for chapter 02 of Spring Batch in Action

Follow the instructions from the book to launch the correct programs and
see the effect of launching batch jobs (thank to Spring Batch Admin).

Here are the available programs:
  - LaunchDatabaseAndConsole: launches H2 with a ready-to-use database and the H2 HTTP console
(access the console at http://127.0.1.1:8082/, the URL of the database is 
jdbc:h2:tcp://localhost/mem:sbia_ch02, default username and password are fine).
  - GeneratesJobMetaData: launches batch processes to generate some metadata in Spring
Batch's tables. These are then available from the H2 console.
  - LaunchSpringBatchAdmin: launches the Spring Batch Admin web application. Accessible
from http://localhost:8080/springbatchadmin/. Allows browsing jobs' metadata.
  - LaunchImportProductsJob: launch the import products job. Generates jobs' metadata to
check what Spring Batch stores.