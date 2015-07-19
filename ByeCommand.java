import java.net.*;
import java.io.*;
import java.util.*;


/*
 * ”BYE”コマンド
 */
public class ByeCommand extends Command {
    public void run(String[] command, ChatClientHandler user) throws IOException {
        serverResult = command[0] + " ";
        commandResult = "bye " + user.getUserName();
        showTerminal(commandResult, serverResult, user); //ターミナルに出力
        //すべてのブラックリストから探索する
        //http://d.hatena.ne.jp/yamama_lanchester/20080530/1212122258 参考
        for(Map.Entry<String, ChatClientHandler> entry : ChatServer.userAssembly.entrySet()) {
            ChatClientHandler handler = entry.getValue(); //ユーザのオブジェクトを取り出す
            if(handler.findBlackList(user) == true) { //自身が誰かのブラックリストにあるかどうか
                handler.removeBlackList(user); //あるならその誰かのブラックリストから消去する
            }
        }
        //すべての禁止リストから探索する
        for(Map.Entry<String, Group> entry : ChatServer.groupAssembly.entrySet()) {
            Group group = entry.getValue(); //グループのオブジェクトを取り出す
            if(group.findBanList(user) == true) { //自身がどこかのグループの禁止リストにあるかどうか
                group.removeBanUser(user); //あるならその禁止リストから消去
            }
        }
        //参加しているグループから脱退
        for(Group entry : user.getBelongGroup()) {
            if(entry.getGroupMember().size() == 1) { //もしメンバーが空になるなら
                ChatServer.groupAssembly.remove(entry.getGroupName()); //グループを解散する
            } else {
                if(entry.getAdministrator() == user) { //脱退した自身が管理者なら
                    entry.changeAdministrator(); //管理者の変更
                }
            }
            entry.removeGroupMember(user); //グループから脱退
        }
        ChatServer.userAssembly.remove(user.getUserName()); //ユーザの集合から削除
        user.getBelongGroup().clear(); //すべて所属から排除　
        user.close();
    }
}