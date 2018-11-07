package com.megacode.models.response;

import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelTerminado;

import java.util.List;

public class ListarNivelesResponse {

    private List<Nivel> niveles;
    private List<NivelTerminado> nivelesTerminados;

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public List<NivelTerminado> getNivelesTerminados() {
        return nivelesTerminados;
    }

    public void setNivelesTerminados(List<NivelTerminado> nivelesTerminados) {
        this.nivelesTerminados = nivelesTerminados;
    }
}
