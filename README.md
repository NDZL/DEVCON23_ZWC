# DEVCON23_ZWC
Sample code showing how to use some Large Screens APIs from Android and how to configure Zebra Workstation Connect at runtime.

This app needs to be granted the permission to send ZWC configurations.

The easiest way to achieve that is through Stagenow, using the AccessManager Feature as shown below.

The Service Identifier is
"delegation-zebra-workstationconnect-api-access-configuration-service"

then you also need this app's signature in CRT format

![image](https://github.com/NDZL/DEVCON23_ZWC/assets/11386676/1ac53aef-c612-4a0e-8eae-82c82ae96262)

The same outcome can be achieved programmatically as shown here https://github.com/NDZL/ZVA_sERVICE_ALLOW_CALLER (provided that an administrator has not 
restricted the developer from accessing the EMDK)
