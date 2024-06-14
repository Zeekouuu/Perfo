package org.sii.performance.exception;

public class BilanNotFoundException extends  RuntimeException{
    public BilanNotFoundException(){super(Constante.BilanNotFound);}
}
