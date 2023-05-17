package com.cwc.phonebook.controller;

import com.cwc.phonebook.exception.ResourceNotFoundException;
import com.cwc.phonebook.model.PhoneBook;
import com.cwc.phonebook.services.PhoneBookService;
import com.cwc.phonebook.utils.PhoneBookExcelExporter;
import com.cwc.phonebook.utils.PhoneBookPdfGenerator;
import com.lowagie.text.DocumentException;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/contacts")
public class PhoneBookController {

    //ResponseEntity not support serialization

    @Autowired
    private PhoneBookService phoneBookService;


    @PostMapping("/")
    public ResponseEntity<?> addContact(@RequestBody PhoneBook phoneBook){
        PhoneBook savedContacts = phoneBookService.addContacts(phoneBook);
        return new ResponseEntity<>(savedContacts,HttpStatus.CREATED);
    }

    @PutMapping("/{phoneBookId}")
    public ResponseEntity<?> updateContacts(@PathVariable("phoneBookId") String phoneBookId, @RequestBody PhoneBook phoneBook) {
        PhoneBook updatedContact =  null;
        if (!phoneBookId.isEmpty()) {
            updatedContact = phoneBookService.updateContact(phoneBookId, phoneBook);
        } else {
            throw new ResourceNotFoundException("Resource not found Controller");
        }
//       String id = phoneBookId.isEmpty() ? phoneBookService.updateContact(phoneBookId,phoneBook): throw new ResourceNotFoundException("");
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    //ResponseEntity not support serialization
//    @Cacheable(value = "contacts",key = "#phoneBookId")
//    @GetMapping("/{phoneBookId}")
//    public PhoneBook getContactById(@PathVariable("phoneBookId") String phoneBookId){
//        PhoneBook phoneBookById = phoneBookService.getPhoneBookById(phoneBookId);
//        if (phoneBookById == null)throw new ResourceNotFoundException("Id not found with this id Controller");
//        return phoneBookById;
//    }

//
    @GetMapping("/{phoneBookId}")
    public ResponseEntity<?> getContactById(@PathVariable("phoneBookId") String phoneBookId){
        PhoneBook phoneBookById = phoneBookService.getPhoneBookById(phoneBookId);
        if (phoneBookById == null)throw new ResourceNotFoundException("Id not found with this id Controller");
        return new ResponseEntity<>(phoneBookById ,HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<PhoneBook>> getContacts(){
        List<PhoneBook> listContacts = phoneBookService.getAllContacts();
        return new ResponseEntity<>(listContacts,HttpStatus.OK);
    }

    @DeleteMapping("/{phoneBookId}")
    public ResponseEntity<?> deleteContacts(@PathVariable("phoneBookId") String phoneBookId){
         phoneBookService.deleteContactById(phoneBookId);
        return new ResponseEntity<>("Contact deleted..." ,HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll(){
        phoneBookService.deleteContactAll();
        return new ResponseEntity<>("All Contact deleted...",HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCounts(){
        Long contacts = phoneBookService.countContacts();
        return new ResponseEntity<>(contacts,HttpStatus.OK);
    }

    @GetMapping("/search-keyword/{firstname}")
    public ResponseEntity<List<PhoneBook>> searchByKeyWord(@PathVariable("firstname") String firstname){
        List<PhoneBook> searchedKeyword = phoneBookService.searchContactsByKeyWord(firstname);
        return new ResponseEntity<>(searchedKeyword,HttpStatus.OK);
    }

    @GetMapping("/sorts/{firstname}")
    public ResponseEntity<List<PhoneBook>> getSortedContacts(@PathVariable("firstname") String firstname){
//        System.out.println("Sorting Method is ruuning");
//       Sort sort =  new Sort(new Sort.Order(Sort.Direction.ASC,firstname).ignoreCase());
////        Sort sorted = Sort.by(Sort.Direction.DESC, firstname);
//        System.out.println("Sorting Method is ruuning" + sorted);
//        List<PhoneBook> sortedList = phoneBookService.sortContacts(sorted);
//        return new ResponseEntity<>(sortedList,HttpStatus.OK);
        return null;
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=phoneBook_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<PhoneBook> phoneBookList = phoneBookService.getAllContacts();
        PhoneBookExcelExporter excelExporter = new PhoneBookExcelExporter(phoneBookList);
        excelExporter.export(response);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneBook> getAllC() {
        log.info("Get all Contacts");
        return phoneBookService.getAllContacts();
    }



    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<PhoneBook> listPhoneBooks = phoneBookService.getAllContacts();

        PhoneBookPdfGenerator exporter = new PhoneBookPdfGenerator(listPhoneBooks);
        exporter.export(response);

    }

    @GetMapping("/export/csv")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "phonebook.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<PhoneBook> writer = new StatefulBeanToCsvBuilder<PhoneBook>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all phonebook to csv file
        writer.write(phoneBookService.getAllContacts());

    }


    @PostMapping(value = "/import/excel")
    public void importPhoneBookFromExcelToDb(@RequestPart(required = true) List<MultipartFile> files) {
        phoneBookService.importToDb(files);

    }


}
