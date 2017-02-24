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

        int z;
        int rid;
        for (z = 0; z < resourceIdsForZLayers.length; ++z) {
            rid = resourceIdsForZLayers[z];
            if (rid == 0)
                continue;
            Bitmap bm = BitmapFactory.decodeResource(ress, rid);

            int x, y;
            for (y = 0; y < bm.getHeight(); ++y) {
                // WTF, a 32x32 resource file ends up as a 96x96 bitmap!?
                if (y % 3 != 0)
                    continue;
                float yy = y / 3;

                for (x = 0; x < bm.getWidth(); ++x) {
                    if (x % 3 != 0)
                        continue;
                    float xx = x / 3;

                    int color = bm.getPixel(x, y);
                    int alpha = Color.alpha(color);
                    if (alpha > 0) {
                        float r = Color.red(color)   / 255f;
                        float g = Color.green(color) / 255f;
                        float b = Color.blue(color ) / 255f;

                        // those -16 are a crude way to center the model
                        // so that my stupid way of rotating the world along the origin makes sense
                        Block block = new Block(xx - 16, yy - 16, z, r, g, b);
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