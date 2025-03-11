package org.example;

import jdk.jshell.execution.Util;
import org.example.Domain.validators.UtilizatorValidator;
import org.example.repository.database.UtilizatorDBRepository;

public class Main {
    public static void main(String[] args) {

        UtilizatorDBRepository userRepo = new UtilizatorDBRepository(new UtilizatorValidator(), "jdbc:postgresql://localhost:5432/social_network","postgres","ioana");
        userRepo.findAll().forEach(System.out::println);

    }
}