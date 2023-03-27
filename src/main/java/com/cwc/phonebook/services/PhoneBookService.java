package com.cwc.phonebook.services;

import com.cwc.phonebook.model.PhoneBook;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhoneBookService {

    //add
    public PhoneBook addContacts(PhoneBook phoneBook);
    //update
    public PhoneBook updateContact(String phoneBookId,PhoneBook phoneBook);
    //get By Id
    public PhoneBook getPhoneBookById(String phoneBookId);
    // get All
    public List<PhoneBook> getAllContacts();
    // delete
    public void deleteContactById(String phoneBookId);

    //business methods
    public Long countContacts();
    public List<PhoneBook> sortContacts(Sort sort);
    public List<PhoneBook> latestAddedContacts();
    public List<PhoneBook> searchContactsByKeyWord(String firstname);
    void importToDb(List<MultipartFile> multipartfiles);
}
