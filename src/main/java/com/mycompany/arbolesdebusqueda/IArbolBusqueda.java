/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.arbolesdebusqueda;

import java.util.List;

/**
 *
 * @author RodrigoRivero
 * @param <K>
 * @param <V>
 */
public interface IArbolBusqueda<K extends Comparable<K>, V>{
    void insertar(K clave, V valor);
    V eliminar(K clave);
    V buscar(K clave);
    boolean contiene(K clave);
    int size();
    int altura();
    void vaciar();
    boolean esArbolVacio();
    int nivel();
    List<K> recorridoEnInOrden();
    List<K> recorridoEnPreOrden();
    List<K> recorridoEnPostOrden();
    List<K> recorridoPorNiveles();
}
