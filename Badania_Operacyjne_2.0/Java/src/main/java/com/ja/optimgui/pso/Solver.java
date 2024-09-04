package com.ja.optimgui.pso;

import com.ja.optimgui.math.MVector;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Solver {

    @Getter
    private MVector globalBestPosition;

    @Getter
    private double bestValue;

    @Getter
    private Particle bestParticle;
    private Function<MVector, Double> objectiveFunction;

    @Getter
    @Setter
    private MVector lowerBoundary;
    @Getter
    @Setter
    private MVector upperBoundary;
    @Getter
    @Setter
    private int maxIter;
    @Getter
    @Setter
    private double w;
    @Getter
    @Setter
    private double c1;
    @Setter
    @Getter
    private double c2;
    @Getter
    private List<Double> costHistory;

    private int swarmSize;
    private List<Particle> particles = new ArrayList<>();

    public Solver(int swarmSize, Function<MVector, Double> objectiveFunction, MVector lowerBoundary, MVector upperBoundary, int maxIter, double w, double c1, double c2) {

        this.swarmSize = swarmSize;
        bestValue = Double.POSITIVE_INFINITY;
        globalBestPosition = new MVector(lowerBoundary.dimension(), Double.NaN);
        this.objectiveFunction = objectiveFunction;
        this.maxIter = maxIter;
        this.w = w;
        this.c1 = c1;
        this.c2 = c2;
        this.costHistory = new ArrayList<>();

        for (int i = 0; i < swarmSize; i++) {

            Particle particle = new Particle(lowerBoundary, upperBoundary);
            particle.castToInt();
            this.checkForBetterGlobalValue(particle);
            particles.add(particle);
        }
    }

//    public void setSwarmSize(int size) {
//
//    }

    public void checkForBetterGlobalValue(Particle particle) {
        MVector particlePosition = particle.getPosition();
        double particleValue = objectiveFunction.apply(particlePosition);

        if(particleValue < particle.getBestValue()){
            particle.setBestValue(particleValue);
            particle.setBestPosition(particlePosition);
        }

        if(particleValue < bestValue){
            globalBestPosition.set(particlePosition);
            bestValue = particleValue;
            bestParticle = particle;
        }
    }

    public void update() {
//        //inertia weight constant
//        double w = 0.8;
//        //cognitive coefficient
//        double c1 = 0.1;
//        //social coefficient
//        double c2 = 0.1;

        double r1 = ThreadLocalRandom.current().nextDouble(0, 1+1);
        double r2 = ThreadLocalRandom.current().nextDouble(0, 1+1);

        for (Particle particle: particles) {
            particle.update(globalBestPosition, w, c1, r1, c2, r2);
            this.checkForBetterGlobalValue(particle);
        }
    }

    public List<MVector> getParticlePositions(){
        List<MVector> particlePositions = new ArrayList<>();

        for (Particle particle: particles) {
            particlePositions.add(particle.getPosition());
        }

        return particlePositions;
    }

    public void solve() {

//        bestValue = Double.POSITIVE_INFINITY;
//        globalBestPosition.reset();
//        double previousValue = Double.POSITIVE_INFINITY;
        double previousValue = bestValue;
        int  counter = 0;

        //na szybko zrobiona pętla do testowania algorytmu
        //counter w ifie mówi ile musi powtórzyć się wynik by uznać że można skończyć
        for (int i = 0; i < 10000; i++) {
            costHistory.add(bestValue);
            System.out.println(this.getBestParticle().getPosition());
            System.out.println(this.getBestValue());
            if(this.getBestValue() == previousValue){
                counter++;
            }else{
                counter = 0;
            }

            if (counter >= 10){
                System.out.println();
                System.out.println("znaleziono:");
                System.out.println("Iteracje: " + i);
                System.out.println(this.getBestParticle().getPosition());
                System.out.println(this.getBestValue());
                break;
            }
            previousValue = bestValue;
            this.update();
        }
    }

    public static SolverBuilder builder() {
        return new SolverBuilder();
    }
}
