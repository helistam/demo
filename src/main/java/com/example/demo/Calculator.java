package com.example.demo;

import com.example.demo.Services.NumberService;
import com.example.demo.async.NumberAsync;
import com.example.demo.cache.Cache;
import com.example.demo.repositories.ResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.counter.CounterService;
import com.example.demo.counter.CounterThread;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.entities.ResponseEntiteData;
@RestController
@Validated
@ControllerAdvice

public class Calculator {

    @Autowired
    private ResponseRepository responseRepository;

    public Calculator(NumberService numberService, NumberAsync numberAsync) {
        this.numberService = numberService;
        this.numberAsync = numberAsync;
    }

    public static class OperationRequest {
        private String first;
        private String second;
        private String operator;
        private int result;

        public String getSecond() {
            return second;
        }

        public String getFirst() {
            return first;
        }

        public String getOperator() {
            return operator;
        }

        // Конструкторы, геттеры и сеттеры
    }

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);
    private final NumberService numberService;
    int result = 0;
    @Autowired
    private Cache cache;

    @GetMapping("/calculator")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> hello(@Valid @NotNull(message = "First number cannot be null") @RequestParam(value = "first") String first,
                                        @Valid @NotNull(message = "Second number cannot be null") @RequestParam(value = "second") String second,
                                        @RequestParam(value = "operator", defaultValue = "+") String operator) throws InterruptedException {
        CounterThread counter = new CounterThread();

        ResponseEntiteData numbers = new ResponseEntiteData();
        numbers.setFirst(first);
        numbers.setSecond(second);
        numbers.setOperator(operator);


        counter.start();
        String cacheKey = String.format("%s %s %s", first, operator, second);
        String cachedResult = cache.get(cacheKey);
        if (cachedResult != null) {
            logger.info("Retrieved result from cache");
            return ResponseEntity.ok("result from cache=" + cachedResult + "  counter=" + CounterService.getCounter());
        }
        int num1 = Integer.parseInt(first);
        int num2 = Integer.parseInt(second);

        if (!operator.matches("[+\\-*/@]")) {
            String message = "Invalid operator: " + operator;
            logger.error(message);
            return ResponseEntity.badRequest().body(message);
        }


        switch (operator) {
            case "@":
                return ResponseEntity.ok("  counter=" + CounterService.getCounter());
            case "+":
                result = num1 + num2;
                //cache.put(String.format("%d+%d", num1, num2), String.valueOf(result));
                break;
            case "-":
                result = num1 - num2;
                //cache.put(String.format("%d-%d", num1, num2), String.valueOf(result));
                break;
            case "*":
                result = num1 * num2;
                //cache.put(String.format("%d*%d", num1, num2), String.valueOf(result));
                break;
            case "/":
                try {
                    result = num1 / num2;
                    // cache.put(String.format("%d/%d", num1, num2), String.valueOf(result));
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
        cache.put(String.format("%d%s%d", num1, operator, num2), String.valueOf(result));
        cache.put(cacheKey, String.valueOf(result));
        String message = String.format("%d %s %d = %d", num1, operator, num2, result);
        logger.info(message);
        logger.info("counter=" + CounterService.getCounter());

        numbers.setResult(result);
         numberService.save(numbers);
        return new ResponseEntity<>(num1 + operator + num2 + "= " + result, HttpStatus.OK);
    }

    @PostMapping("/calculator")
    public ResponseEntity<String> bulkOperations(@RequestBody List<OperationRequest> operationRequests) {
        List<String> results = operationRequests.stream()
                .map(request -> {
                    int num1 = Integer.parseInt(request.getFirst());
                    int num2 = Integer.parseInt(request.getSecond());

                    if (!request.getOperator().matches("[+\\-*/@]")) {
                        String message = "Invalid operator: " + request.getOperator();
                        logger.error(message);
                        return message;
                    } else {
                        int result;
                        switch (request.getOperator()) {
                            case "@":
                                result = CounterService.getCounter();
                                return "counter=" + result;
                            case "+":
                                result = num1 + num2;
                                return num1 + "+" + num2 + "=" + result;
                            case "-":
                                result = num1 - num2;
                                return num1 + "-" + num2 + "=" + result;
                            case "*":
                                result = num1 * num2;
                                return num1 + "*" + num2 + "=" + result;
                            case "/":
                                try {
                                    result = num1 / num2;
                                    return num1 + "/" + num2 + "=" + result;
                                } catch (ArithmeticException e) {
                                    String message = "Cannot divide by zero";
                                    logger.error(message, e);
                                    return message;
                                }
                            default:
                                return "Invalid operator: " + request.getOperator();
                        }
                    }
                })
                .collect(Collectors.toList());


        List<Integer> numericResults = results.stream()
                .filter(result -> result.matches(".*=\\d+"))
                .map(result -> {
                    String[] parts = result.split("=");
                    return Integer.parseInt(parts[1]);
                })
                .collect(Collectors.toList());

        int min = numericResults.stream().min(Integer::compareTo).orElse(0);
        int max = numericResults.stream().max(Integer::compareTo).orElse(0);
        int sum = numericResults.stream().mapToInt(Integer::intValue).sum();
        double average = numericResults.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        String response = "Result: " + results + "\nmin: " + min + "\nmax: " + max + "\nsum: " + sum + "\naverage: " + average;

        return ResponseEntity.ok(response);
    }

    public final NumberAsync numberAsync;

    @PostMapping("/async")
    public Integer async(@RequestBody ResponseEntiteData numbers) {
        int id = numberAsync.createAsync(numbers);

        numberAsync.computeAsync(id);

        return id;
    }

    @GetMapping("counter")
    public ResponseEntity<?> index()

    {
        return new ResponseEntity<>("counter= "+ CounterService.getCounter(),HttpStatus.OK);
    }
    @GetMapping("/result/{id}")
    public ResponseEntiteData result(@PathVariable("id")int id){
        return numberService.findOne(id);
    }
}