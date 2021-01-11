package py.com.personal.mimundo.services.backtones.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Categoria implements Parcelable {

	private Integer idCategoria;
	private String descripcion;

	public Categoria() {
	}

	public Categoria(Integer idCategoria, String descripcion) {
		this.idCategoria = idCategoria;
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

    public static final Parcelable.Creator<Categoria> CREATOR = new Creator<Categoria>() {
        public Categoria createFromParcel(Parcel source) {
            Categoria categoria = new Categoria();
            categoria.idCategoria = source.readInt();
            categoria.descripcion = source.readString();
            return categoria;
        }
        public Categoria[] newArray(int size) {
            return new Categoria[size];
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
    }
}
