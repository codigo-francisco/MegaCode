package com.rockbass2560.megacode.models.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.rockbass2560.megacode.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NivelConTerminado implements Parcelable {
    public Nivel nivel;

    public List<NivelTerminado> nivelesTerminados;

    public NivelConTerminado() {
    }

    public int getImageResource(boolean bloqueado){
        Integer imageResource = null;

        int typeLevel = nivel.tipoNivel;
        boolean isTerminado = !nivelesTerminados.isEmpty() && nivelesTerminados.get(0).terminado;

        if (!bloqueado) {
            switch (typeLevel) {
                case 1:
                    imageResource = R.drawable.ic_c;
                    break;
                case 2:
                    imageResource = R.drawable.ic_s;
                    break;
                case 3:
                    imageResource = R.drawable.ic_p;
                    break;
                case 4:
                    imageResource = R.drawable.ic_m;
                    break;
                default:
                    imageResource = R.drawable.megacode;
                    break;
            }
        }else{
            switch (typeLevel) {
                case 1:
                    imageResource = R.drawable.ic_c_gray;
                    break;
                case 2:
                    imageResource = R.drawable.ic_s_gray;
                    break;
                case 3:
                    imageResource = R.drawable.ic_p_gray;
                    break;
                case 4:
                    imageResource = R.drawable.ic_m_gray;
                    break;
                default:
                    imageResource = R.drawable.megacode;
                    break;
            }
        }

        return imageResource;
    }

    public static LinkedList<List<NivelConTerminado>> organizarPorNiveles(@NotNull List<NivelConTerminado> niveles){
        LinkedList<List<NivelConTerminado>> nivelesPorGrupo = new LinkedList<>();
        int grupoActual = 0;

        List<NivelConTerminado> nuevoGrupo=new ArrayList<>();

        for (NivelConTerminado nivelConTerminado : niveles) {
            Nivel nivel = nivelConTerminado.nivel;
            if (nivel.grupo != grupoActual) {
                if (nuevoGrupo.size() > 0){
                    nivelesPorGrupo.add(nuevoGrupo);
                    nuevoGrupo = new ArrayList<>();
                }
                grupoActual = nivel.grupo;
            }
            nuevoGrupo.add(nivelConTerminado);
        }

        if (nuevoGrupo.size()>0)
            nivelesPorGrupo.add(nuevoGrupo);


        return nivelesPorGrupo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.nivel, flags);
        dest.writeList(this.nivelesTerminados);
    }

    protected NivelConTerminado(Parcel in) {
        this.nivel = in.readParcelable(Nivel.class.getClassLoader());
        this.nivelesTerminados = new ArrayList<NivelTerminado>();
        in.readList(this.nivelesTerminados, NivelTerminado.class.getClassLoader());
    }

    public static final Parcelable.Creator<NivelConTerminado> CREATOR = new Parcelable.Creator<NivelConTerminado>() {
        @Override
        public NivelConTerminado createFromParcel(Parcel source) {
            return new NivelConTerminado(source);
        }

        @Override
        public NivelConTerminado[] newArray(int size) {
            return new NivelConTerminado[size];
        }
    };
}
