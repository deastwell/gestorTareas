package org.example.domain.usuario;

import lombok.*;
import org.example.domain.tarea.Tarea;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {
    @NonNull Long id;
    @NonNull  private String nombre;
    @NonNull private String email;
    private ArrayList<Tarea> tareas = new ArrayList<>(0);
}
