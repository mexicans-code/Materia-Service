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

    //Funcionalidad de habilitar --Maria Fernanda Rosas Briones- idgs12
    @PutMapping("/habilitar/{id}")
    public ResponseEntity<String> habilitarMateria(@PathVariable int id) {
        boolean habilitada = materiaService.habilitarMateria(id);

        if (habilitada) {
            return ResponseEntity.ok("Materia habilitada correctamente");
        } else {
            return ResponseEntity.badRequest().body("No se pudo habilitar la materia");
        }
    }

//Funcionalidad de deshabilitar - CeciliaMendoza Arteaga
    @PutMapping("/deshabilitar/{id}")
    public ResponseEntity<String> deshabilitarMateria(@PathVariable int id) {
        boolean deshabilitada = materiaService.deshabilitarMateria(id);

        if (deshabilitada) {
            return ResponseEntity.ok("Materia deshabilitada correctamente");
        } else {
            return ResponseEntity.badRequest().body("No se pudo deshabilitar la materia");
        }
    }
    //funcion de editar- pedrito bb
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarMateria(@PathVariable int id, @RequestBody MateriaDTO materiaDTO) {

        MateriaDTO materiaEditada = materiaService.editarMateria(id, materiaDTO);

        if (materiaEditada == null) {
            return ResponseEntity.badRequest().body("No se pudo actualizar la materia");
        }

        return ResponseEntity.ok(materiaEditada);
    }

}
