package com.enquete.controller;

import com.enquete.model.Crime;
import com.enquete.service.CrimeServiceIA;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crime")
@CrossOrigin(origins = "*")

public class CrimeController {
    
    @GetMapping
    public Crime genererCrime() throws Exception{
        return CrimeServiceIA.genererCrimeParIA();
    }
}
