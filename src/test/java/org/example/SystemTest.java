package org.example;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class SystemTest extends Assert {
    public ConnectionController controller;
    private DataBaseSlice orginalData;
    private PrintStream origSystemOut;
    private PrintStream origSystemErr;
    public final String greet = "Доступные команды:\n" +
            "list - вывести все пары домен-ip в алфавитном порядке\n" +
            "findip <domain> - вывести ip-адрес по имени\n" +
            "finddomain <ip> -вывести домен по ip-адресу \n" +
            "add <domain> <ip> - добавить пару домен-ip в файл\n" +
            "remove <domain or ip> - удалить пару домен-ip из файла по ip или домену\n" +
            "exit - завершение работы\n";
    @Parameters({"host","port","user","password","fileUrl"})
    @BeforeMethod
    public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException, SftpException {
        controller= new ConnectionController(host, port, user, password, fileUrl);
        orginalData = JsonWorker.getData(controller.getSftpChannel(),fileUrl);
        origSystemOut = System.out;
        origSystemErr = System.err;
    }
    @AfterMethod
    public void cleanup() throws JSchException, SftpException, IOException {
        JsonWorker.writeJsonFile(orginalData,controller.getSftpChannel(),controller.getFileUrl());
        controller.disconnect();
        System.setOut(origSystemOut);
        System.setErr(origSystemErr);
    }
    @DataProvider
    public Object[]caseData(){
    Object[][] object = new Object[6][2];
    //сценарий вывода списка
    object[0][0] = new ByteArrayInputStream(("list\n"+"exit").getBytes(StandardCharsets.UTF_8));
    object[0][1] =(greet+"first.domain 192.168.0.1\n" +
            "fourth.domain 192.168.0.4\n" +
            "second.domain 192.168.0.2\n" +
            "third.domain 192.168.0.3\n"+"Bye\n");
    //добавления пункта
    object[1][0] = new ByteArrayInputStream(("add 1.domain 192.168.0.5\n"+"findip 1.domain\n"+"finddomain 192.168.0.5\n"+"exit").getBytes(StandardCharsets.UTF_8));
    object[1][1] = (greet+"Ok\n"+"192.168.0.5\n"+"1.domain\n"+"Bye\n");
    //удаления двух пунктов
    object[2][0] = new ByteArrayInputStream(("remove fourth.domain\n"+"remove 192.168.0.2\n"+"list\n"+"exit").getBytes(StandardCharsets.UTF_8));
    object[2][1] = (greet+"Ok\n"+"Ok\n"+"first.domain 192.168.0.1\n" +
            "third.domain 192.168.0.3\n"+"Bye\n");
    //не правильного ввода
    object[3][0] = new ByteArrayInputStream(("\n"+"list adada\n"+"add sadad adasd adada\n"+"findip asda adada\n"+"remove sdsd sdsds\n"+"exit").getBytes(StandardCharsets.UTF_8));
    object[3][1] = (greet+"Неизвестная команда\n"+"Команда не принимает аргументы\n"+"Некоректное количество аргументов\n"+"Некоректное количество аргументов\n"+"Некоректное количество аргументов\n"+"Bye\n");
    //удаления не существующего элемента
    object[4][0] = new ByteArrayInputStream(("remove 5.domain\n"+"remove 192.34.0.2\n"+"exit").getBytes(StandardCharsets.UTF_8));
    object[4][1] = (greet+"Несодержит домейн 5.domain\n"+"Несодержит Ip 192.34.0.2\n"+"Bye\n");
    //не Ipv4 ip
    object[5][0] = new ByteArrayInputStream(("remove 192.9.7.600\n"+"exit").getBytes(StandardCharsets.UTF_8));
    object[5][1] = (greet+"Несодержит домейн 192.9.7.600\n"+"Bye\n");

    return object;
    }

    @Test(dataProvider = "caseData")
    public void SystemDataTest(ByteArrayInputStream input,String expected){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errStream));
        new ConsoleRunner(input,new ClientService(controller)).run();
        String actual = outputStream.toString();
        assertEquals(actual, expected);
        assertEquals(errStream.toString().length(),0);
    }

}
