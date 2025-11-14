package com.idgs12.materias.materias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.idgs12.materias.materias.dto.MateriaDTO;
import com.idgs12.materias.materias.services.MateriaService;

@RestController
@RequestMapping("/materias")
@CrossOrigin(origins = "*")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping("/all")
    public ResponseEntity<List<MateriaDTO>> findAll() {
        List<MateriaDTO> materias = materiaService.findAll();
        return ResponseEntity.ok(materias);
    }
    
}
