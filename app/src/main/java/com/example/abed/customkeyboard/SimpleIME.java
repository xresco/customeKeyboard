package com.example.abed.customkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

/**
 * Created by abed on 11/3/16.
 */

public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard secureKeyboard;


    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        secureKeyboard = new Keyboard(this, R.xml.qwerty1);
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
                ic.commitText("Date "+getCurrentInputEditorInfo().packageName, 1);
                kv.setKeyboard(secureKeyboard);
                break;
            case 5:
                kv.setKeyboard(secureKeyboard);
                break;
        }

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