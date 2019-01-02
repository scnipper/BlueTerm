package me.creese.blueterm;

public class TermProcess extends Thread {
    private final Process process;
    private boolean isEnd;
    private int exitCode;

    public TermProcess(Process process) {
        this.process = process;
        exitCode = Integer.MAX_VALUE;
        start();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public Process getProcess() {
        return process;
    }

    @Override
    public void run() {
        try {
            exitCode = process.waitFor();
            isEnd = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
