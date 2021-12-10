package examples.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class Challenge1 extends Agent {

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    int iteracion=1;
    double funcion_objetivo = 0;
    double x;
    double funcion_derivada;


       do
        {
           System.out.println("Iteracion " + iteracion);
           x = -1 + (double)Math.random() * (1-(-1))+1;
           System.out.println("X: " + x);
           funcion_objetivo = Math.pow(x,2);
           System.out.println("Funcion objetivo(x^2) : " + funcion_objetivo);
           funcion_derivada = 2 * x;
           System.out.println("Funcion derivada: " + funcion_derivada);
           x = x - 1 * funcion_derivada;
           iteracion++;
        }  while(funcion_objetivo != 0);
  }  
}
