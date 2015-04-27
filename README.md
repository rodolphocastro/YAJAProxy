# YAJAProxy
YAJAProxy (Short for Yet Another Java Proxy) is a simple open-source Proxy writen in Java.
The idea behind YAJAProxy's goal is to allow users to have a lightweight proxy that can redirect traffic from blocked ports to another port, in a way that just might circunvent some badly configured firewalls (I.E. University ones). YAJAP also allows recording of all the exchanged data beetween the client and the remote server, if the user wants.

## Functions
+ Port-Redirecting
+ Data Recording

## System Requirements
+ Java 1.7 or greater.

## How to Use?
+ Simply run the .jar file with the correct parameters.

### Obligatory Parameters
+ -l [num] -> The local port to which the Proxy should collect data from.
+ -r [num] -> The remote port to which the Proxy should foward data to.
+ -h [IPAddress or Hostname] -> The remote server to which the Proxy should send the data.

### Optional Parameters
+ -f [path to a file] -> Prints all the data that goes thru the proxy to a file.
+ -v -> Activates Verbose mode, which will display aditional info on the console.


## Version History
