# ILoggr
iLoggr is a suite of software that provides iPhone application developers critical information about how their apps are being used and to remotely control and track their usage.

This project is well suited for individuals and companies that want to host fully control their iPhone metrics and statistics rather than trusting hosted services such as Pinch and Flurry.

iLoggr has a Java-based server that services iPhone applications with light-weight messages to a service that logs the "events" in a Mysql database. Developers can run reports to find out how and when their application is being used. This backend java service could be used with any http capable smart phone.

iLoggr also includes iPhone application client code that provides the ILoggr class object to log events and get application specific provisioning information.

All logging information can be retrieved via an easy to use web interface. Logging data can be viewed or downloaded as a comma delimited (excel compatible) file for off-line processing.

Major features of iLoggr include:

Track application usage including unique phones/users and application use duration statistics.
See where most of your users are located geographically.
Perform remote logging for applications.
Popup message-of-the-day in an application where message can be changed via web interface.
Additional remote control features for your applications.
Messages sent from the phone to the back-end servers are light-weight, JSON formatted messages.
