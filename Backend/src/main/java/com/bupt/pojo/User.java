package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private int userId;
    private String userName;
    private String userPassword;
    private double userUsage;
    private double userStorage;
    private String userKey;
    private String userEmail;
    private String userType;
    private boolean userStatus;


}
