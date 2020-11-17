package DanielC_Exa_U2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Dani
 */
public class Conversor{
    final ArrayList<String> tokens;
    final boolean aPrefija;
    final private int intervalo=1000;
    Stack<String> stack;
    String entrada;
    String salida;
    String tokenActual;
    int pos;
    boolean listo;
    Conversor(String infija, boolean aPrefija){
        this.tokens=getTokens(infija);
        this.aPrefija=aPrefija;
        this.listo=false;
        this.stack=new Stack();
        this.salida="";
    }
    
    private ArrayList<String> getTokens(String entrada){
        this.entrada=entrada;
        String tokensRegex="(?=[-+*/()])|(?<=[^-+*/][-+*/])|(?<=[()])";
        ArrayList<String> tokens=new ArrayList<>();
        for(String token : entrada.split(tokensRegex)){
            tokens.add(token);
        }
        return tokens;
    }
    
    boolean esParentesis(String str){
        return str.equals("(") || str.equals(")");
    }

    private int prioridadDe(String operando){
        if(operando.equals("^")) return 3;
        else if(operando.equals("*")) return 2;
        else if(operando.equals("/")) return 2;
        else if(operando.equals("+")) return 1;
        else if(operando.equals("-")) return 1;
        else return 6;
    }
    private boolean esNumero(String token){
        try{
            Integer.parseInt(token);
        }
        catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    
    public void convierte() {
        try{
            if(aPrefija) prefijo();
            else posfijo();
            tokenActual="";
            listo=true;
        }
        catch(InterruptedException e){}
    }
    
    private void prefijo() throws InterruptedException{
        for(pos=tokens.size()-1; pos>=0; pos--){
            tokenActual=tokens.get(pos);
            if(esNumero(tokens.get(pos))){
                salida+=tokenActual+" ";
                Thread.sleep(intervalo);
            }
            else{
                //Si vacía, sale
                if(stack.empty()) stack.push(tokenActual);
                else{
                    //Si es paréntesis
                    if(tokenActual.equals("(")){
                        while(!stack.peek().equals(")")){
                            salida+=stack.pop()+" ";
                            Thread.sleep(intervalo);
                        }
                        stack.pop();
                        Thread.sleep(intervalo);
                    }
                    //Si es operador
                    else{
                        while(stack.size()>0 && !esParentesis(stack.peek()) && prioridadDe(stack.peek())>prioridadDe(tokenActual)){
                            salida+=stack.pop()+ " ";
                            Thread.sleep(intervalo);
                        }
                        stack.push(tokenActual);
                        Thread.sleep(intervalo);
                    }
                }
            }
        }
        tokenActual="$";
        while(stack.size()>0){
            salida+=stack.pop()+" ";
            Thread.sleep(intervalo);
        }
        tokens.clear();
        for(String token : salida.split(" ")) tokens.add(token);
        salida="";
        for(int i=tokens.size()-1; i>=0; i--){
            pos=i;
            if(tokens.get(i).length()==0) continue;
            salida+=tokens.get(i)+" ";
            Thread.sleep(intervalo);
        }
    }
    
    private void posfijo() throws InterruptedException{
        for(pos=0; pos<tokens.size(); pos++){
            tokenActual=tokens.get(pos);
            if(esNumero(tokens.get(pos))){
                salida+=tokenActual+" ";
                Thread.sleep(intervalo);
            }
            else{
                //Si vacía, sale
                if(stack.empty()) stack.push(tokenActual);
                else{
                    //Si es paréntesis
                    if(tokenActual.equals(")")){
                        while(!stack.peek().equals("(")){
                            salida+=stack.pop()+" ";
                            Thread.sleep(intervalo);
                        }
                        stack.pop();
                        Thread.sleep(intervalo);
                    }
                    //Si es operador
                    else{
                        while(stack.size()>0 && !esParentesis(stack.peek()) && prioridadDe(stack.peek())>=prioridadDe(tokenActual)){
                            salida+=stack.pop()+ " ";
                            Thread.sleep(intervalo);
                        }
                        stack.push(tokenActual);
                        Thread.sleep(intervalo);
                    }
                }
            }
        }
        tokenActual="$";
        while(stack.size()>0){
            salida+=stack.pop()+" ";
            Thread.sleep(intervalo);
        }
    }
}