import java.net.*;
import java.io.*;
import java.util.*;

/*
 * コマンドクラスのsuperクラス
 */
public class Command {
    protected String commandResult;
    protected String serverResult;
    protected void run(String[] command, ChatClientHandler user) throws IOException {}
    protected void start(String[] command, ChatClientHandler user) {
        commandResult = null; //ターミナルに表示する結果
        serverResult = null; //ターミナルに表示する入力されたコマンド
        try {
            run(command, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * コマンドを入力した結果をターミナルに出力する
     */
    protected void showTerminal(String commandResult, String serverResult, ChatClientHandler user) throws IOException {
        user.out.write(commandResult + "\r\n"); //ユーザで表示する
        user.out.flush();
        System.out.println("クライアント" + user.getUserId() + "(" + user.getUserName() + "): " + serverResult.trim() + ": " + commandResult); //サーバーに結果を表示する。誰が実行したかと結果
    }
}
