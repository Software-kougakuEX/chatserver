import java.net.*;
import java.io.*;
import java.util.*;

/*
 * グループについてのクラス
 */
public class Group {
    private ArrayList<ChatClientHandler> banList = new ArrayList<ChatClientHandler>(); //禁止リスト
    private String groupName; //グループ名
    private LinkedList<ChatClientHandler> groupMember = new LinkedList<ChatClientHandler>(); //グループメンバー
    private ChatClientHandler administrator; //管理者
    
    /*
     * コンストラクタ "CREATE"コマンドにしか実行しない
     */
    public Group(String name, ChatClientHandler user) {
        this.groupName = name; //グループ名を格納
        this.administrator = user; //作成者を管理者に登録する
        addGroupMember(user); //作成者をメンバーに追加する
    }
    
    /*
     * 管理者を返す
     */
    public ChatClientHandler getAdministrator() {
        return administrator;
    }
    
    /*
     * 管理者を変更する getMemberの2番目の要素　実行されるのはグループの管理者が脱退するとき
     */
    public void changeAdministrator() {
        administrator = groupMember.get(1); //2番目に格納されているユーザを管理者にする
    }
    
    /*
     * グループネームを返す
     */
    public String getGroupName() {
        return groupName;
    }
    
    /*
     * 禁止リストを返す
     */
    public ArrayList<ChatClientHandler> getBanList() {
        return banList;
    }
    
    /*
     * 指定したユーザを禁止リストに追加
     */
    public void banUser(ChatClientHandler user) {
        banList.add(user);
    }
    
    /*
     * 指定したユーザを禁止リストから削除
     */
    public void removeBanUser(ChatClientHandler user) {
        banList.remove(user);
    }
    
    /*
     * メンバーを追加する
     */
    public void addGroupMember(ChatClientHandler user) {
        groupMember.add(user);
    }
    
    /*
     * メンバーから削除する
     */
    public void removeGroupMember(ChatClientHandler user) {
        groupMember.remove(user);
    }
    
    /*
     * メンバーを返す
     */
    public LinkedList<ChatClientHandler> getGroupMember() {
        return groupMember;
    }
    
    /*
     * 受け取ったユーザがメンバーに存在するかどうか　存在するならtrueを返す
     */
    public boolean findGroupMember(ChatClientHandler user) {
        return(groupMember.contains(user) == true);
    }
    
    /*
     * 指定されたユーザが禁止リストにいるかどうか いるならtrueを返す
     */
    public boolean findBanList(ChatClientHandler user) {
        return(banList.contains(user));
    }
}
