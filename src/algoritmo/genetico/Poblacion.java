package algoritmo.genetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poblacion {
//nos encontramos con los metodos de una lista de poblacion
    public List<Individuo> poblacion;
    
    public Poblacion() {
        this.poblacion = new ArrayList();
    }
    
    public Poblacion(List<Individuo> solucion) {
        this.poblacion = new ArrayList();
        
        for(int i=0; i<solucion.size();i++)
            this.poblacion.add(solucion.get(i));
    }   

    public List<Individuo> getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(List<Individuo> poblacion) {
        this.poblacion = poblacion;
    }

    public void ordenarPoblacion() {
        Collections.sort(poblacion);
    }

    public Individuo getIndividuo(int index) {
        return poblacion.get(index);
    }
    
    @Override
    public String toString() {
        String sPoblacion = "";
        for (int i = 0; i < poblacion.size(); i++) {
            sPoblacion+="Individuo "+String.valueOf(i)+": ";
            for(int j=0; j<GA.GENES/2;j++)
                sPoblacion+=String.valueOf(poblacion.get(i).getCromosoma().get(j));
            
            sPoblacion+=" ";
            for(int j=GA.GENES/2; j<GA.GENES;j++)
                sPoblacion+=String.valueOf(poblacion.get(i).getCromosoma().get(j));
            sPoblacion+=";Platillo1 =" + poblacion.get(i).platillo1;
            sPoblacion+=";Platillo2 =" + poblacion.get(i).platillo2;
           sPoblacion+="; beneficio: "+ poblacion.get(i).getFitness()+"\n";
        }
        //System.out.println(sPoblacion);
        return sPoblacion;
    }

}
