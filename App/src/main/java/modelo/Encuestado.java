package modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rytscc on 14/07/2015.
 */
public class Encuestado implements Serializable {
    @SerializedName("cedula")
    private String cedula;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellido")
    private String apellido;
    @SerializedName("fecha")
    private String fechaNacimiento;

    public Encuestado() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
