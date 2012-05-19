Code source for chapter 11 of Spring Batch in Action "Enterprise Integration"

To start the web container of the enterprise integration use case, 
launch the LaunchEnterpriseIntegrationServer class.

If you have CURL installed on your computer, you can use it to submit imports to the server.
Open a shell in the directory of the project and launch the following command:

curl http://localhost:8080/enterpriseintegration/product-imports/partner1-1 -H 'Content-type:text/xml;charset=utf-8' -X PUT --data-binary @./data-samples/products-partner1-1.xml

If you don't have CURL installed, you can launch the SubmitImport class to submit an import.

You can then go to following URLs (with a web browser):
  - http://localhost:8080/enterpriseintegration/products (list of the products)
  - http://localhost:8080/enterpriseintegration/product-imports/partner1-1 (status of the import)
  
You can re-iterate the same operation with the partner1-2 and partner1-3-error imports.