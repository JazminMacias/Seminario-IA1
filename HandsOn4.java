package examples.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import java.util.Scanner;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class HandsOn4 extends Agent {
    double Beta_0, Beta_1;
    double promedio_y;
    double promedio_x;

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    Scanner leer=new Scanner(System.in);
    System.out.print("Ingrese valor de n: ");
    int n=leer.nextInt();
    double vector_x [] = new double[n];
    double vector_y [] = new double[n];
    //For para llenar los vectores
    for (int i = 0; i<n; i = i+1)
    {
        System.out.print("X"+(i+1)+": ");
        vector_x[i]=leer.nextDouble();
        System.out.print("Y"+(i+1)+": ");
        vector_y[i]=leer.nextDouble();
    }
//For para encontrar sumatoria de y
    double suma_y=vector_y[0];
    for(int i=1; i<n; i=i+1)
    {
        suma_y=suma_y+vector_y[i];
    }

    //For para encontrar sumatoria de x
    double suma_x=vector_x[0];
    for(int i=1; i<n; i=i+1)
    {
        suma_x=suma_x+vector_x[i];
    }

    //For para encontrar sumatoria de xy
    double suma_xy=0;
    for(int i=0; i<n; i=i+1)
    {
        suma_xy=suma_xy+(vector_x[i]*vector_y[i]);
    }

    //For para encontrar sumatoria de x^2
    double suma_xcuadrada=0;
    for(int i=0; i<n; i=i+1)
    {
        suma_xcuadrada=suma_xcuadrada+Math.pow(vector_x[i],2);
    }

    promedio_y=suma_y/n;
    promedio_x=suma_x/n;

    Beta_1= ((n*suma_xy)-(suma_x*suma_y))/((n*suma_xcuadrada)-Math.pow(suma_x,2));
    Beta_0= promedio_y-(Beta_1*promedio_x);

    System.out.println("Variables: ");
    System.out.println("N: "+n);
    System.out.println("Sumatoria de X: "+suma_x);
    System.out.println("Sumatoria de Y: "+suma_y);
    System.out.println("Sumatoria de X^2: "+suma_xcuadrada);
    System.out.println("Sumatoria de X al cuadrado: "+Math.pow(suma_x,2));
    System.out.println("Beta0: "+Beta_0);
    System.out.println("Beta1: "+Beta_1);

    String x_texto=JOptionPane.showInputDialog(null,"Ingresar valor de X deseado para predecir Y");
    double x_deseado=Double.parseDouble(x_texto);
    System.out.println("y=Beta0 + Beta1*x");
    System.out.println("Sustituyendo: Y= "+Beta_0+" + "+Beta_1+" * "+x_deseado);
    double y_calculada=Beta_0+(Beta_1*x_deseado);
    JOptionPane.showMessageDialog(null,"La prediccion de Y calculada es: "+y_calculada);
  }
}
