package me.creese.blueterm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class TermView extends View {

    private static final String TAG = TermView.class.getSimpleName();
    private Paint paint;
    private int widthTerminal;
    private float oneSymbolWidth;
    private ArrayList<char[]> lines;
    private int currNumLine;
    private int cursorPos;
    private String userName;

    public TermView(Context context) {
        super(context);
        init();
    }

    public TermView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TermView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TermView(Context context,  @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "term_font.ttf");
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        paint.setColor(Color.WHITE);
        widthTerminal = 30;
        currNumLine = 0;
        cursorPos = 0;
        lines = new ArrayList<>();
        lines.add(new char[widthTerminal]);


    }


    public void setUserName(String userName) {
        userName+="@:/";
        this.userName = userName;
        setText(userName);

    }

    public void setText(String text) {


        int lengthText = text.length();

        if(lengthText > widthTerminal) {
            int countLines = (int) Math.ceil(lengthText / (float) widthTerminal);

            int srcStart = 0;
            for (int i = 0; i < countLines; i++) {
                char[] chars = lines.get(currNumLine);
                //if(lengthText > widthTerminal) lengthText-=widthTerminal;

                text.getChars(srcStart,srcStart+
                        (i == countLines-1 ? lengthText- (widthTerminal * i) : widthTerminal ),chars,cursorPos);
                srcStart+=widthTerminal;

                cursorPos = 0;

                lines.add(new char[widthTerminal]);
                currNumLine++;
            }
        } else {
            char[] chars = lines.get(currNumLine);
            text.getChars(0,lengthText,chars,cursorPos);
            iterateCursor(text.length());
        }
    }

    public void showKeyboard(boolean show) {
        Log.w(TAG, "showKeyboard: " );

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    private void iterateCursor(int length) {
        cursorPos+=length;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        // конфигурация параметров клавиатуры
        outAttrs.inputType = InputType.TYPE_NULL;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        outAttrs.initialSelEnd = outAttrs.initialSelStart = 0;

        // создание своего коннектора для клавиатуры
        return new BaseInputConnection(this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.w(TAG, "onKeyDown: "+keyCode );

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //requestFocus();
                showKeyboard(true);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);


        oneSymbolWidth = canvas.getWidth() / (float)widthTerminal;
        oneSymbolWidth*=1.67;
        paint.setTextSize(oneSymbolWidth);


        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i);
            canvas.drawText(line,0,line.length,0,oneSymbolWidth* (i+1),paint);
        }




    }
}
