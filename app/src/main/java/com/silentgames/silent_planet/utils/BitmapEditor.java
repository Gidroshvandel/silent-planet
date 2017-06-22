package com.silentgames.silent_planet.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.silentgames.silent_planet.logic.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class BitmapEditor {
    public enum RotateAngle {
        DEGREES0,
        DEGREES90,
        DEGREES180,
        DEGREES360;

        private static final List<RotateAngle> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static RotateAngle randomAngle()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    public static Bitmap resize(Bitmap bit, float newWidth, float newHeight) {

        int width = bit.getWidth();
        int height = bit.getHeight();
        float scaleWidth = (newWidth) / width;
        float scaleHeight = (newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bit, 0, 0,
                width, height, matrix, true);
        return resizedBitmap;
    }

    public static Bitmap getEntityBitmap(int DefaultBitId, Resources res){
        Bitmap bitmap = BitmapFactory.decodeResource(res, DefaultBitId);
        return resize(bitmap, Converter.convertDpToPixel(Constants.entitySize,res),Converter.convertDpToPixel(Constants.entitySize,res));
    }

    public static Bitmap getCellBitmap(int DefaultBitId, Resources res){
        Bitmap bitmap = BitmapFactory.decodeResource(res, DefaultBitId);
        return resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res));
    }

    public static Bitmap rotateBitmap(RotateAngle rotateAngle, Bitmap bitmap){

        Matrix matrix = new Matrix();

        switch (rotateAngle){
            case DEGREES90:
                matrix.postRotate(90);
                break;
            case DEGREES180:
                matrix.postRotate(180);
                break;
            case DEGREES360:
                matrix.postRotate(360);
                break;
        }
        return Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


}
