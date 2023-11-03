package org.example.domain.tarea;

import lombok.Data;
import org.example.domain.usuario.Usuario;

@Data
public class Tarea {
    private Long id;
    private String titulo;
    private String prioridad;
    private Long usuario_id;
    private Usuario usuario;
    private String categoria;
    private String descripcion;
}
