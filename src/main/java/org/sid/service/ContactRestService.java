package org.sid.service;

import org.sid.dao.ContactRepository;
import org.sid.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactRestService {
	
	@Autowired
	private ContactRepository contactRepository;

	@GetMapping(path = "/", produces = "application/json")
	public ResponseEntity<List<Contact>> getAllContact(@RequestParam(required = false) String title) {
		try {
			List<Contact> contacts = new ArrayList<Contact>();

			if (title == null)
				contactRepository.findAll().forEach(contacts::add);
			else
				contactRepository.findByTitleContaining(title).forEach(contacts::add);
			if (contacts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(contacts, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Contact> getContact(@PathVariable long id) {
		Optional<Contact> contactData = contactRepository.getContactId(id);
		return contactData.map(contact -> new ResponseEntity<>(contact, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> save(@RequestBody Contact c) {
		try {

			Contact _contact = contactRepository.save(new Contact(c.getNom(), c.getPrenom(), c.getEmail(), c.getDateNaissance(), c.getNumero()));
			return new ResponseEntity<>(_contact, HttpStatus.CREATED);
		} catch (Exception e) {
			List<String> messages = new ArrayList<String>();
			String m1 = "Le nom ne peut pas etre null";
			String m2 = "Le prenom ne peut pas etre null";
			if (c.getNom() == null)
				messages.add(m1);
			if (c.getPrenom() == null)
				messages.add(m2);
			Error error = new Error(messages, HttpStatus.INTERNAL_SERVER_ERROR, new Date());
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		List<String> messages = new ArrayList<String>();
		String m1 = "Une erreur s'est produite";
		String m2 = "Verifier votre saisie";
		messages.add(m1);
		messages.add(m2);
		Error error = new Error(messages, HttpStatus.INTERNAL_SERVER_ERROR, new Date());
		try {
			contactRepository.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}catch (Exception e ) {
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping (path = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> edit(@PathVariable Long id, @RequestBody Contact c) {
		Optional<Contact> contactData = contactRepository.getContactId(id);
		List<String> messages = new ArrayList<String>();
		String m1 = "Ce contact n'existe pas";
		String m2 = "Verifier votre saisie";
		messages.add(m1);
		messages.add(m2);
		Error error = new Error(messages, HttpStatus.NOT_FOUND, new Date());
		if (contactData.isPresent()) {
			Contact _contact = contactData.get();
			_contact.setNom(c.getNom());
			_contact.setPrenom(c.getPrenom());
			_contact.setEmail(c.getEmail());
			_contact.setDateNaissance(c.getDateNaissance());
			_contact.setNumero(c.getNumero());
			return new ResponseEntity<>(contactRepository.save(_contact), HttpStatus.OK);
		} else
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
}
