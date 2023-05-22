package com.example.demo.async;

import com.example.demo.Services.NumberService;
import com.example.demo.counter.CounterService;
import org.springframework.stereotype.Component;
import com.example.demo.entities.ResponseEntiteData;

import java.util.concurrent.CompletableFuture;

@Component
public class NumberAsync {

    private final NumberService numberService;

    public NumberAsync(NumberService numberService) {
        this.numberService = numberService;
    }

    public Integer createAsync(ResponseEntiteData numbers) {
        ResponseEntiteData numbers1 = new ResponseEntiteData();
        numbers1.setFirst(numbers.getFirst());
        numbers1.setSecond(numbers.getSecond());
        numbers1.setOperator(numbers.getOperator());

        numberService.save(numbers1);

        return numbers1.getId();
    }

    public void computeAsync(int id) {
        CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntiteData result = numberService.findOne(id);
                int num1= Integer.parseInt(result.getFirst());
                int num2= Integer.parseInt(result.getSecond());
                Thread.sleep(15000);
                int resul;
                switch (result.getOperator()) {
                    case "+":
                        resul = num1 + num2;
                        result.setResult(resul);
                        break;
                    case "-":
                        resul = num1 - num2;
                        result.setResult(resul);
                        break;
                    case "*":
                        resul = num1 * num2;
                        result.setResult(resul);
                        break;
                    case "/":
                            resul = num1 / num2;
                        result.setResult(resul);
                          break;
                    default:
                        return "Invalid operator: ";
                }


                numberService.save(result);

                return result.getId();

            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        });
    }

}
