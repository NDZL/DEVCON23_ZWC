// IZVAService.aidl
package com.zebra.workstationconnect;
//package com.ndzl.zwc;

// Declare any non-default types here with import statements

interface IZVAService {

    String processZVARequest(in String json);
}