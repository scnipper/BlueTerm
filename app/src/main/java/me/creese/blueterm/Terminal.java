package me.creese.blueterm;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
            InputStream errorStream = process.getProcess().getErrorStream();
            OutputStream outputStream = process.getProcess().getOutputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            DataInputStream dataErrStream = new DataInputStream(errorStream);

            boolean isStartRead = false;
            StringBuilder outStr = new StringBuilder();
            while (!process.isEnd()) {
                int available = inputStream.available();
                int errorAvailable = errorStream.available();


                if (errorAvailable > 0) {

                    byte[] bytes = new byte[errorAvailable];
                    dataErrStream.readFully(bytes);

                    if (commandListener != null) {

                        commandListener.onCommandSuccess(new String(bytes));


                    }


                }
                if (available > 0) {
                    isStartRead = true;
                    byte[] bytes = new byte[available];
                    dataInputStream.readFully(bytes);

                    /*int n = 0;
                    while (n < available) {
                        int count = inputStream.read(bytes, n, available - n);
                        if (count < 0)
                            throw new EOFException();
                        n += count;
                    }
*/
                    for (byte aByte : bytes) {
                        if(aByte == 0x1b) {
                            System.out.println("lol");
                        }
                    }
                    if (commandListener != null) {

                        commandListener.onCommandSuccess(new String(bytes));


                    }


                } else continue;


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
