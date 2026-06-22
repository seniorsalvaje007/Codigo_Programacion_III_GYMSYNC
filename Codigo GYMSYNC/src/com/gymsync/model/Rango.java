package com.gymsync.model;

public enum Rango {
    NEUTRO(0),
    BRONCE(1000),
    PLATA(5000),
    PLATINO(10000),
    DIAMANTE(20000),
    GIGACHAD(100000);

    public final int puntosRequeridos;
    Rango(int puntos) {this.puntosRequeridos = puntos;}
    public int getPuntosRequeridos() {return this.puntosRequeridos;}
}
