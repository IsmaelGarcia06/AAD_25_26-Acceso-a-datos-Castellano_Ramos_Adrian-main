package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {

    private int dni;
    private String name;
    private String email;

//    public Person() {
//
//    }

//    public Person(String surname, String dni, String name) {
//        this.dni = dni;
//        this.surname = surname;
//        this.name = name;
//    }
//
//    public String getDni() {
//        return dni;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setDni(String dni) {
//        this.dni = dni;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }


//    @Override
//    public String toString() {
//        return "Person{" +
//                "dni='" + dni + '\'' +
//                ", name='" + name + '\'' +
//                ", surname='" + surname + '\'' +
//                '}';
//    }
}
