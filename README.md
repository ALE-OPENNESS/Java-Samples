
# Java-Samples
This repository contains sample applications built with the Java SDK.

## SimpleRoutingApp
This test application is built with javaFX. it uses the routing service and listen events. 
The user enters his login, password and the O2G server address and a routing application is displayed.

Please note the O2G server FQDN must be known in your DNS. As an alternative, you can configure the O2G server in the hosts file (C:\Windows\System32\drivers\etc on Windows).
If the FQDN can't be resolved, a connection error is raised even if the provided O2G address is an IPv4 address.

![enter image description here](https://github.com/ALE-OPENNESS/Java-Samples/blob/main/SimpleRoutingApp/SimpleRoutingApp.png?raw=true)

The user can then:
 - Activate a forward
 - Configure the overflow
 - Activate the Do Not Disturb
