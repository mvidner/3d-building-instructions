package net.vidner.threedbuildinginstructions;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvidner on 24.2.17.
 */

public class ToyStep {
    private List<Block> mBlocks;

    public ToyStep() {
        mBlocks = new ArrayList<Block>();
        // must be filled with setLayer
    }

    public void setLayer(int z, Bitmap bm) {
        // FIXME: only adds blocks, does not replace any blocks that might already be in that layer
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

    public void draw(float[] mvpMatrix) {
        for (Block block: mBlocks) {
            block.draw(mvpMatrix);
        }
    }
}