package com.cwc.phonebook.controller;

import com.cwc.phonebook.model.PhoneBook;
import com.cwc.phonebook.services.PhoneBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhoneBookController.class)
class PhoneBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneBookService mockPhoneBookService;

    @Test
    void testUpdateContacts() throws Exception {
        // Setup
        // Configure PhoneBookService.updateContact(...).
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);
        when(mockPhoneBookService.updateContact(eq("phoneBookId"), any(PhoneBook.class))).thenReturn(phoneBook);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/contacts/update/{phoneBookId}", "phoneBookId")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetContactById() throws Exception {
        // Setup
        // Configure PhoneBookService.getPhoneBookById(...).
        final PhoneBook phoneBook = new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false);
        when(mockPhoneBookService.getPhoneBookById("phoneBookId")).thenReturn(phoneBook);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/{phoneBookId}", "phoneBookId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetContactById_PhoneBookServiceReturnsNull() throws Exception {
        // Setup
        when(mockPhoneBookService.getPhoneBookById("phoneBookId")).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/{phoneBookId}", "phoneBookId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetContacts() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetContacts_PhoneBookServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPhoneBookService.getAllContacts()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testDeleteContacts() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(delete("/contacts/{phoneBookId}", "phoneBookId")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockPhoneBookService).deleteContactById("phoneBookId");
    }

    @Test
    void testGetCounts() throws Exception {
        // Setup
        when(mockPhoneBookService.countContacts()).thenReturn(0L);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSearchByKeyWord() throws Exception {
        // Setup
        // Configure PhoneBookService.searchContactsByKeyWord(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.searchContactsByKeyWord("firstname")).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/contacts/search-keyword/{firstname}", "firstname")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSearchByKeyWord_PhoneBookServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPhoneBookService.searchContactsByKeyWord("firstname")).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/contacts/search-keyword/{firstname}", "firstname")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetSortedContacts() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/sorts/{firstname}", "firstname")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportToExcel() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/excel")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportToExcel_PhoneBookServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPhoneBookService.getAllContacts()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/excel")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportToExcel_ThrowsIOException() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/excel")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllC() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/get-all")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllC_PhoneBookServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPhoneBookService.getAllContacts()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/get-all")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testExportToPDF() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/pdf")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportToPDF_PhoneBookServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPhoneBookService.getAllContacts()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/pdf")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportToPDF_ThrowsDocumentException() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/pdf")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportToPDF_ThrowsIOException() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/pdf")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportCSV() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/csv")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportCSV_PhoneBookServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPhoneBookService.getAllContacts()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/csv")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testExportCSV_ThrowsException() throws Exception {
        // Setup
        // Configure PhoneBookService.getAllContacts(...).
        final List<PhoneBook> phoneBookList = List.of(
                new PhoneBook("phoneBookId", "firstname", "lastname", "mobile", false));
        when(mockPhoneBookService.getAllContacts()).thenReturn(phoneBookList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/contacts/export/csv")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testImportPhoneBookFromExcelToDb() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(multipart("/contacts/import/excel")
                        .file(new MockMultipartFile("files", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                "content".getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockPhoneBookService).importToDb(List.of());
    }
}
