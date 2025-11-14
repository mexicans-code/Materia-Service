package com.idgs12.materias.materias.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.idgs12.materias.materias.dto.ProgramaEducativoDTO;

@FeignClient(name = "programaseducativos")
public interface ProgramaFeignClient {

    @GetMapping("/programas/{id}")
    ProgramaEducativoDTO getProgramaById(@PathVariable("id") Long id);

    @PostMapping("/programas/by-ids")
    List<ProgramaEducativoDTO> obtenerProgramasPorIds(@RequestBody List<Long> ids);
}