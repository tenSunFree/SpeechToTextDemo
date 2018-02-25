package com.example.administrator.speechtotextdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;

import com.example.administrator.speechtotextdemo.databinding.ActivityMainBinding;

public class CustomSTT implements RecognitionListener {

    private final String TAG = CustomSTT.class.getSimpleName();
    private Activity activity;
    private ActivityMainBinding mainBinding;
    private Intent intentSpeech;
    private SpeechRecognizer speechRecognizer;

    public CustomSTT(Activity activity, ActivityMainBinding mainBinding, String language) {
        this.activity = activity;
        this.mainBinding = mainBinding;
        init(language);
    }

    public void init(String language) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);                           // 创建语音识别器
        speechRecognizer.setRecognitionListener(this);                                              // 设置监听器
        intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);                     // 开启语音识别功能, ACTION_RECOGNIZE_SPEECH: 开启语音识别调用
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,                           // 设置模式 目前设置的是自由识别模式, EXTRA_LANGUAGE_MODEL: 语音识别的模式
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);                                    // LANGUAGE_MODEL_FREE_FORM	: 在一种模式上的自由语音
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);                       // 使用手機默認的語言 做為語言環境
    }

    /**
     * 开始语音识别
     */
    public void startCustomSTT() {
        if (speechRecognizer != null && intentSpeech != null) {
            speechRecognizer.startListening(intentSpeech);
        }
    }

    /**
     * 结束语音识别
     */
    public void stopCustomSTT() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
        mainBinding.textViewSpeechStatus.setText("試著說些什麼吧");
        mainBinding.textViewSpeechResult.setText(" ");
        mainBinding.buttonSpeech.setBackgroundResource(R.drawable.bg_mic2);
        mainBinding.microphoneImageView.setVisibility(View.INVISIBLE);
        onRmsChanged(1);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
        mainBinding.buttonSpeech.setBackgroundResource(R.drawable.bg_mic);
        mainBinding.microphoneImageView.setVisibility(View.VISIBLE);
        stopCustomSTT();
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "onError");
        mainBinding.textViewSpeechStatus.setText("找不到相符結果, 請再試一次");
        if (error == 8) {
            activity.recreate();
        }
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults");
        mainBinding.textViewSpeechResult.setText(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
        mainBinding.textViewSpeechStatus.setText("輕觸即可開始說話");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent");
    }
}
