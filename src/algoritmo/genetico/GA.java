package algoritmo.genetico;
import java.util.ArrayList;
import java.util.List;

public class GA {

    final static int GENERACIONES = 10;             // Iteraciones realizadas
    final static int TAM_POBLACION = 10;            // Tamaño de la población
    final static int GENES = 10;                    // Tamaño de cada solución
    final static int ELITISMO = 10;                 // % mejores individuos directos a la siguiente población
    final static double P_MUTACION = 0.1;           // Probabilidad de mutación
    public static List<Double> pesos;

    public static void main(String[] args) {
        long t_inicial, t_final;
        long t_total = 0;
        Poblacion poblacion = null;
        for (int i = 0; i < 30; i++) {
            t_inicial = System.currentTimeMillis();
            poblacion = algoritmoGenetico();
            t_final = System.currentTimeMillis();
            t_total += t_final - t_inicial;
        }
        
        System.out.println("Población final ya ordenada: \n" + poblacion.toString());
        System.out.println("Tiempo de ejecución de media: " + (t_total / 30)/1000.0 + " sec.");
    }

    public static Poblacion algoritmoGenetico() {
        Poblacion pobActual;                    // Almacena la población actual
        Poblacion pobSiguiente;                 // Almacena la población generada a partir de la actual
        
        int condParada = 0;
        pesos = generarPesos();
        pobActual = generarPoblacionInicial();

        while (condParada < GENERACIONES) {
            pobSiguiente = generarNuevaPoblacion(pobActual);               // Operador cruce
            pobActual = pobSiguiente;                                      // Actualización
            System.out.println("####\nNueva población ya ordenada en iteración: " + condParada + "\n" + pobSiguiente.toString() + "####\n");
            condParada++;                                                  // Generación completada
        }

        return pobActual;
    }

    public static List<Double> generarPesos() {
        List<Double> pesosAleatorios = new ArrayList();
        int i = 0;
        double peso;
        while (i < GENES) {
            peso=Math.abs(Math.random());
            
            pesosAleatorios.add(peso);        // Lo inserto en la lista de la población inicial
            i++;
        }
        //System.out.println(pesosAleatorios.toString());
        return pesosAleatorios;
    }


    public static Poblacion generarPoblacionInicial() {
        // Se genera la población inicial aleatoriamente    
        Poblacion pobInicial = new Poblacion();
        Individuo individuoNuevo;

        while (pobInicial.poblacion.size() < TAM_POBLACION) {
            individuoNuevo = new Individuo();
            for (int i = 0; i < GENES; i++) {//creamos los individuos de forma aleatoria dependemos del numero obtenido por el random
                if (Math.random() > 0.5) {
                    individuoNuevo.cromosoma.add(1);
                } else {
                    individuoNuevo.cromosoma.add(0);
                }
            }
            individuoNuevo.setFitness(individuoNuevo.calcularFitness());
            // Si el peso no es positivo , ni está ya en la población ni ha habido error
            if (!pobInicial.poblacion.contains(individuoNuevo) && individuoNuevo.getFitness() > 0) {
                pobInicial.poblacion.add(individuoNuevo);   // Lo inserto en la lista de la población inicial
            }
        }
        // Ordeno la población para facilitar el elitismo
        pobInicial.ordenarPoblacion();
        System.out.println("Población inicial generada ya ordenada:\n" + pobInicial.toString());
        return pobInicial;

    }

    public static List<Individuo> seleccionarProgenitores(Poblacion pobActual) {
        List<Individuo> progenitores = new ArrayList();
        int pos1, pos2, pos3, pos4;

        // Escojo 4 números aleatorios
        pos1 = (int) (Math.random() * (TAM_POBLACION - 1));
        pos2 = pos3 = pos4 = pos1;
        while (pos2 == pos1) {
            pos2 = (int) (Math.random() * (TAM_POBLACION - 1));
        }
        while (pos3 == pos1 || pos3 == pos2) {
            pos3 = (int) (Math.random() * (TAM_POBLACION - 1));
        }
        while (pos4 == pos1 || pos4 == pos2 || pos3 == pos4) {
            pos4 = (int) (Math.random() * (TAM_POBLACION - 1));
        }

        // Hago el torneo entre ellos, seleccionando los dos ganadores
        //// Ganador #1
        if (pobActual.getPoblacion().get(pos1).getFitness() < pobActual.getPoblacion().get(pos2).getFitness()) {
            progenitores.add(pobActual.getPoblacion().get(pos1));
        } else {
            progenitores.add(pobActual.getPoblacion().get(pos2));
        }
        //// Ganador #2
        if (pobActual.getPoblacion().get(pos3).getFitness() < pobActual.getPoblacion().get(pos4).getFitness()) {
            progenitores.add(pobActual.getPoblacion().get(pos3));
        } else {
            progenitores.add(pobActual.getPoblacion().get(pos4));
        }
        // Devuelvo los dos progenitores ganadores del torneo
        return progenitores;
    }

    public static Poblacion generarNuevaPoblacion(Poblacion pobActual) {
        List<Individuo> descendientes=new ArrayList();
        List<Individuo> descendientesMutados;
        List<Individuo> progenitores;
        Poblacion pobSiguiente;
        // Número de descendientes que se deben generar, menos los élite
        int numDescendientes = (int) (TAM_POBLACION * (1.0 - (ELITISMO / 100.0)));

        while (descendientes.size() < numDescendientes) {
            progenitores = seleccionarProgenitores(pobActual); 
            cruceUniforme(progenitores.get(0), progenitores.get(1),descendientes);    // Por eso get(0) y get(1)
            //crucePuntoCorte(progenitores.get(0), progenitores.get(1),descendientes);// Por eso get(0) y get(1)   
            
        }
        descendientesMutados = mutarDescendientes(descendientes); 
        pobSiguiente = nuevaPoblacion(pobActual, descendientesMutados);
        
        return pobSiguiente;
    }

    //Método para hacer un cruce bit a bit. Invocar a este método en vez de cruceUniforme si se quiere 
    //probar diferentes formas de hacer los cruces
    public static List<Individuo> crucePuntoCorte(Individuo progenitor1, Individuo progenitor2, List<Individuo> descendientes) {
        // Selecciono aleatoriamente un bit de cada progenitor para cada descendiente 
        Individuo descendiente1 = new Individuo();
        Individuo descendiente2 = new Individuo();
        int puntoCorte;

        puntoCorte = (int) (Math.random() * (progenitor1.cromosoma.size())); // Punto de corte dinámico
        //puntoCorte = (int)progenitor1.genotipo.length/2; // Punto de corte estático para todos

        for (int i = 0; i < puntoCorte; i++) { // Copio medio progenitor en cada descendiente
            descendiente1.cromosoma.set(i, progenitor1.cromosoma.get(i));
            descendiente2.cromosoma.set(i, progenitor2.cromosoma.get(i));
        }

        for (int i = puntoCorte; i < progenitor1.cromosoma.size(); i++) { // Y ahora el medio restante
            descendiente1.cromosoma.set(i, progenitor2.cromosoma.get(i));
            descendiente2.cromosoma.set(i, progenitor1.cromosoma.get(i));
        }
        // Calculo el fitness de los nuevos descendientes
        descendiente1.setFitness(descendiente1.calcularFitness());
        descendiente2.setFitness(descendiente2.calcularFitness());


            descendientes.add(descendiente1);
            descendientes.add(descendiente2);
       
        
        return descendientes;
    }

    //Método para hacer un cruce uniforme. Invocar a este método en vez de crucePuntoCorte si se quiere 
    //probar diferentes formas de hacer los cruces
    public static List<Individuo> cruceUniforme(Individuo progenitor1, Individuo progenitor2, List<Individuo> descendientes) {
        // Selecciono aleatoriamente un bit de cada progenitor para cada descendiente 
        
        Individuo descendiente1 = new Individuo();
        Individuo descendiente2 = new Individuo();
        boolean b;

        for (int i = 0; i < GENES; i++) {
            b = (Math.random() >= 0.5);
            if (b) {
                descendiente1.cromosoma.add(progenitor1.cromosoma.get(i));
                descendiente2.cromosoma.add(progenitor2.cromosoma.get(i));
            } else {
                descendiente1.cromosoma.add(progenitor2.cromosoma.get(i));
                descendiente2.cromosoma.add(progenitor1.cromosoma.get(i));
     }
        }
        //calculamos el fitnes de los descendientes
        descendiente1.setFitness(descendiente1.calcularFitness());
        descendiente2.setFitness(descendiente2.calcularFitness());

        if (!descendientes.contains(descendiente1)){// && descendiente1.getFitness() > progenitor1.getFitness() && descendiente1.getFitness() > progenitor2.getFitness()) {
            descendientes.add(descendiente1);
        } else {
            descendientes.add(progenitor1);
        }

        if (!descendientes.contains(descendiente2)){// && descendiente2.getFitness() > progenitor1.getFitness() && descendiente2.getFitness() > progenitor2.getFitness()) {
            descendientes.add(descendiente2);
        } else {
           descendientes.add(progenitor2);
        }
        
        return descendientes;
    }

    public static List<Individuo> mutarDescendientes(List<Individuo> descendientes) {
        List<Individuo> descendientesMutados = new ArrayList();
        int i;
        boolean mutar;
        int gen;
        Individuo mutado;//=new Individuo();

        for (i = 0; i < descendientes.size(); i++) {
            mutado = descendientes.get(i).clone();
            mutar = (Math.random() <= P_MUTACION);      //Aplico la probabilidad de mutación
            if (mutar) { // Si se cumple, selecciono aleatoriamente un bit (gen) para mutar
                gen = (int) (Math.random() * GENES);
                if (mutado.cromosoma.get(gen) == 0) {
                    mutado.cromosoma.set(i, 1);
                } else {
                    mutado.cromosoma.set(i, 0);
                }
                mutado.setFitness(mutado.calcularFitness());
            }
            //Añado el descendiente mutado (o sin mutar) a la lista de descendientes mutados
                descendientesMutados.add(mutado);
        }

        return descendientesMutados;
    }

    public static Poblacion nuevaPoblacion(Poblacion pobActual, List<Individuo> descendientes) {
        Poblacion pobSiguiente = new Poblacion(descendientes);  // Vuelco los descendientes mutados ya generados

        int elite = (int) (TAM_POBLACION * ELITISMO / 100.0);       // Determino cuántos individuos élite envío a la siguiente generación

        for (int i = 0; i < elite && pobSiguiente.poblacion.size() < TAM_POBLACION; i++) {
            pobSiguiente.poblacion.add(pobActual.poblacion.get(i));     // Y los añado
        }
        // Dejamos ordenado para la siguiente iteración (para coger las soluciones élite fácilmente)
        pobSiguiente.ordenarPoblacion();

        return pobSiguiente;
    }

}
