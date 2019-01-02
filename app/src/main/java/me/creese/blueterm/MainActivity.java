package me.creese.blueterm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CommandListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Terminal terminal = new Terminal();
        terminal.setCommandListener(this);
        terminal.execCommand("top");

        text = findViewById(R.id.text_comand);
    }

    @Override
    public void onCommandSuccess(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(result);
            }
        });

    }

    @Override
    public void onStopProcess() {

    }

    @Override
    public void onErrorExecute() {

    }
}
