package com.cwc.phonebook.repositories;

import com.cwc.phonebook.model.PhoneBook;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneBookRepository extends MongoRepository<PhoneBook,String> {
    //business logics methods
    @Query("{ firstname : { $regex : ?0 } }")
    List<PhoneBook> getPhoneBooksByFirstNameRegEx(String firstname);



    @Query("{}")
    List<PhoneBook> sortingContacts(Sort sort);


}
