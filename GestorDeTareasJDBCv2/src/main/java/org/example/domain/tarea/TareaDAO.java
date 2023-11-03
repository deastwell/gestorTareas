package org.example.domain.tarea;

import java.util.ArrayList;

public interface TareaDAO {
    public Tarea load(Long id);
    public ArrayList<Tarea> loadAll();
    public ArrayList<Tarea> loadAllByResponsable(Long responsable);
    public Tarea save(Tarea t);
    public Tarea update(Tarea t);
    public void remove(Tarea t);
}
