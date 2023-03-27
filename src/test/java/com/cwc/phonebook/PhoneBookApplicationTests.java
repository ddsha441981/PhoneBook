package com.cwc.phonebook;

import com.cwc.phonebook.repositories.PhoneBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PhoneBookApplicationTests {

	@Autowired
	private PhoneBookRepository phoneBookRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testRepository(){
		System.out.println("Test case started");
		phoneBookRepository.findAll().forEach((phoneBook) -> System.out.println(phoneBook.getFirstname()));
	}
}
