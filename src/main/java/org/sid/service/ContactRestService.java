package org.sid.service;

import org.sid.dao.ContactRepository;
import org.sid.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
	public ResponseEntity<Contact> save(@RequestBody Contact c) {
		try {
			Contact _contact = contactRepository.save(new Contact(c.getNom(), c.getPrenom(), c.getEmail(), c.getDateNaissance(), c.getNumero()));
			return new ResponseEntity<>(_contact, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
		try {
			contactRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e ) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping (path = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Contact> edit(@PathVariable Long id, @RequestBody Contact c) {
		Optional<Contact> contactData = contactRepository.getContactId(id);
		if (contactData.isPresent()) {
			Contact _contact = contactData.get();
			_contact.setNom(c.getNom());
			_contact.setPrenom(c.getPrenom());
			_contact.setEmail(c.getEmail());
			_contact.setDateNaissance(c.getDateNaissance());
			_contact.setNumero(c.getNumero());
			return new ResponseEntity<>(contactRepository.save(_contact), HttpStatus.OK);
		} else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
}
