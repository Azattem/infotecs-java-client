package org.example;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleRunner {
    private final ClientService clientService ;
    private final Scanner scanner;
    public ConsoleRunner(InputStream inputStream,ClientService clientService) {
        this.clientService = clientService;
        scanner = new Scanner(inputStream);
    }

    public void run(){
    System.out.println("Доступные команды:\n" +
            "list - вывести все пары домен-ip в алфавитном порядке\n" +
            "findip <domain> - вывести ip-адрес по имени\n" +
            "finddomain <ip> -вывести домен по ip-адресу \n" +
            "add <domain> <ip> - добавить пару домен-ip в файл\n" +
            "remove <domain or ip> - удалить пару домен-ip из файла по ip или домену\n" +
            "exit  - завершение работы");
    while (scanner.hasNextLine()){
    String[] args = scanner.nextLine().trim().split("\\s+");
    if(args.length<1){
        System.out.println("Неизвестная команда");
        continue;
    }
    switch (args[0]){
        case "list":
        case "exit":
            if(args.length>1){
            System.out.println("Команда не принимает аргументы");
            continue;
            }
            if(args[0].equals("list")){
            System.out.print(clientService.getAll());
            }else {
            return;
            }
            continue;
        case "add":
            if(args.length!=3){
                System.out.println("Некоректное количество аргументов");
                continue;
            }
            System.out.println(clientService.add(args[1],args[2]));
            continue;
        case "findip":
        case "finddomain":
        case "remove":
            if (args.length!=2){
                System.out.println("Некоректное количество аргументов");
                continue;
            }
            if(args[0].equals("remove")){
                System.out.println(clientService.remove(args[1]));
            } else if(args[0].equals("findip")){
                System.out.println(clientService.getByDomain(args[1]));
            }else {
                System.out.println(clientService.getByIp(args[1]));
            }
            continue;


    }
    }
    }
}
