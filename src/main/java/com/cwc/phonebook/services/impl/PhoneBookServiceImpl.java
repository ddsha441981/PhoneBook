package com.cwc.phonebook.services.impl;

import com.cwc.phonebook.exception.ResourceNotFoundException;
import com.cwc.phonebook.model.PhoneBook;
import com.cwc.phonebook.repositories.PhoneBookRepository;
import com.cwc.phonebook.services.PhoneBookService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PhoneBookServiceImpl implements PhoneBookService {

    @Autowired
    private PhoneBookRepository phoneBookRepository;

    @Override
    public PhoneBook addContacts(PhoneBook phoneBook) {
        //unique PhoneBookId
        String phoneBookId = UUID.randomUUID().toString();
        phoneBook.setPhoneBookId(phoneBookId);
        PhoneBook savedContact = phoneBookRepository.save(phoneBook);
        return savedContact;
    }

    @Override
    public PhoneBook updateContact(String phoneBookId, PhoneBook phoneBook) {
        PhoneBook phoneBookIds = phoneBookRepository.findById(phoneBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found with this phoneId"));
        //now update
        phoneBookIds.setFirstname(phoneBook.getFirstname());
        phoneBookIds.setLastname(phoneBook.getLastname());
        phoneBookIds.setMobile(phoneBook.getMobile());
        phoneBookIds.setEnabled(true);

        return phoneBookRepository.save(phoneBookIds);
    }

    @Override
    public PhoneBook getPhoneBookById(String phoneBookId) {
        return phoneBookRepository.findById(phoneBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found with this phoneId"));
    }

    @Override
    public List<PhoneBook> getAllContacts() {
        return phoneBookRepository.findAll();
    }

    @Override
    public void deleteContactById(String phoneBookId) {
        phoneBookRepository.deleteById(phoneBookId);
    }

    @Override
    public Long countContacts() {
        long count = phoneBookRepository.count();
        return count;
    }

    @Override
    public List<PhoneBook> sortContacts(Sort sort) {

        return null;
    }

    @Override
    public List<PhoneBook> latestAddedContacts() {
        return null;
    }

    @Override
    public List<PhoneBook> searchContactsByKeyWord(String firstname) {
        return phoneBookRepository.getPhoneBooksByFirstNameRegEx(firstname);
    }

    @Override
    public void importToDb(List<MultipartFile> multipartfiles) {
        if (!multipartfiles.isEmpty()) {
            List<PhoneBook> phones = new ArrayList<>();
            multipartfiles.forEach(multipartfile -> {
                try {
                    XSSFWorkbook workBook = new XSSFWorkbook(multipartfile.getInputStream());

                    XSSFSheet sheet = workBook.getSheetAt(0);
                    // looping through each row
                    for (int rowIndex = 0; rowIndex < getNumberOfNonEmptyCells(sheet, 0) - 1; rowIndex++) {
                        // current row
                        XSSFRow row = sheet.getRow(rowIndex);
                        // skip the first row because it is a header row
                        if (rowIndex == 0) {
                            continue;
                        }
                        String phoneBookId = String.valueOf(row.getCell(0));
                        String firstname = String.valueOf(row.getCell(1));
                        String lastname = String.valueOf(row.getCell(2));
                        String mobile = String.valueOf(row.getCell(3));
                        boolean isEnabled = Boolean.getBoolean(String.valueOf(row.getCell(4)));
                        PhoneBook phoneBook = PhoneBook.builder()
                                .phoneBookId(phoneBookId)
                                .firstname(firstname)
                                .lastname(lastname)
                                .mobile(mobile)
                                .isEnabled(isEnabled)
                                .build();
                        phones.add(phoneBook);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (!phones.isEmpty()) {
                // save to database
                phoneBookRepository.saveAll(phones);
            }
        }
    }

    private Object getValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return cell.getErrorCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            case _NONE:
                return null;
            default:
                break;
        }
        return null;
    }

    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }


}
