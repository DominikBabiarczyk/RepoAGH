package com.ja.optimgui.pso;

import com.ja.optimgui.math.MVector;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Particle {

    private MVector position;
    private MVector velocity;

    @Setter
    private MVector bestPosition;

    @Setter
    private double bestValue;
    MVector lowerBoundary;
    MVector upperBoundary;

    public Particle(MVector lowerBoundary, MVector upperBoundary) {
        position = new MVector(lowerBoundary, upperBoundary);
        velocity = new MVector(lowerBoundary.dimension(), 0, 0.1);

        bestPosition = new MVector(lowerBoundary.dimension());
        bestPosition.set(position);

        this.lowerBoundary = lowerBoundary;
        this.upperBoundary = upperBoundary;

        bestValue = Double.POSITIVE_INFINITY;
    }

    public void update(MVector globalBestPosition, double w, double c1, double r1, double c2, double r2){
        velocity.set(velocity.scale(w).addElWise(bestPosition.subtractElWise(position).scale(c1*r1)).addElWise(globalBestPosition.subtractElWise(position).scale(c2*r2)));
        position.set(position.addElWise(velocity));

        position.castToInt();

        // check if constraints are satisfied
        for(int i=0; i<position.getDimension(); i++) {
            if(position.getElement(i) < lowerBoundary.getElement(i)) {
                position.setElement(i, lowerBoundary.getElement(i));
            }

            if(position.getElement(i) > upperBoundary.getElement(i)) {
                position.setElement(i, upperBoundary.getElement(i));
            }
        }
    }

    public void castToInt() {
        this.position.castToInt();
    }
}
