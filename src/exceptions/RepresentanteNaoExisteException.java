/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author USER
 */
public class RepresentanteNaoExisteException  extends Exception{
    public RepresentanteNaoExisteException(int nr){
        super("Representante com nr: "+nr+" não existe");
    }
}