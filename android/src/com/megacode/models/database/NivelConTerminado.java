package com.megacode.models.database;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class NivelConTerminado {
    @Embedded
    public Nivel nivel;

    @Relation(
            parentColumn = "id",
            entityColumn = "nivelId",
            entity = NivelTerminado.class
    )
    public List<NivelTerminado> nivelesTerminados;

    public static LinkedList<List<NivelConTerminado>> organizarPorNiveles(@NotNull List<NivelConTerminado> niveles){
        LinkedList<List<NivelConTerminado>> nivelesPorGrupo = new LinkedList<>();
        int grupoActual = 0;

        List<NivelConTerminado> nuevoGrupo=new ArrayList<>();

        for (NivelConTerminado nivelConTerminado : niveles) {
            Nivel nivel = nivelConTerminado.nivel;
            if (nivel.getGrupo() != grupoActual) {
                if (nuevoGrupo.size() > 0){
                    nivelesPorGrupo.add(nuevoGrupo);
                    nuevoGrupo = new ArrayList<>();
                }
                grupoActual = nivel.getGrupo();
            }
            nuevoGrupo.add(nivelConTerminado);
        }

        if (nuevoGrupo.size()>0)
            nivelesPorGrupo.add(nuevoGrupo);


        return nivelesPorGrupo;
    }
}
