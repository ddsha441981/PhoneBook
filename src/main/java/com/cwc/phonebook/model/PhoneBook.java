package com.cwc.phonebook.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class PhoneBook implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @CsvBindByPosition(position = 0)
    private String phoneBookId;
    @CsvBindByPosition(position = 1)
    private String firstname;
    @CsvBindByPosition(position = 2)
    private String lastname;
    @CsvBindByPosition(position = 3)
    private String mobile;
    private boolean isEnabled;

}
