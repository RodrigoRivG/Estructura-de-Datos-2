/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arbolesdebusqueda;

import Excepciones.OrdenInvalidoExcepcion;

/**
 *
 * @author RodrigoRivero
 */
public class Arboles {
    public static void main(String arg[]) throws OrdenInvalidoExcepcion{
        /*ArbolBinarioBusqueda<Integer, String> binario = new ArbolBinarioBusqueda<>();
        binario.insertar(50, "rojo");
        binario.insertar(30, "azul");
        binario.insertar(90, "amarillo");
        binario.insertar(75, "verde");
        binario.insertar(80, "cafe");
        binario.insertar(10, "abc");
        System.out.println("Recorrido: "+binario.recorridoPorNiveles());
        binario.eliminar(50);
        System.out.println("Recorrido: "+binario.recorridoPorNiveles());*/
        
        ArbolMViasBusqueda<Integer, String> mvias = new ArbolMViasBusqueda(4);
        mvias.insertar(50, "rojo");
        mvias.insertar(70, "azul");
        mvias.insertar(90, "amarillo");
        mvias.insertar(75, "verde");
        mvias.insertar(80, "cafe");
        mvias.insertar(10, "abc");
        mvias.insertar(20, "def");
        mvias.insertar(30, "ghi");
        mvias.insertar(15, "abcbc");
        mvias.insertar(95, "xhasa");
        mvias.insertar(110, "rdasdojo");
        mvias.insertar(115, "roewrjo");
        mvias.insertar(125, "yert");
        mvias.insertar(35, "ghity");
        System.out.println(mvias.contarHNV(2));
        //System.out.println("ClavesVaciasEnNivel: "+mvias.clavesVaciasEnNivel(2));
        //System.out.println("ContarClavesVacias: "+mvias.contarClavesVacias(2));
        //System.out.println("Cant: "+mvias.clavesVacias());
        //System.out.println("Buscar 35: "+mvias.buscar(35));
        //System.out.println("Cantidad de claves vacias antes del nivel 3: "+mvias.contarClavesVacias(3));
        /*System.out.println("Recorrido por Niveles: "+mvias.recorridoPorNiveles());
        System.out.println("Recorrido en InOden: "+mvias.recorridoEnInOrden());
        System.out.println("Recorrido en PreOrden: "+mvias.recorridoEnPreOrden());
        System.out.println("Recorrido en PostOrden: "+mvias.recorridoEnPostOrden());
        System.out.println("Cantidad de hijos vacios: "+mvias.cantidadDeHijosVacios());
        System.out.println("Cantidad de hijos vacios hasta 0: "+mvias.contarHijosVaciosHastaN(0));
        System.out.println("Size del arbol: "+mvias.size());
        System.out.println("Nivel: "+mvias.nivel()); 
        System.out.println("Buscar: "+mvias.buscar(300));*/
    }  
}
