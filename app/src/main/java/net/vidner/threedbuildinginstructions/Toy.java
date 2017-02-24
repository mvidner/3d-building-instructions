package net.vidner.threedbuildinginstructions;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvidner on 24.2.17.
 */

/*
    Building instructions for a Toy.
    (we call it a Toy not to get confused with an OpenGL Model)

    It consists of additive steps. See ToyStep.
 */
public class Toy {
    private List<ToyStep> mSteps;
    private int mCurrentStep; // one-based

    /*
        Return a hardcoded toy: a house
     */
    static Toy newHouse(Context ctx) {
        int[][] ids = {
                {
                        R.drawable.house_s00_z00
                },
                {
                        0,
                        R.drawable.house_s01_z01
                },
                {
                        0,
                        0,
                        R.drawable.house_s02_z02,
                        R.drawable.house_s02_z03,
                        R.drawable.house_s02_z04,
                        R.drawable.house_s02_z05,
                        R.drawable.house_s02_z06,
                        R.drawable.house_s02_z07,
                        R.drawable.house_s02_z08
                },
                {
                        0, 0, 0, 0, 0, 0, 0, 0, 0,
                        R.drawable.house_s03_z09,
                        R.drawable.house_s03_z10,
                        R.drawable.house_s03_z11,
                        R.drawable.house_s03_z12
                },
                {
                        0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0,
                        R.drawable.house_s04_z13
                }
        };

        ArrayList<ToyStep> steps = new ArrayList<ToyStep>();
        for (int[] stepdata: ids) {
            ToyStep step = new ToyStep(ctx, stepdata);
            steps.add(step);
        };
        Toy t = new Toy(steps);
        return t;
    }

    public Toy(List<ToyStep> steps) {
        mSteps = steps;
        // FIXME: assert that mSteps is not empty
        mCurrentStep = steps.size();
    }

    public void draw(float[] mvpMatrix) {
        int i;
        for (i = 0; i < mCurrentStep; ++i) {
            mSteps.get(i).draw(mvpMatrix);
        }
    }

    public void previousStep() {
        if (mCurrentStep > 1) {
            mCurrentStep -= 1;
        }
    }

    public void nextStep() {
        if (mCurrentStep < mSteps.size()) {
            mCurrentStep += 1;
        }
    }
}
