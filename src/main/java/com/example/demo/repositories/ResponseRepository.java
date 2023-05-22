package com.example.demo.repositories;
import com.example.demo.entities.ResponseEntiteData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<ResponseEntiteData, Integer> {
}