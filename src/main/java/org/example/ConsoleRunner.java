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
    System.out.print("Доступные команды:\n" +
            "list - вывести все пары домен-ip в алфавитном порядке\n" +
            "findip <domain> - вывести ip-адрес по имени\n" +
            "finddomain <ip> -вывести домен по ip-адресу \n" +
            "add <domain> <ip> - добавить пару домен-ip в файл\n" +
            "remove <domain or ip> - удалить пару домен-ip из файла по ip или домену\n" +
            "exit - завершение работы\n");
    while (scanner.hasNextLine()){
    String[] args = scanner.nextLine().trim().split("\\s+");
    if(args.length<1){
        System.out.print("Неизвестная команда\n");
        continue;
    }
    switch (args[0]){
        case "list":
        case "exit":
            if(args.length>1){
            System.out.print("Команда не принимает аргументы\n");
            continue;
            }
            if(args[0].equals("list")){
            System.out.print(clientService.getAll());
            }else {
            System.out.print("Bye\n");
            return;
            }
            continue;
        case "add":
            if(args.length!=3){
                System.out.print("Некоректное количество аргументов\n");
                continue;
            }
            System.out.print(clientService.add(args[1],args[2])+"\n");
            continue;
        case "findip":
        case "finddomain":
        case "remove":
            if (args.length!=2){
                System.out.print("Некоректное количество аргументов\n");
                continue;
            }
            if(args[0].equals("remove")){
                System.out.print(clientService.remove(args[1])+"\n");
            } else if(args[0].equals("findip")){
                System.out.print(clientService.getByDomain(args[1])+"\n");
            }else {
                System.out.print(clientService.getByIp(args[1])+"\n");
            }
            continue;
        default:
            System.out.print("Неизвестная команда"+"\n");
    }
    }
    }
}
