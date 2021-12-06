package com.evandro.mykeyboard;

import static java.util.EnumSet.range;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.inputmethod.InputConnection;
import android.view.View;
import android.view.KeyEvent;
import android.media.AudioManager;

import java.util.ArrayList;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;
    private String texto, frase = "";

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playSound(primaryCode);
        onPress(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                frase = frase.replaceFirst(".$", "");
                Log.d("Caractere removido", frase);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                if (caps) {
                    caps = false;
                    kv.getKeyboard().setShifted(false);
                    kv.invalidateAllKeys();
                }
                ic.commitText(String.valueOf(code), 1);
                texto = String.valueOf(code);
                frase = frase + texto;
                Log.d("Frase criada", frase);
        }
    }

    @Override
    public void onFinishInput() {
        String[] textoSeparado = frase.split("\\s");
        if (textoSeparado.length > 2) {
            SegundoPlano segundoPlano = new SegundoPlano(frase);
            segundoPlano.execute();
            frase = "";
        } else {
            frase = "";
        }
    }

    private void playSound(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 0x20:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 0x0a:
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
    public void onPress(int vCode) {
        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vCode < 0) {
            vibrator.vibrate(50);
        } else {
            vibrator.vibrate(50);
        }

    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
}