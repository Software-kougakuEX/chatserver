import java.net.*;
import java.io.*;
import java.util.*;

/*
 * "Name"コマンド
 */
public class NameCommand extends Command {
    public void run(String[] command, ChatClientHandler user) throws IOException {
        serverResult = command[0] + " ";
        if(command.length == 1) { //コマンドの引数が正常でないなら
            commandResult = "登録されていないコマンドです。";
            showTerminal(commandResult, serverResult, user); //ターミナルに出力
            return;
        }
        //すでに指定された名前のユーザまたはグループが存在している場合
        if(ChatServer.groupAssembly.containsKey(command[1]) == true || ChatServer.userAssembly.containsKey(command[1]) == true) {
            commandResult = "すでに'" + command[1] + "'という名前は存在します。";
        } else {
            ChatServer.userAssembly.remove(user.getUserName()); //一旦マップから消去する
            user.setUserName(command[1]); //ユーザの名前変更
            ChatServer.userAssembly.put(user.getUserName(), user); //マップに新しい名前と自身のオブジェクトを格納
            commandResult = user.getUserName();
        }
        serverResult += command[1];
        showTerminal(commandResult, serverResult, user); //ターミナルに出力
    }
}
