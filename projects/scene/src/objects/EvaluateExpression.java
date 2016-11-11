/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javax.script.ScriptException;

/**
 *
 * @author marci
 */
public interface EvaluateExpression {
    public Double parseDouble(String expr) throws ScriptException;
    public Integer parseInt(String expr) throws ScriptException;
    
    public Object evaluate(String expr) throws ScriptException;
}
