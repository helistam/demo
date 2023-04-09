package com.example.demo;

import com.example.demo.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Counter.CounterService;
import com.example.demo.Counter.CounterThread;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@ControllerAdvice
public class Calculator {

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);
    int result = 0;
    @Autowired
    private Cache cache;
    @GetMapping("/hello")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> hello(@Valid @NotNull(message = "First number cannot be null") @RequestParam(value = "first") String first,
                                        @Valid @NotNull(message = "Second number cannot be null") @RequestParam(value = "second") String second,
                                        @RequestParam(value = "operator", defaultValue = "+") String operator) {
        CounterThread counter = new CounterThread();
        counter.start();
        String cacheKey = String.format("%s %s %s", first, operator, second);
        String cachedResult = cache.get(cacheKey);
        if (cachedResult != null) {
            logger.info("Retrieved result from cache");
            return ResponseEntity.ok("result="+cachedResult+"counter="+CounterService.getCounter());
        }
        int num1 = Integer.parseInt(first);
        int num2 = Integer.parseInt(second);

        if (!operator.matches("[+\\-*/]")) {
            String message = "Invalid operator: " + operator;
            logger.error(message);
            return ResponseEntity.badRequest().body(message);
        }


            switch (operator) {
                case "+":
                    result = num1 + num2;
                    cache.put(String.format("%d+%d", num1, num2), String.valueOf(result));
                    break;
                case "-":
                    result = num1 - num2;
                    cache.put(String.format("%d-%d", num1, num2), String.valueOf(result));
                    break;
                case "*":
                    result = num1 * num2;
                    cache.put(String.format("%d*%d", num1, num2), String.valueOf(result));
                    break;
                case "/":
                    try {
                    result = num1 / num2;
                    cache.put(String.format("%d/%d", num1, num2), String.valueOf(result));
                    } catch (ArithmeticException e) {
                        String message = "Cannot divide by zero";
                        logger.error(message, e);
                        return ResponseEntity.badRequest().body(message);
                    }
                    break;
                default:
                    result = num1; // default to num1 if operator is not recognized
                    break;
            }

        cache.put(cacheKey,String.valueOf(result));

        String message = String.format("%d %s %d = %d", num1, operator, num2, result);
        logger.info(message);
        return new ResponseEntity<>("Result: counter= " + CounterService.getCounter() + ", " + num1 + operator+ num2 +"= "+result, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String message = "Internal server error";
        logger.error(message, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}