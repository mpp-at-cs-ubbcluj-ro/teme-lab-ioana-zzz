package org.example;

import org.example.Domain.Person;
import org.example.repository.database.PersonDBRepository;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties props = new Properties();

        try {
            props.load(new FileReader("C:\\Users\\ioana\\Desktop\\faculty\\sem III\\MAP\\teme-lab-ioana-zzz\\temaLab3\\bd.config"));
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find bd.config " + e);
        } catch (IOException e1) {
            System.out.println("Error reading bd.config " + e1);
        }

        PersonDBRepository personDBRepository = new PersonDBRepository(props);
        List<Person> people = personDBRepository.findAll();
        System.out.println("\n\n\nPeople from database:");
        people.forEach(System.out::println);
        System.out.print("\n\n\n");

//        personDBRepository.save(new Person(6040507000000L, "Maria", "Pop"" ,LocalDate.parse("2004-05-07",DateTimeFormatter.ISO_LOCAL_DATE), "Bucuresti));
//        personDBRepository.save(new Person(6010101000000L, "Daria", "Iorga", LocalDate.parse("2001-01-01",DateTimeFormatter.ISO_LOCAL_DATE),"Constanta" ));




        people = personDBRepository.findAllPeopleLastName("Pop");
        System.out.println("\n\n\nPeople with last name Pop:");
        people.forEach(System.out::println);
        System.out.print("\n\n\n");

        personDBRepository.update(new Person(6040507000000L, "Maria", "Popovici",LocalDate.parse("2004-05-07",DateTimeFormatter.ISO_LOCAL_DATE),"Bucuresti") );
    }
}
