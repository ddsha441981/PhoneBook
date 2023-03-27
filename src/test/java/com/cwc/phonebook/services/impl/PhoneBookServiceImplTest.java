package com.cwc.phonebook.services.impl;

import com.cwc.phonebook.exception.ResourceNotFoundException;
import com.cwc.phonebook.model.PhoneBook;
import com.cwc.phonebook.repositories.PhoneBookRepository;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneBookServiceImplTest {

    @Mock
    private PhoneBookRepository mockPhoneBookRepository;

    @InjectMocks
    private PhoneBookServiceImpl phoneBookServiceImplUnderTest;

    @Test
    void testAddContacts() {
        // Setup
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);

        // Configure PhoneBookRepository.save(...).
        final PhoneBook phoneBook1 = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);
        when(mockPhoneBookRepository.save(any(PhoneBook.class))).thenReturn(phoneBook1);

        // Run the test
        final PhoneBook result = phoneBookServiceImplUnderTest.addContacts(phoneBook);

        // Verify the results
    }

    @Test
    void testAddContacts_PhoneBookRepositoryThrowsOptimisticLockingFailureException() {
        // Setup
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);
        when(mockPhoneBookRepository.save(any(PhoneBook.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> phoneBookServiceImplUnderTest.addContacts(phoneBook))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testUpdateContact() {
        // Setup
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);

        // Configure PhoneBookRepository.findById(...).
        final Optional<PhoneBook> phoneBook1 = Optional.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookRepository.findById("phoneBookId")).thenReturn(phoneBook1);

        // Configure PhoneBookRepository.save(...).
        final PhoneBook phoneBook2 = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);
        when(mockPhoneBookRepository.save(any(PhoneBook.class))).thenReturn(phoneBook2);

        // Run the test
        final PhoneBook result = phoneBookServiceImplUnderTest.updateContact("phoneBookId", phoneBook);

        // Verify the results
    }

    @Test
    void testUpdateContact_PhoneBookRepositoryFindByIdReturnsAbsent() {
        // Setup
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);
        when(mockPhoneBookRepository.findById("phoneBookId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> phoneBookServiceImplUnderTest.updateContact("phoneBookId", phoneBook))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUpdateContact_PhoneBookRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);

        // Configure PhoneBookRepository.findById(...).
        final Optional<PhoneBook> phoneBook1 = Optional.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookRepository.findById("phoneBookId")).thenReturn(phoneBook1);

        when(mockPhoneBookRepository.save(any(PhoneBook.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> phoneBookServiceImplUnderTest.updateContact("phoneBookId", phoneBook))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testGetPhoneBookById() {
        // Setup
        // Configure PhoneBookRepository.findById(...).
        final Optional<PhoneBook> phoneBook = Optional.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookRepository.findById("phoneBookId")).thenReturn(phoneBook);

        // Run the test
        final PhoneBook result = phoneBookServiceImplUnderTest.getPhoneBookById("phoneBookId");

        // Verify the results
    }

    @Test
    void testGetPhoneBookById_PhoneBookRepositoryReturnsAbsent() {
        // Setup
        when(mockPhoneBookRepository.findById("phoneBookId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> phoneBookServiceImplUnderTest.getPhoneBookById("phoneBookId"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetAllContacts() {
        // Setup
        // Configure PhoneBookRepository.findAll(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookRepository.findAll()).thenReturn(phoneBookList);

        // Run the test
        final List<PhoneBook> result = phoneBookServiceImplUnderTest.getAllContacts();

        // Verify the results
    }

    @Test
    void testGetAllContacts_PhoneBookRepositoryReturnsNoItems() {
        // Setup
        when(mockPhoneBookRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<PhoneBook> result = phoneBookServiceImplUnderTest.getAllContacts();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testDeleteContactById() {
        // Setup
        // Run the test
        phoneBookServiceImplUnderTest.deleteContactById("phoneBookId");

        // Verify the results
        verify(mockPhoneBookRepository).deleteById("phoneBookId");
    }

    @Test
    void testCountContacts() {
        // Setup
        when(mockPhoneBookRepository.count()).thenReturn(0L);

        // Run the test
        final Long result = phoneBookServiceImplUnderTest.countContacts();

        // Verify the results
        assertThat(result).isEqualTo(0L);
    }

    @Test
    void testSortContacts() {
        assertThat(phoneBookServiceImplUnderTest.sortContacts(Sort.by("properties"))).isNull();
    }

    @Test
    void testLatestAddedContacts() {
        assertThat(phoneBookServiceImplUnderTest.latestAddedContacts()).isNull();
    }

    @Test
    void testSearchContactsByKeyWord() {
        // Setup
        // Configure PhoneBookRepository.getPhoneBooksByFirstNameRegEx(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookRepository.getPhoneBooksByFirstNameRegEx("firstname")).thenReturn(phoneBookList);

        // Run the test
        final List<PhoneBook> result = phoneBookServiceImplUnderTest.searchContactsByKeyWord("firstname");

        // Verify the results
    }

    @Test
    void testSearchContactsByKeyWord_PhoneBookRepositoryReturnsNoItems() {
        // Setup
        when(mockPhoneBookRepository.getPhoneBooksByFirstNameRegEx("firstname")).thenReturn(Collections.emptyList());

        // Run the test
        final List<PhoneBook> result = phoneBookServiceImplUnderTest.searchContactsByKeyWord("firstname");

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testImportToDb() {
        // Setup
        final List<MultipartFile> multipartfiles = List.of();

        // Configure PhoneBookRepository.saveAll(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookRepository.saveAll(
                List.of(new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false))))
                .thenReturn(phoneBookList);

        // Run the test
        phoneBookServiceImplUnderTest.importToDb(multipartfiles);

        // Verify the results
        verify(mockPhoneBookRepository).saveAll(
                List.of(new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false)));
    }

    @Test
    void testImportToDb_PhoneBookRepositoryThrowsOptimisticLockingFailureException() {
        // Setup
        final List<MultipartFile> multipartfiles = List.of();
        when(mockPhoneBookRepository.saveAll(
                List.of(new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false))))
                .thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> phoneBookServiceImplUnderTest.importToDb(multipartfiles))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testGetNumberOfNonEmptyCells() {
        // Setup
        final XSSFSheet sheet = null;

        // Run the test
        final int result = PhoneBookServiceImpl.getNumberOfNonEmptyCells(sheet, 0);

        // Verify the results
        assertThat(result).isEqualTo(0);
    }
}
