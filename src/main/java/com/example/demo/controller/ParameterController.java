package com.example.demo.controller;

import com.example.demo.model.Parameter;
import com.example.demo.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameters")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    @GetMapping("/{id}")
    public Parameter getParameter(@PathVariable String id) {
        return parameterService.getParameterById(id);
    }
}
