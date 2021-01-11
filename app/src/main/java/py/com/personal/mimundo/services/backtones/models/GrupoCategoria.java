package py.com.personal.mimundo.services.backtones.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

public class GrupoCategoria implements Parcelable {

	private Integer idCategoria;
	private String descripcion;
	private byte[] icono;
	private Integer orden;
	private Set<Categoria> categorias;

	public GrupoCategoria() {
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte[] getIcono() {
		return icono;
	}

	public void setIcono(byte[] icono) {
		this.icono = icono;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}


    public static final Parcelable.Creator<GrupoCategoria> CREATOR = new Parcelable.Creator<GrupoCategoria>() {
        public GrupoCategoria createFromParcel(Parcel source) {
            GrupoCategoria grupoCategoria = new GrupoCategoria();
            grupoCategoria.idCategoria = source.readInt();
            grupoCategoria.descripcion = source.readString();
            grupoCategoria.icono = new byte[source.readInt()];
            source.readByteArray(grupoCategoria.icono);
            grupoCategoria.orden = source.readInt();
            return grupoCategoria;
        }
        public GrupoCategoria[] newArray(int size) {
            return new GrupoCategoria[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idCategoria);
        parcel.writeString(descripcion);
        parcel.writeInt(icono.length);
        parcel.writeByteArray(icono);
        parcel.writeInt(orden);
        //parcel.writeParcelableArray();
    }

}
