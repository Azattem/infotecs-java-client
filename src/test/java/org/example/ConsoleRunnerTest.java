package org.example;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ConsoleRunnerTest extends Assert {
    public ConnectionController controller;
    public final String greet = "Доступные команды:\n" +
            "list - вывести все пары домен-ip в алфавитном порядке\n" +
            "findip <domain> - вывести ip-адрес по имени\n" +
            "finddomain <ip> -вывести домен по ip-адресу \n" +
            "add <domain> <ip> - добавить пару домен-ip в файл\n" +
            "remove <domain or ip> - удалить пару домен-ip из файла по ip или домену\n" +
            "exit - завершение работы\n";
    private DataBaseSlice orginalData;
    private PrintStream origSystemOut;
    private PrintStream origSystemErr;
    @Parameters({"host","port","user","password","fileUrl"})
    @BeforeClass
    public void setup(String host,String port,String user,String password,String fileUrl) throws JSchException, SftpException {
        controller= new ConnectionController(host, port, user, password, fileUrl);
        orginalData = JsonWorker.getData(controller.getSftpChannel(),fileUrl);
        origSystemOut = System.out;
        origSystemErr = System.err;
    }
    @AfterClass
    public void cleanup() throws JSchException, SftpException, IOException {
        JsonWorker.writeJsonFile(orginalData,controller.getSftpChannel(),controller.getFileUrl());
        controller.disconnect();
        System.setOut(origSystemOut);
        System.setErr(origSystemErr);
    }
    @DataProvider
    public Object[] userInput(){
    return new Object[][]{
            {"list",greet+"first.domain 192.168.0.1\n" +
                    "fourth.domain 192.168.0.4\n" +
                    "second.domain 192.168.0.2\n" +
                    "third.domain 192.168.0.3\n"},
            {"findip fourth.domain",greet+"192.168.0.4\n"},
            {"finddomain 192.168.0.1",greet+"first.domain\n"},
            {"add 1.domain 192.168.0.5",greet+"Ok\n"},
            {"remove 1.domain",greet+"Ok\n"},
            {"exit",greet+"Bye\n"},
            {"list adada",greet+"Команда не принимает аргументы\n"},
            {"\n",greet+"Неизвестная команда\n"},
            {"exit das",greet+"Команда не принимает аргументы\n"},
            {"add sadad adasd adada",greet+"Некоректное количество аргументов\n"},
            {"findip asda adada",greet+"Некоректное количество аргументов\n"},
            {"finddomain sdsdsd sdsdsd",greet+"Некоректное количество аргументов\n"},
            {"remove sdsd sdsds",greet+"Некоректное количество аргументов\n"},
    };
    }
    @Test(dataProvider = "userInput")
    public void consoleRunnerInputTest(String input,String expected){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errStream));
        new ConsoleRunner(new ByteArrayInputStream(input.getBytes()),new ClientService(controller)).run();
        String actual = outputStream.toString();
        assertEquals(actual, expected);
        assertEquals(errStream.toString().length(),0);
    }
}
