package examples.behaviours;

import java.lang.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class HandsOn6 extends Agent{
    protected void setup(){
        System.out.println("Agent "+getLocalName()+" started.");
        AlgoritmoGenetico ga = new AlgoritmoGenetico(10, 0.01, 0.95, 2); //Total de individuos, %de mutación, % de cruzamiento, Numero de mejores individuos
        Poblacion poblacion = ga.iniciarPoblacion(10);//Se inicializa poblacion con cantidad de cromosomas deseados

        //Evaluar la poblacion
        ga.evalPoblacion(poblacion);

        //Inicializar generacion
        int generacion = 1;

        while(ga.condicionTerminal(poblacion)==false){
            //Muestra los cromosomas del individuo mas adaptado
            System.out.println("Mejor Adaptado: "+poblacion.getMasAdaptado(0).cromosomaString());

            //LLamar a cruzamiento
            poblacion = ga.poblacionDescendientes(poblacion);

            //Llamar a mutacion
            poblacion = ga.mutarPoblacion(poblacion);

            //LLamar metodo para evaluar poblacion
            ga.evalPoblacion(poblacion);

            //Incrementar generacion
            generacion++;
        }
//Muestra la solucion optima
        System.out.println("Solucion encontrada en "+generacion+"generaciones");
        System.out.println("Mejor Solucion: "+poblacion.getMasAdaptado(0).cromosomaString());
    }


}

class Individuo{
    public int cromosoma[];
    private double fitness = -1;

    public Individuo(int cromosoma[]){
        this.cromosoma=cromosoma;
    }

    public Individuo(int tamanioCromosoma){
        this.cromosoma = new int[tamanioCromosoma];
        for(int gen = 0; gen < tamanioCromosoma; gen++){
            if(0.5 < Math.random()){
                setGen(gen, 1);
            }else{
                setGen(gen, 0);
            }
        }
    }

    public void setGen(int posicion, int gen){
        cromosoma[posicion] = gen;
    }

    public int getGen(int posicion){
        return cromosoma[posicion];
    }

    public int[] getCromosoma(){
        return cromosoma;
    }

    public int getTamanoCromosoma(){
        return cromosoma.length;
    }

    public double getFitness(){
        return fitness;
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public String cromosomaString(){
        String strCromosoma = "";
        for(int gen=0; gen<cromosoma.length; gen++){
            strCromosoma += cromosoma[gen];
        }
        return strCromosoma;
    }
}

class Poblacion{
    private Individuo poblacion[];//Coleccion de individuos
    private double fitnessPoblacion = -1;

//Se crea el arreglo de los individuos
    public Poblacion(int tamanoPoblacion){
        poblacion = new Individuo[tamanoPoblacion];
    } 

//A cada individuo se asigna el tamaño de cromosomas
    public Poblacion(int tamanoPoblacion, int tamanioCromosoma){
        poblacion = new Individuo[tamanoPoblacion];
        for(int i=0; i<tamanoPoblacion;i++){
            Individuo individual=new Individuo(tamanioCromosoma);
            poblacion[i]=individual;
        }
    }

    public Individuo[] getPoblacion(){
        return poblacion;
    }

//Se ordenan los individuos de mejor a peor
    public Individuo getMasAdaptado(int seleccionIndividuo){
        Arrays.sort(poblacion, new Comparator<Individuo>(){
            @Override
            public int compare(Individuo o1, Individuo o2){
                if(o1.getFitness()>o2.getFitness()){
                    return -1;
                }else if(o1.getFitness()<o2.getFitness()){
                    return 1;
                }
                return 0;
            }
        });
        return poblacion[seleccionIndividuo];
    }

    public double getFitnessPoblacion(){
        return fitnessPoblacion;
    }

    public int tamanoPoblacion(){
        return poblacion.length;
    }

    public void setFitnessPoblacion(double fitnessPoblacion){
        this.fitnessPoblacion=fitnessPoblacion;
    }

    public Individuo setIndividuo(int indice, Individuo individuo){
        return poblacion[indice]=individuo;
    }

    public Individuo getIndividuo(int indice){
        return poblacion[indice];
    }

    public void Mezclar(){
        Random rnd = new Random();
        for(int i= poblacion.length-1; i>0; i--){
            int index=rnd.nextInt(i+1);
            Individuo a = poblacion[index];
            poblacion[index]=poblacion[i];
            poblacion[i]=a;
        }
    }
}

class AlgoritmoGenetico{
    private int tamanoPoblacion;
    private double tasaMutacion;
    private double tasacruzamiento;
    private int numeroElite;//Cantidad de individuos a los que no se les mutará (los mas adaptados)

    public AlgoritmoGenetico(int tamanoPoblacion, double tasaMutacion, double tasacruzamiento, int numeroElite){
        this.tamanoPoblacion = tamanoPoblacion;
        this.tasaMutacion = tasaMutacion;
        this.tasacruzamiento = tasacruzamiento;
        this.numeroElite = numeroElite;
    }

    public Poblacion iniciarPoblacion(int tamanioCromosoma){
        Poblacion poblacion = new Poblacion(tamanoPoblacion, tamanioCromosoma);
        return poblacion;
    }
//Suma la cantidad de unos y se dividen entre el cromosoma para calcular el fitness de cada individuo
    public double calcFitness(Individuo individuo){
        int genesCorrectos =0;
        for(int indiceGen = 0; indiceGen<individuo.getTamanoCromosoma(); indiceGen++){
            if(individuo.getGen(indiceGen)==1){
                genesCorrectos += 1;
            }
        }
        double fitness = (double) genesCorrectos / individuo.getTamanoCromosoma();
        individuo.setFitness(fitness);
        return fitness;
    }
//Suma del fitnes de cada individuo, para tener el fitness de la poblacion
    public void evalPoblacion(Poblacion poblacion){
        double poblacionFitness = 0;
        for(Individuo individuo:poblacion.getPoblacion()){
            poblacionFitness += calcFitness(individuo);
        }
        poblacion.setFitnessPoblacion(poblacionFitness);
    }

//Determina si la poblacion converge, es decir si ya son todos unos
    public boolean condicionTerminal(Poblacion poblacion){
        for(Individuo individuo:poblacion.getPoblacion()){
            if(individuo.getFitness() == 1){
                return true;
            }
        }
        return false;
    }

//Seleccion de los padres de forma aleatoria
    public Individuo seleccionarPadre(Poblacion poblacion){
        Individuo individuos[] = poblacion.getPoblacion();
        double fitnessPoblacion = poblacion.getFitnessPoblacion();//Se obtiene el fitness de la poblacion
        double posicionRuleta = Math.random()*fitnessPoblacion;//Se multiplica por un numero aleatorio
        double agujaRuleta = 0;
        for(Individuo individuo:individuos){
            agujaRuleta+=individuo.getFitness();
            if(agujaRuleta>=posicionRuleta){
                return individuo;
            }
        }
        return individuos[individuos.length-1];
    }

//Cruzamiento
    public Poblacion poblacionDescendientes(Poblacion poblacion){
        Poblacion newPoblacion = new Poblacion(poblacion.getPoblacion().length);
        for(int indice =0; indice<poblacion.getPoblacion().length; indice++){
            Individuo padre1=poblacion.getMasAdaptado(indice);
            if(this.tasacruzamiento>Math.random()&&indice>=this.numeroElite){
                Individuo padre2=seleccionarPadre(poblacion);
                Individuo descendiente= new Individuo(padre1.getCromosoma());
                for(int geneIndex=0;geneIndex<padre1.getTamanoCromosoma();geneIndex++){
                    if(0.5>Math.random()){
                        descendiente.setGen(geneIndex, padre1.getGen(geneIndex));
                    }else{
                        descendiente.setGen(geneIndex, padre2.getGen(geneIndex));
                    }
                }
                newPoblacion.setIndividuo(indice, padre1);
            }
        }
        return newPoblacion;
    }

    public Poblacion mutarPoblacion(Poblacion poblacion){
        Poblacion newPoblacion = new Poblacion(tamanoPoblacion);
        for(int indice=0; indice<poblacion.getPoblacion().length; indice++)
        {
            Individuo individuo = poblacion.getMasAdaptado(indice);
            for(int geneIndex=0; geneIndex<individuo.getTamanoCromosoma(); geneIndex++){
                if(indice>numeroElite){
                    if(tasaMutacion>Math.random()){
                        int newGen=1;
                        if(individuo.getGen(geneIndex)==1){
                            newGen=0;
                        }
                        individuo.setGen(geneIndex, newGen);
                    }
                }
            }

            newPoblacion.setIndividuo(indice, individuo);
        }
        return newPoblacion;
    }
}

