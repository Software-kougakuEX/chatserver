import java.net.*;
import java.io.*;
import java.util.*;

/*
 * "HELP"コマンド
 */
public class HelpCommand extends Command {
    public void run(String[] command, ChatClientHandler user) throws IOException {
        serverResult = command[0] + " ";
        //http://d.hatena.ne.jp/yamama_lanchester/20080530/1212122258 参考
        for(Map.Entry<String, Command> entry : ChatServer.commandMap.entrySet()) {
            String name = entry.getKey(); //コマンドの名前を取り出す
            commandResult += name + ", ";
        }
        //http://detail.chiebukuro.yahoo.co.jp/qa/question_detail/q1435267304 参考
        commandResult = commandResult.substring(4, commandResult.length()-2); //末尾を削除 ", "　先頭から４番目から開始
        showTerminal(commandResult, serverResult, user); //ターミナルに出力
    }
}