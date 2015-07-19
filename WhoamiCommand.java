import java.net.*;
import java.io.*;
import java.util.*;

/*
 * "WHOAMI"コマンド
 */
public class WhoamiCommand extends Command {
    public void run(String[] command, ChatClientHandler user) throws IOException {
        serverResult = command[0] + " ";
        commandResult = user.getUserName(); //ユーザの名前を取り出す
        showTerminal(commandResult, serverResult, user); //ターミナルに出力
    }
}