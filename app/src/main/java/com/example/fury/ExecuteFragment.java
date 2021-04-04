package com.example.fury;

import android.graphics.Bitmap;
//Используется для связи класса Shell с фрагментами(каждый метод объяснён в классе Shell)
interface ExecuteFragment {
    void StartTransaction(String fragment);
    void StartAlphabet(int letter_choose);

}
