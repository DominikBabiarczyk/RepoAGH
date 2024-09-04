package com.ja.dataGui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;


@Getter
@Setter
@ToString
public class Temp {
    private String username;
    private String password;

    HashMap<Integer, String> people;

    public Temp() {};

    public Temp(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
