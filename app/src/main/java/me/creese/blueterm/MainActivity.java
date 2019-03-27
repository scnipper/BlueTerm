package me.creese.blueterm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CommandListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TermView termView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Terminal terminal = new Terminal();
        terminal.setCommandListener(this);
        terminal.execCommand("whoami");

        termView = findViewById(R.id.term_view);


    }

    @Override
    public void onCommandSuccess(final String result) {
        termView.setUserName(result);

    }

    @Override
    public void onStopProcess() {

    }

    @Override
    public void onErrorExecute() {

    }
}
