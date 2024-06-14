package org.sii.performance.exception;

public class ActionNotFoundException extends RuntimeException{
    public ActionNotFoundException(){super(Constante.ActionNotFound);}
}
