package org.example.ServiceTest;

import org.example.ClientService;
import org.example.ConnectionController;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class isIpv4Test extends Assert {
    @DataProvider
    public Object[][]inputData(){
    return new Object[][]{
            {"127.0.0.1",true},
            {"0.0.0.0",true},
            {"255.255.255.255",true},
            {null,false},
            {"",false},
            {"-1.-1.-1.-1",false},
            {"255.256.255.255",false},
            {"adaksdjaoidaj",false},
            {"1,1,1,1",false},
            {"123",false},
            {"51.51.51.51.51",false},
            {"51.51.51",false},
    };
    }
    @Test(dataProvider = "inputData")
    public void testIsPv4output(String input,boolean expected){
    final boolean actual = ClientService.isIpv4(input);
    assertEquals(actual,expected);
    }
}
