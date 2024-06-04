/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arbolesdebusqueda;

/**
 *
 * @author RodrigoRivero
 */
public class Contacto {
    private String apellido;
    private String telefono;
    private String dirección;
    private String email;

    public Contacto(String apellido, String telefono, String dirección, String email) {
        this.apellido = apellido;
        this.telefono = telefono;
        this.dirección = dirección;
        this.email = email;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString(){
        String datos = "";
        datos = datos + "Apellido: "+getApellido()+"\n";
        datos = datos + "Teléfono: "+getTelefono()+"\n";
        datos = datos + "Dirección: "+getDirección()+"\n";
        datos = datos + "Email: "+getEmail()+"\n";
        
        return datos;
    }
}
