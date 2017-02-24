package net.vidner.threedbuildinginstructions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ArrayList<ToyStep> toySteps = new ArrayList<ToyStep>();

        // do not rescale the bitmap according to display dpi
        BitmapFactory.Options bmOpts = new BitmapFactory.Options();
        bmOpts.inScaled = false;

        try {
            Pattern pat = Pattern.compile("s([0-9]+)_z([0-9]+).*");

            String path = "house";
            String[] fileNames = ctx.getAssets().list(path);
            SortedSet<String> sortedFileNames = new TreeSet<String>(Arrays.asList(fileNames));

            int currentStep = -1;
            ToyStep currentToyStep = null;

            for(String fileName: sortedFileNames) {
                Matcher m = pat.matcher(fileName);
                if (!m.matches())
                    continue;

                String sStep = m.group(1);
                String sZ = m.group(2);
                int step = Integer.valueOf(sStep);
                int z = Integer.valueOf(sZ);

                if (step != currentStep) {
                    if (currentToyStep != null) {
                        toySteps.add(currentToyStep);
                    }
                    currentStep = step;
                    currentToyStep = new ToyStep();
                }

                InputStream is = ctx.getAssets().open(path + "/" + fileName);
                Bitmap bm = BitmapFactory.decodeStream(is, null, bmOpts);
                currentToyStep.setLayer(z, bm);
                //Log.i("Toy", "file " + fileName);
            }
            // don't forget the last step
            if (currentToyStep != null) {
                toySteps.add(currentToyStep);
            }
        }
        catch (IOException e) {
            Log.e("Toy", "OOPS " + e.toString());
        }

        Toy t = new Toy(toySteps);
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
