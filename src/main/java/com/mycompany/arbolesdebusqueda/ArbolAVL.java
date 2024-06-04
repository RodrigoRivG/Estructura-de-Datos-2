/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arbolesdebusqueda;

/**
 *
 * @author RodrigoRivero
 * @param <K>
 * @param <V>
 */
public class ArbolAVL<K extends Comparable<K>, V> extends ArbolBinarioBusqueda<K, V>{
    public static final byte LIMITE_MAXIMO = 1;
    
    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        
        if(claveAInsertar == null){
            throw new IllegalArgumentException("La clave no puede ser nula");
        }
        if (valorAsociado == null){
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }

        super.raiz = insertar(super.raiz, claveAInsertar, valorAsociado);
    }

    private NodoBinario<K, V> insertar(NodoBinario<K,V> nodoActual,
                                       K claveAInsertar, V valorAsociado){
        if(NodoBinario.esNodoVacio(nodoActual)){
            NodoBinario<K, V> nodoNuevo = new NodoBinario<K, V>(claveAInsertar, valorAsociado);
            return nodoNuevo;
        }

        K claveDelNodoActual = nodoActual.getClave();
        if(claveAInsertar.compareTo(claveDelNodoActual) < 0){
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = balancear(insertar(nodoActual.getHijoIzquierdo(),
                                    claveAInsertar, valorAsociado));
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);
        }

        if(claveAInsertar.compareTo(claveDelNodoActual) > 0){
            NodoBinario<K, V> supuestoNuevoHijoDerecho = insertar(nodoActual.getHijoDerecho(),
                                    claveAInsertar, valorAsociado);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return balancear(nodoActual);
        }

        nodoActual.setValor(valorAsociado);
        return nodoActual;
    }

    private NodoBinario<K, V> balancear(NodoBinario<K, V> nodoABalancear){
        int alturaXIzq = super.altura(nodoABalancear.getHijoIzquierdo());
        int alturaXDer = super.altura(nodoABalancear.getHijoDerecho());
        int diferenciaDeAlturas = alturaXIzq - alturaXDer;
        if(diferenciaDeAlturas > ArbolAVL.LIMITE_MAXIMO){
            //rotación a la derecha
            NodoBinario<K,V> nodoIzquierdo = nodoABalancear.getHijoIzquierdo();
            alturaXIzq = super.altura(nodoIzquierdo.getHijoIzquierdo());
            alturaXDer = super.altura(nodoIzquierdo.getHijoDerecho());
            if(alturaXDer > alturaXIzq){
                return rotacionDobleADerecha(nodoABalancear);
            }else if(alturaXIzq > alturaXDer){
                return rotacionSimpleADerecha(nodoABalancear);
            }
        }else if(diferenciaDeAlturas < -ArbolAVL.LIMITE_MAXIMO){
            //rotación a la izquierda
            NodoBinario<K, V> nodoDerecho = nodoABalancear.getHijoDerecho();
            alturaXIzq = super.altura(nodoDerecho.getHijoIzquierdo());
            alturaXDer = super.altura(nodoDerecho.getHijoDerecho());
            if(alturaXIzq > alturaXDer){
                return rotacionDobleAIzquierda(nodoABalancear);
            }else if(alturaXIzq < alturaXDer){
                return rotacionSimpleAIzquierda(nodoABalancear);
            }
        }
        return nodoABalancear;
    }

    private NodoBinario<K, V> rotacionSimpleADerecha(NodoBinario<K, V> nodoActual){
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());
        nodoQueRota.setHijoDerecho(nodoActual);
        return nodoQueRota;
    }

    private NodoBinario<K, V> rotacionSimpleAIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());
        nodoQueRota.setHijoIzquierdo(nodoActual);
        return nodoQueRota;
    }

    private NodoBinario<K, V> rotacionDobleADerecha(NodoBinario<K, V> nodoActual){
        // Rotación doble: Primero a la izquierda y luego a la derecha
        nodoActual.setHijoIzquierdo(rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo()));
        return rotacionSimpleADerecha(nodoActual);
    }

    private NodoBinario<K, V> rotacionDobleAIzquierda(NodoBinario<K, V> nodoActual) {
        // Rotación doble: Primero a la derecha y luego a la izquierda
        nodoActual.setHijoDerecho(rotacionSimpleADerecha(nodoActual.getHijoDerecho()));
        return rotacionSimpleAIzquierda(nodoActual);
    }
    
    
    //ELIMINAR
    @Override
    public V eliminar(K claveAEliminar){
        V valorRetorno = super.buscar(claveAEliminar);
        if(valorRetorno == null){
            return null;
        }
        this.raiz=eliminar(raiz,claveAEliminar);
        return valorRetorno;
    }

    private NodoBinario<K,V> eliminar(NodoBinario<K,V>nodoActual,K claveAEliminar){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return NodoBinario.nodoVacio();
        }

        K claveActual=nodoActual.getClave();
        if(claveAEliminar.compareTo(claveActual)<0){
            NodoBinario<K,V> izquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(izquierdo);
            return balancear(nodoActual);
        }
        if(claveAEliminar.compareTo(claveActual)>0){
            NodoBinario<K,V> derecho=eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(derecho);
            return balancear(nodoActual);
        }
        /// SI SE LLEGA A ESTE PUNTO SE ENCONTRO LA CLAVE A ELIMINAR
        ///YA QUE LA CLAVE A ELIMINAR NO ES MENOR NI MAYOR ,SINO IGUAL
        // # caso 1 el nodo a eliminar es una hoja
        if(nodoActual.esHoja()){
            return NodoBinario.nodoVacio();
        }

        //# CASO 2 LA CLAVE A ELIMINAR ES UN NODO INCOMPLETO
        if(nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo()){
            return balancear(nodoActual.getHijoIzquierdo());
        }
        if(!nodoActual.esVacioHijoDerecho() && nodoActual.esVacioHijoIzquierdo()){
            return balancear(nodoActual.getHijoDerecho());
        }

        // # CASO 3 LA CLAVE A ELIMINAR ES UN NODO COMPLETO
        // HAY QUE BUSCAR SU NODO SUCESOR
        NodoBinario<K,V> nodoSucesor = (cambiar(nodoActual.getHijoDerecho()));
        NodoBinario<K,V> posibleNuevo = (eliminar(nodoActual.getHijoDerecho(),nodoSucesor.getClave()));

        nodoActual.setHijoDerecho((posibleNuevo));
        nodoSucesor.setHijoDerecho((nodoActual.getHijoDerecho()));
        nodoSucesor.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoDerecho(NodoBinario.nodoVacio());
        nodoActual.setHijoIzquierdo(NodoBinario.nodoVacio());

        return nodoSucesor;
    }

    private NodoBinario<K, V> cambiar(NodoBinario<K, V> nodoActual){
        while (!nodoActual.esVacioHijoIzquierdo()) {
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoActual;
    }
    
    public static void main(String[] args){
        ArbolAVL<Integer, String> avl = new ArbolAVL();
        avl.insertar(50, "Rodrigo");
        avl.insertar(30, "rojo");
        avl.insertar(90, "azul");
        avl.insertar(10, "amarillo");
        avl.insertar(75, "verde");
        avl.insertar(80, "cafe");
        avl.insertar(95, "asasa");
        System.out.println("PreOrden: "+avl.recorridoEnPreOrden());
        System.out.println("Elimino el 10: "+avl.eliminar(10));
        System.out.println("PreOrden: "+avl.recorridoEnPreOrden());
        avl.insertar(10, "amarillo");
        System.out.println("PreOrden: "+avl.recorridoEnPreOrden());
    }
}
