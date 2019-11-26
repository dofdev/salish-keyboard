package com.dofdev.salishkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.Keyboard;
import android.os.Vibrator;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private Vibrator myVib;
    private KeyboardView keyboardView;
    private boolean mainView;
    @Override
    public View onCreateInputView() {
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        // get the KeyboardView and add our Keyboard layout to it
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.main_pad);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setPreviewEnabled(false);
        mainView = true;
        return keyboardView;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        myVib.vibrate(20);
        switch (primaryCode) {
            case Keyboard.KEYCODE_MODE_CHANGE:
                Keyboard keyboard = new Keyboard(this, R.xml.main_pad);
                if (mainView)
                {
                    keyboard = new Keyboard(this, R.xml.special_pad);
                    mainView = false;
                }
                else
                {
                    mainView = true;
                }
                keyboardView.setKeyboard(keyboard);
                break;
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1, 0);
                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                break;
            default:
                char code = (char) primaryCode;
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onPress(int primaryCode) { }

    @Override
    public void onRelease(int primaryCode) { }

    @Override
    public void onText(CharSequence text) { }

    @Override
    public void swipeLeft() { }

    @Override
    public void swipeRight() { }

    @Override
    public void swipeDown() { }

    @Override
    public void swipeUp() { }
}
