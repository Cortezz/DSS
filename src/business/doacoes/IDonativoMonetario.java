/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.doacoes;

/**
 *
 * @author daniel
 */
public interface IDonativoMonetario extends IDonativo{
    
    /*gets*/
    public float getValor();
    /*sets*/
    public void setValor (float v);
    
}
