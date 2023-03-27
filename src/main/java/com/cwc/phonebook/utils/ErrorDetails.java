package com.cwc.phonebook.utils;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
