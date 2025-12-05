package com.idgs12.materias.materias.dto;

import lombok.Data;

@Data
public class MateriaDTO {
    private Integer id;
    private Long programaId;
    private String nombre;
    private boolean activo;
    private String nombrePrograma;
}
