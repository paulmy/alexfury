package com.example.fury;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

//Предзагрузка
public class Preloading extends Activity {
    private final Thread[] thread = new Thread[2];
    private TextView text;
    private ProgressBar progressBar;
    private int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Если Android 4.4 - включить IMMERSIVE MODE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        setContentView(R.layout.preloading);
        text = (TextView) findViewById(R.id.text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Определение разрешения экрана
        if (Build.VERSION.SDK_INT >= 17) {
            Point size = new Point();
            this.getWindowManager().getDefaultDisplay().getRealSize(size);
            width = size.x;
            height = size.y;

        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
        progressBar.setMax(46);//46
        execute();
        //Loading loading = new Loading();
        //loading.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Запись времени выхода(для родительского контроля)
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putLong("Exit", new Date().getTime()).apply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Если Android 4.4 - включить IMMERSIVE MODE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus)
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //для блокировки от случайных нажатий
        return true;
    }

    private void execute() {
        //Запуск задач, которые нужны для запуска остальных фрагментов
        thread[0] = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TextSize();
                    database();
                    //Когда все потоки завершили обработку - продолжается переход
                } catch (Exception ignored) {
                }
            }
        });
        thread[0].setDaemon(true);
        thread[0].setPriority(Thread.MAX_PRIORITY);
        thread[0].start();
        thread[1] = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    background();
                    button();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread[1].setDaemon(true);
        thread[1].setPriority(Thread.MAX_PRIORITY);
        thread[1].start();
    }

    //Все ниже методы выполняются в фоне
    private void TextSize() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
        editor.remove("Range");
        editor.remove("attemps");
        editor.remove("ParentControl");
        editor.putInt("Title", (int) (height / 13.0));
        editor.putInt("UnderPictures", (int) ((height - (height / 10.0)) / 30.0));
        editor.putInt("Char", (int) (height - (height / 10.0)) / 8);
        editor.apply();
        complete();
    }

    private void database() {
        complete();

    }

    private void background() throws IOException {
        //Удаление файлов от старой версии игры
        File file = new File(String.valueOf(getApplicationContext().getFilesDir()));
        String[] files = file.list();
        for (String File : files) {
            getApplicationContext().deleteFile(File);
        }
        //Открытие файа для записи
        FileOutputStream fos;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //Сжатие
        Bitmap bitmap = new ImageHelper(this).Resize(R.drawable.room, width, height, true);
        complete();
        //Сохранение в файл
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        fos = openFileOutput("room.png", MODE_PRIVATE);
        fos.write(stream.toByteArray());
        complete();
        //Удаление кэша
        bitmap.recycle();
        System.gc();

        stream.reset();
        //Полностью повторение вышеописанного, но другой картинки


        //Создание фонов, для которых важно расположение элементов

    }

    private void button() throws IOException {
        //Открытие файа для записи
        FileOutputStream fos;
    }

    synchronized void complete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progressBar.getProgress() + 1);
                text.setText(100 / progressBar.getMax() * progressBar.getProgress() + "%");
                Log.d("Progress", String.valueOf(progressBar.getProgress()));
                if (progressBar.getProgress() == progressBar.getMax()) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    System.exit(0);
                }
            }
        });
    }
}
