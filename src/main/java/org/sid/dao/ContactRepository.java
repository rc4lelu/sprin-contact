package org.sid.dao;

import org.sid.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    public Optional<Contact> getContactId (@Param("id") Long id);

    @Query("SELECT c FROM Contact c WHERE c.nom = :nom")
    List<Contact> findByTitleContaining(@Param("nom") String nom);


}
