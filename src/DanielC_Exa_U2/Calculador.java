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
public class Calculador {
    private final String[] entrada;
    private final boolean aPrefijo;
    Stack<String> stack;
    ArrayList<Object[]> operaciones;
    String tokenActual;
    int pos;
    int resultado;
    final private int intervalo=1000;
    boolean listo;
    Calculador(String entrada, boolean aPrefijo){
        this.entrada=entrada.split(" ");
        this.aPrefijo=aPrefijo;
        this.stack=new Stack();
        this.operaciones=new ArrayList();
        this.listo=false;
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
    
    public void calcula(){
        try{
            if(aPrefijo) calculaPrefijo();
            else calculaPosfijo();
            resultado=Integer.parseInt(stack.pop());
            tokenActual="";
            listo=true;
        }
        catch(InterruptedException e){}
    }
    
    private int opera(int n1, int n2, String op){
        if(op.equals("*")) return n1*n2;
        else if(op.equals("/")) return n1/n2;
        else if(op.equals("+")) return n1+n2;
        else if(op.equals("-")) return n1-n2;
        else return 0;
    }
    public void calculaPrefijo() throws InterruptedException{
        for(pos=entrada.length-1; pos>=0; pos--){
            tokenActual=entrada[pos];
            if(!esNumero(entrada[pos])){
                String op=entrada[pos];
                int n1=Integer.parseInt(stack.pop());
                int n2=Integer.parseInt(stack.pop());
                int val=opera(n1, n2, op);
                operaciones.add(new Object[]{n1, op, n2, val});
                stack.push(val+"");
                
            }
            else{
                stack.push(entrada[pos]);
            }
            Thread.sleep(intervalo);
        }
    }
    public void calculaPosfijo() throws InterruptedException{
        for(pos=0; pos<entrada.length; pos++){
            tokenActual=entrada[pos];
            if(!esNumero(entrada[pos])){
                String op=entrada[pos];
                int n2=Integer.parseInt(stack.pop());
                int n1=Integer.parseInt(stack.pop());
                int val=opera(n1, n2, op);
                operaciones.add(new Object[]{n1, op, n2, val});
                stack.push(val+"");
            }
            else{
                stack.push(entrada[pos]);
            }
            Thread.sleep(intervalo);
        }
    }
}
