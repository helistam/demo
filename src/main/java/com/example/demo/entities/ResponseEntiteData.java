package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "calculator")
public class ResponseEntiteData {
    @Basic
    @Column(name = "first", nullable = false)
    private String first;
    @Basic
    @Column(name = "second", nullable = false)
    private String second;
    @Basic
    @Column(name = "operator", nullable = false)
    private String operator;
    @Basic
    @Column(name = "result", nullable = false)
    private int result;
    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public ResponseEntiteData() {
    }

    public ResponseEntiteData(String first, String second, String operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getOperator() {
        return operator;
    }

    public Integer getId() {
        return id;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
