package org.sii.performance.exception;



public class ObjectifNotFoundException extends RuntimeException{
    public ObjectifNotFoundException(){
        super(Constante.ObjectifNotFound);
    }
}
