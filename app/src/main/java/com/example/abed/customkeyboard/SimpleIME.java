package com.example.abed.customkeyboard;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abed on 11/3/16.
 */

public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard creditCardPassword;
    private Keyboard userNamePassowrd;


    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        userNamePassowrd = new Keyboard(this, R.xml.kb_username_password);
        creditCardPassword = new Keyboard(this, R.xml.qwerty1);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);


        return kv;
    }

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onWindowShown() {
        super.onWindowShown();
        kv.setKeyboard(keyboard);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case 1:
                CharSequence currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;
                CharSequence beforCursorText = ic.getTextBeforeCursor(currentText.length(), 0);
                CharSequence afterCursorText = ic.getTextAfterCursor(currentText.length(), 0);
                ic.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                break;
            case 2:
                ic.commitText("CardNum", 1);
                break;
            case 3:
                ic.commitText("CCV", 1);
                break;
            case 4:
                ic.commitText("Date", 1);
                break;
            case 5:
                kv.setKeyboard(userNamePassowrd);
                break;
            case 9:
                kv.setKeyboard(keyboard);
                break;
            case 21:
                ic.commitText(getCredential(getCurrentInputEditorInfo().packageName).username, 1);
                break;
            case 22:
                ic.commitText(getCredential(getCurrentInputEditorInfo().packageName).password, 1);
                break;
        }


    }

    private Credentials getCredential(String packageName) {
        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Credentials> credentialsList;
        String jsonPreferences = sharedPreferences.getString("KEY", null);

        Gson gson = new Gson();
        if (jsonPreferences != null) {
            Type type = new TypeToken<List<Credentials>>() {
            }.getType();
            credentialsList = gson.fromJson(jsonPreferences, type);
        } else {
            credentialsList = new ArrayList<>();
        }
        for (Credentials credentials : credentialsList) {
            if (packageName.contains(credentials.app))
                return credentials;
        }
        return new Credentials("", "username", "password");
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}