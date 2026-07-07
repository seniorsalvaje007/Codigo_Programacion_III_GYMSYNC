package com.gymsync.model;

public class    Usuario {

    private final String correo;
    private final String contraseña;
    private final Rol rol;

    public Usuario (String correo, String contraseña, Rol rol) {
        this.correo = correo;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public String getCorreo() {return correo;}
    public String getContraseña() {return contraseña;}
    public Rol getRol() {return rol;}
}
