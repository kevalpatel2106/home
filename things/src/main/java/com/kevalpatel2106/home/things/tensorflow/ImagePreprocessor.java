package com.kevalpatel2106.home.things.tensorflow;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.Image;


import com.kevalpatel2106.home.things.ImageUtils;

import junit.framework.Assert;

/**
 * Created by Keval Patel on 19/04/17.
 * Class that process an Image and extracts a Bitmap in a format appropriate for
 * TensorFlowImageClassifier
 *
 * @author 'https://github.com/androidthings/sample-tensorflow-imageclassifier/blob/master/app/src/main/java/com/example/androidthings/imageclassifier/env/ImageUtils.java'
 */
public class ImagePreprocessor {
    private static final boolean SAVE_PREVIEW_BITMAP = false;

    private Bitmap rgbFrameBitmap;
    private Bitmap croppedBitmap;

    private byte[][] cachedYuvBytes;
    private int[] cachedRgbBytes;

    public ImagePreprocessor(int inputImageWidth, int inputImageHeight, int outputSize) {
        this.cachedRgbBytes = new int[inputImageWidth * inputImageHeight];
        this.cachedYuvBytes = new byte[3][];
        this.croppedBitmap = Bitmap.createBitmap(outputSize, outputSize, Config.ARGB_8888);
        this.rgbFrameBitmap = Bitmap.createBitmap(inputImageWidth, inputImageHeight, Config.ARGB_8888);
    }

    public Bitmap preprocessImage(final Image image) {
        if (image == null) {
            return null;
        }

        Assert.assertEquals("Invalid size width", rgbFrameBitmap.getWidth(), image.getWidth());
        Assert.assertEquals("Invalid size height", rgbFrameBitmap.getHeight(), image.getHeight());

        cachedRgbBytes = ImageUtils.convertImageToBitmap(image, cachedRgbBytes, cachedYuvBytes);

        if (croppedBitmap != null && rgbFrameBitmap != null) {
            rgbFrameBitmap.setPixels(cachedRgbBytes, 0, image.getWidth(), 0, 0,
                    image.getWidth(), image.getHeight());
            ImageUtils.cropAndRescaleBitmap(rgbFrameBitmap, croppedBitmap, 0);
        }

        image.close();

        // For debugging
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }
        return croppedBitmap;
    }
}