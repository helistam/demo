package com.example.demo.Services;

import org.springframework.stereotype.Service;
import com.example.demo.entities.ResponseEntiteData;
import com.example.demo.repositories.ResponseRepository;
import java.util.Optional;

@Service
public class NumberService {

    private final ResponseRepository numberRepository;


    public NumberService(ResponseRepository numberRepository) {
        this.numberRepository = numberRepository;
    }


    public void save(ResponseEntiteData numbers) {
        numberRepository.save(numbers);
    }


    public ResponseEntiteData findOne(int id) {
        Optional<ResponseEntiteData> numbers = numberRepository.findById(id);

        return numbers.orElse(null);
    }


}
