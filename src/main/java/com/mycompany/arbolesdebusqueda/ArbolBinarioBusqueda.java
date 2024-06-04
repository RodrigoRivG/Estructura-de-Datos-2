/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.arbolesdebusqueda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author RodrigoRivero
 * @param <K>
 * @param <V>
 */
public class ArbolBinarioBusqueda<K extends Comparable<K>, V> implements 
        IArbolBusqueda<K, V>{
    
    protected NodoBinario<K, V> raiz;
    
    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if(claveAInsertar == null){
            throw new IllegalArgumentException("La clave no puede ser nula");
        }
        
        if(valorAsociado == null){
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
        
        if(this.esArbolVacio()){
            this.raiz = new NodoBinario<>(claveAInsertar, valorAsociado);
            return;
        }
        
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<K, V> nodoActual = this.raiz;
        
        do{
            K claveActual = nodoActual.getClave();
            int comparacion = claveAInsertar.compareTo(claveActual);
            nodoAnterior = nodoActual;
            if(comparacion < 0){
                nodoActual = nodoActual.getHijoIzquierdo();
            }else if(comparacion > 0){
                nodoActual = nodoActual.getHijoDerecho();
            }else{
                nodoActual.setValor(valorAsociado);
                return;
            }          
        }while(!NodoBinario.esNodoVacio(nodoActual));
        
        NodoBinario<K, V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAsociado);
        if(claveAInsertar.compareTo(nodoAnterior.getClave()) < 0){
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }else{
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
    }
    
    @Override
    public V eliminar(K claveAEliminar) {
        V valorARetornar = this.buscar(claveAEliminar);
        if(valorARetornar == null){
            return null;
        }
        
        this.raiz = eliminar(this.raiz, claveAEliminar);
        
        return valorARetornar;
    }
    
    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K claveAEliminar){
        K claveDelNodoActual = nodoActual.getClave();
        if(claveAEliminar.compareTo(claveDelNodoActual) < 0){
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = 
                    eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return nodoActual;
        }
        
        if(claveAEliminar.compareTo(claveDelNodoActual) > 0){
            NodoBinario<K, V> supuestoNuevoHijoDerecho = 
                    eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return nodoActual;
        }
        
        //caso 1: clave a eliminar es un nodo hoja
        if(nodoActual.esHoja()){
            return NodoBinario.nodoVacio();
        }
        
        //caso2A: clave a eliminar solo tiene hijo izquierdo
        if(!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()){
            return nodoActual.getHijoIzquierdo();
        }
        
        //caso2B: clave a eliminar solo tiene hijo derecho
        if(!nodoActual.esVacioHijoDerecho() && nodoActual.esVacioHijoIzquierdo()){
            return nodoActual.getHijoDerecho();
        }
        
        //caso3: clave a eliminar tiene ambos hijos
        NodoBinario<K, V> nodoDelSucesor = this.getSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K, V> supuestoNuevoHijoDerecho = 
                eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        
        nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
        return nodoActual;
    }
    
    protected NodoBinario<K, V> getSucesor(NodoBinario<K, V> nodoAux){
        while(!nodoAux.esVacioHijoIzquierdo()){
            nodoAux = nodoAux.getHijoIzquierdo();
        }
        
        return nodoAux;
    }
    
    /*
    @Override
    public V eliminar(K claveAEliminar){
        V valorARetornar = this.buscar(claveAEliminar);
        if(valorARetornar == null)
            return null;
        
        NodoBinario<K, V> nodoAnterior, nodoActual;
        nodoAnterior = NodoBinario.nodoVacio();
        nodoActual = this.raiz;
        K claveActual = nodoActual.getClave();
        while(!NodoBinario.esNodoVacio(nodoActual) && !claveActual.equals(claveAEliminar)){
            nodoAnterior = nodoActual;
            if(claveAEliminar.compareTo(claveActual) < 0){
                nodoActual = nodoActual.getHijoIzquierdo();
            }else{
                nodoActual = nodoActual.getHijoDerecho();
            }
            claveActual = nodoActual.getClave();
        }
        
        if(nodoActual.esHoja()){
            eliminarCaso1(nodoAnterior, nodoActual);
        }else{
            //eliminar caso2A: hijo izquierdo distinto de vacio y derecho vacio
            if(!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()){
                eliminarCaso2A(nodoAnterior, nodoActual);
                //caso 2B: hijo derecho distinto de vacio, hijo izquierdo vacio
            }else if(!nodoActual.esVacioHijoDerecho() && nodoActual.esVacioHijoIzquierdo()){
                eliminarCaso2B(nodoAnterior, nodoActual);
            }else{
                //ambos hijos no vacios
                eliminarCaso3(nodoActual);
            }
        }
        
        return valorARetornar;
    }
    
    private void eliminarCaso1(NodoBinario<K,V> nodoAnterior, NodoBinario<K,V> nodoActual){
        if(NodoBinario.esNodoVacio(nodoAnterior)){
            this.raiz = NodoBinario.nodoVacio();
            return;
        }
        
        K claveActual = nodoActual.getClave();
        K claveAnterior = nodoAnterior.getClave();
        if(claveActual.compareTo(claveAnterior) < 0){
            nodoAnterior.setHijoIzquierdo(NodoBinario.nodoVacio());
        }else{
            nodoAnterior.setHijoDerecho(NodoBinario.nodoVacio());
        }
    }
    
    private void eliminarCaso2A(NodoBinario<K,V> nodoAnterior, NodoBinario<K,V> nodoActual){
        if(NodoBinario.esNodoVacio(nodoAnterior)){
            this.raiz = nodoActual.getHijoIzquierdo();
            return;
        }
        
        NodoBinario<K, V> supuestoNuevoHijo = nodoActual.getHijoIzquierdo();
        K claveActual = nodoActual.getClave();
        K claveAnterior = nodoAnterior.getClave();
        if(claveActual.compareTo(claveAnterior) < 0){
            nodoAnterior.setHijoIzquierdo(supuestoNuevoHijo);
        }else{
            nodoAnterior.setHijoDerecho(supuestoNuevoHijo);
        }
        //nodoAnterior.setHijoIzquierdo(supuestoNuevoHijo);
    }
    
    private void eliminarCaso2B(NodoBinario<K,V> nodoAnterior, NodoBinario<K,V> nodoActual){
        if(NodoBinario.esNodoVacio(nodoAnterior)){
            this.raiz = nodoActual.getHijoDerecho();
            return;
        }
        
        NodoBinario<K, V> supuestoNuevoHijo = nodoActual.getHijoDerecho();
        K claveActual = nodoActual.getClave();
        K claveAnterior = nodoAnterior.getClave();
        if(claveActual.compareTo(claveAnterior) < 0){
            nodoAnterior.setHijoIzquierdo(supuestoNuevoHijo);
        }else{
            nodoAnterior.setHijoDerecho(supuestoNuevoHijo);
        }
        //nodoAnterior.setHijoDerecho(supuestoNuevoHijoDer);
    }
    
    private void eliminarCaso3(NodoBinario<K,V> nodoActual){
        NodoBinario<K, V> nodoAnterior = nodoActual;
        NodoBinario<K, V> nodoAux = nodoActual.getHijoDerecho();
        //NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        while(!nodoAux.esVacioHijoIzquierdo()){
            nodoAnterior = nodoAux;
            nodoAux = nodoAux.getHijoIzquierdo();
        }
        
        nodoActual.setClave(nodoAux.getClave());
        nodoActual.setValor(nodoAux.getValor());
        if(nodoAux.esHoja()){
            eliminarCaso1(nodoAnterior, nodoAux);
        }else if(!nodoAux.esVacioHijoIzquierdo() && nodoAux.esVacioHijoDerecho()){
            eliminarCaso2A(nodoAnterior, nodoAux);
        }else{
            eliminarCaso2B(nodoAnterior, nodoAux);
        }
    }*/

    @Override
    public V buscar(K claveABuscar) {
        if(claveABuscar == null){
            throw new IllegalArgumentException("No se puede buscar claves nulas");
        }
        
        if(this.esArbolVacio()){
            return null;
        }
        
        NodoBinario<K, V> nodoActual = this.raiz; //se soluciona con un if (si raiz es null o árbol vacio)
        do{
            K claveDelNodoActual = nodoActual.getClave();
            if(claveABuscar.compareTo(claveDelNodoActual) == 0){
                return nodoActual.getValor();
            }
            if(claveABuscar.compareTo(claveDelNodoActual) < 0){
               nodoActual = nodoActual.getHijoIzquierdo();
            }else{
                nodoActual = nodoActual.getHijoDerecho();
            }
        }while(!NodoBinario.esNodoVacio(nodoActual));
        
        return null;
    }

    @Override
    public boolean contiene(K claveABuscar) {
        if(claveABuscar == null){
            throw new IllegalArgumentException("No se puede buscar claves nulas");
        }
        
        NodoBinario<K, V> nodoActual = this.raiz;
        do{
            K claveDelNodoActual = nodoActual.getClave();
            if(claveABuscar.compareTo(claveDelNodoActual) == 0){
                return true;
            }
            if(claveABuscar.compareTo(claveDelNodoActual) < 0){
               nodoActual = nodoActual.getHijoIzquierdo();
            }else{
                nodoActual = nodoActual.getHijoDerecho();
            }
        }while(!NodoBinario.esNodoVacio(nodoActual));
        
        return false;
    }

    @Override
    public int size() {
        int contador = 0;
        if(!this.esArbolVacio()){
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            pilaDeNodos.push(raiz);
            do{
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                contador++;
                if(!nodoActual.esVacioHijoDerecho()){
                    pilaDeNodos.push(nodoActual.getHijoDerecho());
                }
                
                if(!nodoActual.esVacioHijoIzquierdo()){
                    pilaDeNodos.push(nodoActual.getHijoIzquierdo());
                }
            }while(!pilaDeNodos.isEmpty());
        }
        
        return contador;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }
    
    protected int altura(NodoBinario<K, V> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        
        int alturaIzq = altura(nodoActual.getHijoIzquierdo());
        int alturaDer = altura(nodoActual.getHijoDerecho());
        if(alturaIzq > alturaDer){
            return alturaIzq + 1;
        }else{
            return alturaDer + 1;
        }
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return this.altura() - 1;
    }
    
    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        if(!this.esArbolVacio()){
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            meterEnInOrden(pilaDeNodos, this.raiz);
            do{
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getClave());
                if(!nodoActual.esVacioHijoDerecho()){
                    meterEnInOrden(pilaDeNodos, nodoActual.getHijoDerecho());
                } 
            }while(!pilaDeNodos.isEmpty());           
        }
        
        return recorrido;
    }
    
    private void meterEnInOrden(Stack<NodoBinario<K, V>> pila, NodoBinario<K, V> nodo){
        while(!NodoBinario.esNodoVacio(nodo)){
            pila.push(nodo);
            if(!nodo.esVacioHijoIzquierdo()){
                nodo = nodo.getHijoIzquierdo();
            }else
                nodo = NodoBinario.nodoVacio();
        }
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        if(!this.esArbolVacio()){
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            pilaDeNodos.push(raiz);
            do{
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getClave());
                if(!nodoActual.esVacioHijoDerecho()){
                    pilaDeNodos.push(nodoActual.getHijoDerecho());
                }
                
                if(!nodoActual.esVacioHijoIzquierdo()){
                    pilaDeNodos.push(nodoActual.getHijoIzquierdo());
                }
            }while(!pilaDeNodos.isEmpty());
        }
        
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
        if(!this.esArbolVacio()){
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            meterEnPostOrden(pilaDeNodos, this.raiz);
            do{
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getClave());
                if(!pilaDeNodos.isEmpty()){
                    NodoBinario<K, V> nodoDelTope = pilaDeNodos.peek();
                    if(nodoDelTope.getHijoIzquierdo() == nodoActual){
                        meterEnPostOrden(pilaDeNodos, nodoDelTope.getHijoDerecho());
                    }
                }
            }while(!pilaDeNodos.isEmpty());
        }
        
        return recorrido;
    }
    
    private void meterEnPostOrden(Stack<NodoBinario<K, V>> pila, NodoBinario<K, V> nodo){
        while(!NodoBinario.esNodoVacio(nodo)){
            pila.push(nodo);
            if(!nodo.esVacioHijoIzquierdo()){
                nodo = nodo.getHijoIzquierdo();
            }else{
                nodo = nodo.getHijoDerecho();
            }         
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if(this.esArbolVacio()){
            return recorrido;
        }
        
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        do{
            NodoBinario<K, V> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if(!nodoActual.esVacioHijoIzquierdo()){
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            
            if(!nodoActual.esVacioHijoDerecho()){
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }while(!colaDeNodos.isEmpty());
        
        return recorrido;
    } 
    
    
    public static void main(String[] args) {
        /*ArbolBinarioBusqueda<Integer, String> arbol = new ArbolBinarioBusqueda();
        arbol.insertar(50, "Rodrigo");
        arbol.insertar(30, "rojo");
        arbol.insertar(90, "azul");
        arbol.insertar(10, "amarillo");
        arbol.insertar(75, "verde");
        arbol.insertar(80, "cafe");
        arbol.insertar(40, "dsasdasd");
        arbol.insertar(95, "asasa");
        arbol.insertar(35, "wweww");   */
       ArbolBinarioBusqueda<String, Contacto> arbol = new ArbolBinarioBusqueda();
        Contacto contacto = 
                new Contacto("", "78474831", "", "");
        arbol.insertar("Rodrigo", contacto);
        arbol.insertar("Rodrigo", new Contacto("", "60853026", "", ""));
        
        System.out.println(arbol.buscar("Rodrigo"));
       /*ArbolBinarioBusqueda<String, String> arbol = new ArbolBinarioBusqueda();
       arbol.insertar("Libro", "Conjunto de hojas");
       System.out.println(arbol.buscar("Libro"));*/
    }

}
