# Extract Payload Utility:
This Utility does the Following:
Uses RestAssured to call an api to get the response based on a data set in excel file
Gets and stores the response in the same excel file

# Dependencies
 - Maven - Project Management Framework
 - TestNG - For creating the test and send the file path to the
 - REST-Assured - To get the Response from the API
 - POI - For Reading and Writing excel files


# Steps To Run this Utility:
  - Get the Path of the .xslx file
  - In the TestNG field update the *value* with the path of the file along with file extension in <parameter name="path" value="Give your file path" />
  - Make sure you are connect to the VPN
  - Run it as a TestNG Suite.
  - Once the run is successful, you can see the same file updated with the extracted payload in the last column 
