package com.jcondotta.web.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/")
public class GreetingController {

    @Get
    public String greet() {
        return "Hello World";
//        Mono<String> map = Mono.fromSupplier(this::performExpensiveCalculation)
//                .subscribeOn(Schedulers.boundedElastic())
//                .map(result -> {
//                    System.out.println("Thread Name: " + Thread.currentThread().getName());
//                    return "Hello, Reactive World! Result: " + result;
//                });
//
//        System.out.println(Thread.currentThread().getName() + " Tamo Aqui rapaziada");
//
//        return map;
    }
//
//    private int performExpensiveCalculation() {
//        int n = 30; // High enough to simulate delay
//        return fibonacci(n);
//    }
//
//    private int fibonacci(int n) {
//        if (n <= 1) {
//            return n;
//        }
////        System.out.println("N value: " + n + " - Thread Name: " + Thread.currentThread().getName());
//        return fibonacci(n - 1) + fibonacci(n - 2);
//    }
}