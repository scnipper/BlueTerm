package me.creese.blueterm;

public interface CommandListener {
    void onCommandSuccess(String result);
    void onStopProcess();
    void onErrorExecute();
}
