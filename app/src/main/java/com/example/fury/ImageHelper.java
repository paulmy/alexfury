package com.example.fury;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageHelper {
    Context context;

    public ImageHelper(Context context) {
        this.context = context;
    }

    Bitmap openFile(String file_name) {
        Bitmap bitmap = null;
        FileInputStream fin = null;
        try {
            fin = context.openFileInput(file_name);
            byte[] bytes = new byte[fin.available()];
            final int i = fin.read(bytes);
            if (i != -1) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } catch (IOException e) {
            //Если картинки не существует - открыть активность предзагрузки
            Intent intent = new Intent(context.getApplicationContext(), Preloading.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ignored) {
            }
        }
        return bitmap;
    }

    Bitmap Resize(int Res, float width_req, float height_req, boolean corp) {
        //Функция пережимания изображения под нужное разрешение экрана
        //Определение размера изображения и насколько его можно сжать без деформации
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), Res, options);
        float heightRatio = (float) options.outHeight / height_req;
        float widthRatio = (float) options.outWidth / width_req;
        //Сжатие по SampleSize, если нечётное число - округляется до ближайшего чётного
        if (corp)
            options.inSampleSize = heightRatio < widthRatio ? (int) heightRatio : (int) widthRatio;
        else
            options.inSampleSize = heightRatio > widthRatio ? (int) heightRatio : (int) widthRatio;
        options.inJustDecodeBounds = false;
        options.inPreferQualityOverSpeed = true;
        //"Дожатие" изображения. Этот метод доводит до нужного размера картинку.(Используется после SampleSize, так как картинка уже выгружена в память)
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Res, options);
        heightRatio = (float) bitmap.getHeight() / height_req;
        widthRatio = (float) bitmap.getWidth() / width_req;
        float resize;
        if (corp)
            resize = heightRatio < widthRatio ? heightRatio : widthRatio;
        else
            resize = heightRatio > widthRatio ? heightRatio : widthRatio;
        heightRatio = (float) bitmap.getHeight() / resize;
        widthRatio = (float) bitmap.getWidth() / resize;
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) widthRatio, (int) heightRatio, true);
        if (corp)
            //Обрезка
            return corp(bitmap, (int) width_req, (int) height_req);
        else return bitmap;
    }

    private Bitmap corp(Bitmap bitmap, int width_corp, int height_corp) {
        //Расчитывание нужных рамеров
        int width_req = bitmap.getWidth() < width_corp ? bitmap.getWidth() : width_corp;
        int height_req = bitmap.getHeight() < height_corp ? bitmap.getHeight() : height_corp;
        Bitmap corpBitmap = Bitmap.createBitmap(width_req, height_req,
                Bitmap.Config.RGB_565);
        //Обрезка
        int[] pixels = new int[width_req * height_req];
        bitmap
                .getPixels(pixels, 0, width_req, 0, 0, width_req, height_req);
        corpBitmap
                .setPixels(pixels, 0, width_req, 0, 0, width_req, height_req);
        return corpBitmap;
    }
}
