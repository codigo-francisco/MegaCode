package com.megacode.models.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.megacode.R;
import com.megacode.adapters.model.enumators.TypeLevel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

import static com.megacode.adapters.model.enumators.TypeLevel.*;

public class NivelConTerminado implements Parcelable {
    @Embedded
    public Nivel nivel;

    @Relation(
            parentColumn = "id",
            entityColumn = "nivelId",
            entity = NivelTerminado.class
    )
    public List<NivelTerminado> nivelesTerminados;

    public NivelConTerminado() {
    }

    public int getImageResource(boolean bloqueado){
        Integer imageResource = null;

        TypeLevel typeLevel = nivel.getTypeLevel();
        boolean isTerminado = !nivelesTerminados.isEmpty() && nivelesTerminados.get(0).isTerminado();

        if (!bloqueado) {
            switch (typeLevel) {
                case COMANDO:
                    imageResource = R.drawable.ic_c;
                    break;
                case SI:
                    imageResource = R.drawable.ic_s;
                    break;
                case PARA:
                    imageResource = R.drawable.ic_p;
                    break;
                case MIENTRAS:
                    imageResource = R.drawable.ic_m;
                    break;
                default:
                    imageResource = R.drawable.megacode;
                    break;
            }
        }else{
            switch (typeLevel) {
                case COMANDO:
                    imageResource = R.drawable.ic_c_gray;
                    break;
                case SI:
                    imageResource = R.drawable.ic_s_gray;
                    break;
                case PARA:
                    imageResource = R.drawable.ic_p_gray;
                    break;
                case MIENTRAS:
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
