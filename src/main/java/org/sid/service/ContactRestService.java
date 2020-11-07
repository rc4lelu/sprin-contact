package org.sid.service;

import org.sid.dao.ContactRepository;
import org.sid.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactRestService {
	
	@Autowired
	private ContactRepository contactRepository;

	@GetMapping(path = "/", produces = "application/json")
	public List<Contact> getAllContact() {
		return contactRepository.findAll();
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public Optional<Contact> getContact(@PathVariable Long id) {
		return contactRepository.findById(id);
	}

	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public Contact save(@RequestBody Contact c) {
		return contactRepository.save(c);
	}
}
