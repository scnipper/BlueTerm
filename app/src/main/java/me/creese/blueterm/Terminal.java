package me.creese.blueterm;

import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Terminal extends Thread {

    private static final String TAG = Terminal.class.getSimpleName();
    private final Runtime runtime;
    private final File workDir;
    private CommandListener commandListener;
    private String currCommand;

    public Terminal() {
        runtime = Runtime.getRuntime();
        workDir = new File("/");
    }


    public void execCommand(String command) {
        currCommand = command;
        start();
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    @Override
    public void run() {
        try {
            TermProcess process = new TermProcess(runtime.exec(currCommand, null, workDir));

            InputStream inputStream = process.getProcess().getInputStream();
            OutputStream outputStream = process.getProcess().getOutputStream();
            DataInputStream data = new DataInputStream(inputStream);
            boolean isStartRead = false;
            ArrayList<Byte> byteArrayList = new ArrayList<>(4096);
            while (!process.isEnd()) {
                int available = inputStream.available();

                if (available > 0) {
                    isStartRead = true;
                    byteArrayList.add((byte) inputStream.read());


                } else if(isStartRead){
                    if (commandListener != null) {
                        byte b[] = new byte[byteArrayList.size()];

                        for (int i = 0; i < byteArrayList.size(); i++) {
                            b[i] = byteArrayList.get(i);
                        }
                        byteArrayList.clear();

                        commandListener.onCommandSuccess(new String(b));
                    }

                    isStartRead = false;
                }


            }

            if (commandListener != null) {
                commandListener.onStopProcess();
            }


        } catch (IOException e) {
            if (commandListener != null) {
                commandListener.onErrorExecute();
            }
        }
    }
}
