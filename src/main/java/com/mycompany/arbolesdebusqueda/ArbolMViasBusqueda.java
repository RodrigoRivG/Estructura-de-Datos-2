/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arbolesdebusqueda;

import Excepciones.OrdenInvalidoExcepcion;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author RodrigoRivero
 * @param <K>
 * @param <V>
 */
public class ArbolMViasBusqueda<K extends Comparable<K>, V> implements
        IArbolBusqueda<K, V> {

    protected NodoMVias<K, V> raiz;
    protected int orden;
    protected static final int ORDEN_MINIMO = 3;
    protected static final int POSICION_INVALIDA = -1;

    public ArbolMViasBusqueda() {
        this.orden = ArbolMViasBusqueda.ORDEN_MINIMO;
    }

    public ArbolMViasBusqueda(int orden) throws OrdenInvalidoExcepcion {
        if (orden < ArbolMViasBusqueda.ORDEN_MINIMO) {
            throw new OrdenInvalidoExcepcion();
        }
        this.orden = orden;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("No se permiten claves nulas");
        }

        if (valorAsociado == null) {
            throw new IllegalArgumentException("No se permiten valore nulos");
        }

        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
            return;
        }

        NodoMVias<K, V> nodoActual = this.raiz;

        do {
            int posicionDeClaveAInsertar = this.buscarPosicionDeClave(claveAInsertar, nodoActual);
            if (posicionDeClaveAInsertar != ArbolMViasBusqueda.POSICION_INVALIDA) {
                nodoActual.setValor(posicionDeClaveAInsertar, valorAsociado);
                nodoActual = NodoMVias.nodoVacio();
            } else if (nodoActual.esHoja()) {
                if (nodoActual.clavesLlenas()) {
                    int posicionPorDondeBajar = this.buscarPosicionPorDondeBajar(claveAInsertar, nodoActual);
                    NodoMVias<K, V> nuevoNodo = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoNodo);
                } else {
                    this.insertarClaveYValorOrdenado(nodoActual, claveAInsertar, valorAsociado);
                }

                nodoActual = NodoMVias.nodoVacio();
            } else {
                //El nodo actual no es hoja
                int posicionPorDondeBajar = this.buscarPosicionPorDondeBajar(claveAInsertar, nodoActual);
                if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                    NodoMVias<K, V> nuevoNodo = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoNodo);
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }

        } while (!NodoMVias.esNodoVacio(nodoActual));
    }

    //cantidad de hijos vacios en todo el arbol y cantidad de hijos vacios hasta el nivel n
    protected int buscarPosicionDeClave(K claveAInsertar, NodoMVias<K, V> nodoActual) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            if (claveAInsertar.compareTo(nodoActual.getClave(i)) == 0) {
                return i;
            }
        }
        return -1;
    }

    protected int buscarPosicionPorDondeBajar(K claveAInsertar, NodoMVias<K, V> nodoActual) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            if (claveAInsertar.compareTo(nodoActual.getClave(i)) < 0) {
                return i;
            }
        }
        return nodoActual.nroDeClavesNoVacias();
    }

    protected void insertarClaveYValorOrdenado(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAsociado) {
        int pos = -1;
        for (int i = nodoActual.nroDeClavesNoVacias() - 1; i >= 0; i--) {
            if (claveAInsertar.compareTo(nodoActual.getClave(i)) < 0) {
                nodoActual.setClave(i + 1, nodoActual.getClave(i));
            } else {
                pos = i;
                break;
            }
        }
        nodoActual.setClave(pos + 1, claveAInsertar);
        nodoActual.setValor(pos + 1, valorAsociado);
    }

    @Override
    public V eliminar(K claveAEliminar) {
        V valorARetornar = this.buscar(claveAEliminar);
        if (valorARetornar == null) {
            return null;
        }

        this.raiz = eliminar(this.raiz, claveAEliminar);

        return valorARetornar;
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveEnTurno = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveEnTurno) == 0) {
                if (nodoActual.esHoja()) {
                    this.eliminarClaveDeNodoDePosicion(nodoActual, i);
                    if (!nodoActual.hayClavesNoVacias()) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }

                K claveDeReemplazo;
                if (this.hayHijosMasAdelanteDeLaPosicion(nodoActual, i + 1)) {
                    //Caso 2
                    claveDeReemplazo = this.obtenerSucesorInOrden(nodoActual, claveAEliminar);
                } else {
                    //Caso 3
                    claveDeReemplazo = this.obtenerPredecesorInOrden(nodoActual, claveAEliminar);
                }

                V valorDelReemplazo = this.buscar(claveDeReemplazo);

                nodoActual = eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorDelReemplazo);
                return nodoActual;
            }

            if (claveAEliminar.compareTo(claveEnTurno) < 0) {
                NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()),
                claveAEliminar);
        nodoActual.setHijo(nodoActual.nroDeClavesNoVacias(), supuestoNuevoHijo);
        return nodoActual;
    }

    private void eliminarClaveDeNodoDePosicion(NodoMVias<K, V> nodoActual, int posicion) {
        /*if (posicion == nodoActual.nroDeClavesNoVacias() - 1) {
            K claveVacia = (K) NodoMVias.datoVacio();
            V valorVacio = (V) NodoMVias.datoVacio();
            nodoActual.setClave(posicion, claveVacia);
            nodoActual.setValor(posicion, valorVacio);
        } else {
            for (int i = posicion; i <= nodoActual.nroDeClavesNoVacias() - 1; i++) { //antes -1
                nodoActual.setClave(i, nodoActual.getClave(i + 1));
                nodoActual.setValor(i, nodoActual.getValor(i + 1));
                if(){
                }
            }
        }*/
        
        for (int i = posicion; i < nodoActual.nroDeClavesNoVacias(); i++) {
            if(i == nodoActual.nroDeClavesNoVacias() - 1){
                K claveVacia = (K) NodoMVias.datoVacio();
                V valorVacio = (V) NodoMVias.datoVacio();
                nodoActual.setClave(i, claveVacia);
                nodoActual.setValor(i, valorVacio);
            }else{
                nodoActual.setClave(i, nodoActual.getClave(i + 1));
                nodoActual.setValor(i, nodoActual.getValor(i + 1));
            }
        }
    }

    private boolean hayHijosMasAdelanteDeLaPosicion(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion; i < this.orden; i++) {
            if (!nodoActual.esHijoVacio(i)) {
                return true;
            }
        }
        return false;
    }

    private K obtenerSucesorInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        List<K> listaDeClavesInOrden = this.recorridoEnInOrden();
        K claveDeRetorno = (K) NodoMVias.datoVacio();
        for (int i = 0; i < listaDeClavesInOrden.size(); i++) {
            if (claveAEliminar.compareTo(listaDeClavesInOrden.get(i)) == 0) {
                claveDeRetorno = listaDeClavesInOrden.get(i + 1);
                break;
            }
        }
        return claveDeRetorno;
    }

    private K obtenerPredecesorInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        List<K> listaDeClavesInOrden = this.recorridoEnInOrden();
        K claveDeRetorno = (K) NodoMVias.datoVacio();
        for (int i = 0; i < listaDeClavesInOrden.size(); i++) {
            if (claveAEliminar.compareTo(listaDeClavesInOrden.get(i)) == 0) {
                claveDeRetorno = listaDeClavesInOrden.get(i - 1);
                break;
            }
        }
        return claveDeRetorno;
    }

    @Override
    public V buscar(K claveABuscar) {
        if (claveABuscar == null) {
            throw new IllegalArgumentException("No se puede buscar claves nulas");
        }
        
        if (this.esArbolVacio()){
            return null;
        }

        NodoMVias<K, V> nodoActual = this.raiz;
        do {
            boolean cambio = false;
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias() && !cambio; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                }

                if (claveABuscar.compareTo(claveActual) < 0) {
                    nodoActual = nodoActual.getHijo(i);
                    cambio = true;
                }
            }
            if (!cambio) {
                nodoActual = nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
            }

        } while (!NodoMVias.esNodoVacio(nodoActual));
        return null;
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != NodoMVias.datoVacio();
    }

    @Override
    public int size() {
        return size(this.raiz);
    }

    protected int size(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cant = 0;
        for (int i = 0; i < this.orden; i++) {
            cant += size(nodoActual.getHijo(i));
        }

        if (!NodoMVias.esNodoVacio(nodoActual)) {
            cant++;
        }

        return cant;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    protected int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int alturaMayor = 0;
        for (int i = 0; i < this.orden; i++) {
            int alturaDelHijoActual = altura(nodoActual.getHijo(i));
            if (alturaDelHijoActual > alturaMayor) {
                alturaMayor = alturaDelHijoActual;
            }
        }
        return alturaMayor + 1;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public int nivel() {
        return nivel(this.raiz) - 1;
    }

    protected int nivel(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return - 1;
        }

        int nivelMayor = 0;
        for (int i = 0; i < this.orden; i++) {
            int nivelActual = nivel(nodoActual.getHijo(i));
            if (nivelActual > nivelMayor) {
                nivelMayor = nivelActual;
            }
        }

        return nivelMayor + 1;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoEnInOrden(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), recorrido);
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnPreOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPreOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        recorridoEnPreOrden(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), recorrido);
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        do {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                recorrido.add(nodoActual.getClave(i));

                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }

            if (!nodoActual.esHijoVacio(nodoActual.nroDeClavesNoVacias())) {
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
            }
        } while (!colaDeNodos.isEmpty());

        return recorrido;
    }

    // ----------------------------------------------------------------
    // cantidad de hijos vacios en todo el arbol
    public int cantidadDeHijosVacios() {
        return cantidadDeHijosVacios(this.raiz);
    }

    private int cantidadDeHijosVacios(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidad = 0;
        for (int i = 0; i <= this.orden - 1; i++) {
            cantidad += cantidadDeHijosVacios(nodoActual.getHijo(i));
            if (nodoActual.esHijoVacio(i)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    // --------------------- EJERCICIOS --------------------------------
    //Contar hijos vacios hasta el nivel n
    public int contarHijosVaciosHastaN(int n) {
        int cantidad = 0;
        if (this.esArbolVacio()) {
            return cantidad;
        }

        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        int nivelActual = 0;
        do {
            int nodosDelNivel = colaDeNodos.size();

            while (nodosDelNivel > 0) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                for (int i = 0; i < this.orden; i++) {
                    if (nivelActual <= n) {
                        if (nodoActual.esHijoVacio(i)) {
                            cantidad++;
                        }
                    }

                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }
                nodosDelNivel--;

            }

            nivelActual++;

        } while (!colaDeNodos.isEmpty());

        return cantidad;
    }

    //CONTAR LAS CLAVES VACIAS HASTA N
    public int contarClavesVacias(int nivel) {
        return contarClavesVacias(this.raiz, nivel, 0);
    }

    private int contarClavesVacias(NodoMVias<K, V> nodoActual, int nivel, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cant = contarClavesVacias(nodoActual.getHijo(0), nivel, nivelActual + 1);
        for (int i = 0; i < this.orden - 1; i++) {
            cant += contarClavesVacias(nodoActual.getHijo(i + 1), nivel, nivelActual + 1);
            if (nivelActual < nivel) {
                /*K claveActual = nodoActual.getClave(i);
                if(claveActual == NodoMVias.datoVacio()){
                    cant++;
                }*/
                if (nodoActual.esDatoVacio(i)) {
                    cant++;
                }
            }
        }

        return cant;
    }

    public int clavesVacias() {
        return clavesVacias(this.raiz);
    }

    private int clavesVacias(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidad = 0;
        for (int i = 0; i < this.orden; i++) {
            cantidad += clavesVacias(nodoActual.getHijo(i));
        }

        for (int i = 0; i < this.orden - 1; i++) {
            if (nodoActual.esDatoVacio(i)) {
                cantidad++;
            }
        }

        return cantidad;
    }
    
    public int clavesVaciasEnNivel(int nivel) {
        return clavesVaciasEnNivel(this.raiz, nivel, 0);
    }

    private int clavesVaciasEnNivel(NodoMVias<K, V> nodoActual, int nivel, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidad = 0;
        for (int i = 0; i < this.orden; i++) {
            cantidad += clavesVaciasEnNivel(nodoActual.getHijo(i), nivel, nivelActual+1);
        }
        
        if(nivelActual < nivel){
            for (int i = 0; i < this.orden - 1; i++) {
                if (nodoActual.esDatoVacio(i)) {
                    cantidad++;
                }
            }
        }
        
        return cantidad;
    }
    
    public int contarHNV(int nivel){
        return contarHNV(this.raiz, nivel, 0);
    }

    private int contarHNV(NodoMVias<K,V> nodoActual, int nivel, int nivelActual){
        if(NodoMVias.esNodoVacio(nodoActual)){
            return 0;
        }

        int cantidad = 0;
        for(int i = 0; i < this.orden; i++){
            cantidad += contarHNV(nodoActual.getHijo(i), nivel, nivelActual+1);
        }

        if(nivel > nivelActual){
            for(int i = 0; i < this.orden; i++){
                if(!nodoActual.esHijoVacio(i)){
                    cantidad++;
                }
            }
        }

        return cantidad;
    }
}
