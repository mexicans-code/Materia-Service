package com.idgs12.materias.materias.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idgs12.materias.materias.repository.MateriaRepository;
import com.idgs12.materias.materias.FeignClient.ProgramaFeignClient;
import com.idgs12.materias.materias.entity.MateriaEntity;
import com.idgs12.materias.materias.entity.ProgramaMateriaEntity;
import com.idgs12.materias.materias.dto.MateriaDTO;
import com.idgs12.materias.materias.dto.ProgramaEducativoDTO;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private ProgramaFeignClient programaFeignClient;

    // Crear materia

    // Listar todas las materias
    @Transactional
    public List<MateriaDTO> findAll() {
        List<MateriaEntity> materias = materiaRepository.findAll();

        List<Long> idsProgramas = materias.stream()
                .flatMap(m -> m.getProgramaMaterias().stream())
                .map(pm -> pm.getProgramaId())
                .distinct()
                .collect(Collectors.toList());

        List<ProgramaEducativoDTO> programas = programaFeignClient.obtenerProgramasPorIds(idsProgramas);

        Map<Long, ProgramaEducativoDTO> programaMap = new HashMap<>();
        if (programas != null) {
            for (ProgramaEducativoDTO p : programas) {
                programaMap.put(Long.valueOf(p.getId()), p);
            }
        }

        List<MateriaDTO> resultado = new ArrayList<>();
        for (MateriaEntity materia : materias) {
            MateriaDTO dto = new MateriaDTO();
            dto.setNombre(materia.getNombre());
            dto.setActivo(materia.isActivo());

            if (materia.getProgramaMaterias() != null && !materia.getProgramaMaterias().isEmpty()) {
                ProgramaMateriaEntity relacion = materia.getProgramaMaterias().get(0);
                dto.setProgramaId(relacion.getProgramaId());

                ProgramaEducativoDTO programa = programaMap.get(relacion.getProgramaId());
                if (programa != null) {
                    dto.setNombrePrograma(programa.getNombre());
                } else {
                    dto.setNombrePrograma("Sin programa");
                }
            }
            resultado.add(dto);
        }
        return resultado;
    }

    //  Obtener materia por ID (para Feign Client)
    public MateriaDTO findById(Integer id) {
        MateriaEntity materia = materiaRepository.findById(id).orElse(null);

        if (materia == null) {
            return null;
        }

        MateriaDTO dto = new MateriaDTO();
        dto.setId(materia.getId());
        dto.setNombre(materia.getNombre());
        dto.setActivo(materia.isActivo());

        if (materia.getProgramaMaterias() != null && !materia.getProgramaMaterias().isEmpty()) {
            ProgramaMateriaEntity relacion = materia.getProgramaMaterias().get(0);
            dto.setProgramaId(relacion.getProgramaId());

            try {
                ProgramaEducativoDTO programa = programaFeignClient.getProgramaById(relacion.getProgramaId());
                if (programa != null) {
                    dto.setNombrePrograma(programa.getNombre());
                }
            } catch (Exception e) {
                dto.setNombrePrograma("Sin programa");
            }
        }

        return dto;
    }

    // Funcionalidad de habilitar materia -Maria Fernanda Rosas Briones
    @Transactional
    public boolean habilitarMateria(int id) {
        MateriaEntity materia = materiaRepository.findById(id).orElse(null);

        if (materia == null) {
            return false;
        }

        materia.setActivo(true);
        materiaRepository.save(materia);

        return true;
    }

    // Funcionalidad de deshabilitar materia -Cecilia Mendoza Arteaga
    @Transactional
    public boolean deshabilitarMateria(int id) {
        MateriaEntity materia = materiaRepository.findById(id).orElse(null);

        if (materia == null) {
            return false;
        }

        materia.setActivo(false); // Cambiamos a false para deshabilitar
        materiaRepository.save(materia);

        return true;
    }

    // Funcionalidad editar-pedritobb
    @Transactional
    public MateriaDTO editarMateria(int id, MateriaDTO materiaDTO) {

        MateriaEntity materia = materiaRepository.findById(id).orElse(null);

        if (materia == null) {
            return null;
        }

        ProgramaEducativoDTO programa = programaFeignClient.getProgramaById(materiaDTO.getProgramaId());
        if (programa == null) {
            throw new RuntimeException("El programa educativo no existe");
        }

        materia.setNombre(materiaDTO.getNombre());

        materia.setActivo(materiaDTO.isActivo());

        if (!materia.getProgramaMaterias().isEmpty()) {
            materia.getProgramaMaterias().get(0).setProgramaId(materiaDTO.getProgramaId());
        }

        MateriaEntity materiaGuardada = materiaRepository.save(materia);

        MateriaDTO respuesta = new MateriaDTO();
        respuesta.setNombre(materiaGuardada.getNombre());
        respuesta.setActivo(materiaGuardada.isActivo());
        respuesta.setProgramaId(materiaDTO.getProgramaId());
        respuesta.setNombrePrograma(programa.getNombre());

        return respuesta;
    }
}