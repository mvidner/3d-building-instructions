package net.vidner.threedbuildinginstructions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvidner on 24.2.17.
 */

public class ToyStep {
    private List<Block> mBlocks;

    public ToyStep(Context ctx, int[] resourceIdsForZLayers) {
        mBlocks = new ArrayList<Block>();
        Resources ress = ctx.getResources();
        // do not rescale the bitmap according to display dpi
        BitmapFactory.Options bmOpts = new BitmapFactory.Options();
        bmOpts.inScaled = false;

        int z;
        int rid;
        for (z = 0; z < resourceIdsForZLayers.length; ++z) {
            rid = resourceIdsForZLayers[z];
            if (rid == 0)
                continue;
            Bitmap bm = BitmapFactory.decodeResource(ress, rid, bmOpts);

            int x, y;
            for (y = 0; y < bm.getHeight(); ++y) {
                for (x = 0; x < bm.getWidth(); ++x) {
                    int color = bm.getPixel(x, y);
                    int alpha = Color.alpha(color);
                    if (alpha > 0) {
                        float r = Color.red(color)   / 255f;
                        float g = Color.green(color) / 255f;
                        float b = Color.blue(color ) / 255f;

                        // those -16 are a crude way to center the model
                        // so that my stupid way of rotating the world along the origin makes sense
                        Block block = new Block(x - 16, y - 16, z, r, g, b);
                        mBlocks.add(block);
                    }
                }
            }
        }
    }

    public void draw(float[] mvpMatrix) {
        for (Block block: mBlocks) {
            block.draw(mvpMatrix);
        }
    }
}