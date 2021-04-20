package algoritmo.genetico;

import java.util.ArrayList;
import java.util.List;


public class Individuo implements Comparable<Individuo> {
    List<Integer> cromosoma;
    double platillo1,platillo2;
    double fitness;

    public double getPlatillo1() {
        return platillo1;
    }

    public void setPlatillo1(double platillo1) {
        this.platillo1 = platillo1;
    }

    public double getPlatillo2() {
        return platillo2;
    }

    public void setPlatillo2(double platillo2) {
        this.platillo2 = platillo2;
    }
    

    public Individuo() {
        cromosoma = new ArrayList();
        fitness = -1;
        platillo1 = 0;
        platillo2 = 0;
    }

    public Individuo(List<Integer> cromosoma, double fitness, List<Double> pesos) {
        this.cromosoma = cromosoma;
        this.fitness = fitness;
   }

    public List<Integer> getCromosoma() {
        return cromosoma;
    }

    public void setCromosoma(List<Integer> cromosoma) {
        this.cromosoma = cromosoma;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individuo other = (Individuo) obj;
        if (Double.doubleToLongBits(this.fitness) != Double.doubleToLongBits(other.fitness)) {
            return false;
        }
        return true;
    }
   
    @Override
    public int compareTo (Individuo ind){//comparamos los beneficios para que vaya cogiendo los de menor beneficio, porque buscamos la menor diferencia 
       int comparacion;
       
       if (this.fitness < ind.fitness)
           comparacion = -1;
       else if (this.fitness > ind.fitness)
           comparacion = 1;
       else
           comparacion = 0;
       
       return comparacion;
    }
    
      
    public double calcularFitness(){//calculamos las diferencias de pesos que hay entre los platillos
        double fitness=0;
        
        for (int i = 0; i < cromosoma.size()/2; i++) 
            platillo1 += cromosoma.get(i)*GA.pesos.get(i);
        for (int i = cromosoma.size()/2; i < cromosoma.size(); i++) 
            platillo2 += cromosoma.get(i)*GA.pesos.get(i);
        fitness = Math.abs(platillo1-platillo2);
        setFitness(fitness);
        return fitness;
    }
    
        
    public Individuo copiar(Individuo o){//copia un individuo
        Individuo clonado = new Individuo();
        
        clonado.cromosoma = new ArrayList<>(o.cromosoma);
        clonado.setFitness(o.getFitness());
        
        return clonado;
    }
    
    
    @Override
    public Individuo clone() {//copiamos todos los atributos 
        Individuo clonado = new Individuo();
        
        clonado.setCromosoma (this.getCromosoma());
        clonado.setFitness(this.getFitness());
        clonado.setPlatillo1(this.getPlatillo1());
        clonado.setPlatillo2(this.getPlatillo2());
        
        return clonado;
    }
    
}
