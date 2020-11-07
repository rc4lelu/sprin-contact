package org.sid;

import org.sid.dao.ContactRepository;
import org.sid.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class ContactApplication implements CommandLineRunner {

    @Autowired
    private ContactRepository contactRepository;

    public static void main(String[] args) {
        SpringApplication.run(ContactApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        contactRepository.save(new Contact("Evouna", "Onana", "evounar55@gmail.com",
                dateFormat.parse("05/04/2001"), 690459460));
        contactRepository.save(new Contact("Manga", "Ela", "ela55@gmail.com",
                dateFormat.parse("08/09/1998"), 670059460));
        contactRepository.save(new Contact("Pierre", "Stephane", "pierrre@gmail.com",
                dateFormat.parse("23/07/2000"), 660459970));

        contactRepository.findAll().forEach(c-> {
            System.out.println(c.getNom());
        });
    }
}
