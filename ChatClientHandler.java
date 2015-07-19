import java.net.*;
import java.io.*;
import java.util.*;

/*
 * 各ユーザの処理を行う
 */
public class ChatClientHandler extends Thread {
    private Socket socket; //ソケット
    public BufferedReader in;
    public BufferedWriter out;
    
    private boolean byeFlag = false; //byeコマンドのフラグ　trueならrun()を終了する
    private InetAddress address;
    private static int userNum = 1; //何番目のユーザかどうか
    private int userId; //ユーザ自身が覚えているid
    private final String defaultName = "undefined"; //デフォルト名
    private String userName; //ユーザ名
    private ArrayList<ChatClientHandler> blackList = new ArrayList<ChatClientHandler>(); //ブラックリスト
    private ArrayList<Group> belongGroup = new ArrayList<Group>(); //参加しているグループ
    
    public ChatClientHandler(Socket socket) {
        this.socket = socket;
        this.userId = userNum;
        this.userName = defaultName + String.valueOf(userNum); //名前の初期値
        this.address = socket.getInetAddress();
        userNum++; //ユーザ番号を増やす
    }
    
    public void run(){
        try{
            open();
            while(true){
                out.write("> ");
                out.flush();
                String message = receive();
                if(message.equals("")) break;
                String[] command; //入力されたコマンドを格納する
                command = message.split(" "); //コマンドは１番目　" "で区切って配列に代入する
                Command commandObject;
                commandObject =  ChatServer.commandMap.get(command[0].toLowerCase()); //入力されたコマンド名と同じkeyをもつコマンドオブジェクトを取り出す。また、大文字を小文字に変換する。
                
                if(ChatServer.commandMap.containsKey(command[0].toLowerCase()) == false) { //コマンドマップにないなら
                    String commandResult = "登録されていないコマンドです。";
                    send(commandResult, command[0]);
                } else {
                    commandObject.start(command, this); //取り出したコマンドオブジェクトを実行する
                }
                if(byeFlag == true) {
                    break; //"BYE"コマンドが実行されたらrun()から抜ける
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
     * inやoutなどの入出力ストリームを開く
     */
    private void open() throws IOException{
        InputStream socketIn = socket.getInputStream();
        OutputStream socketOut = socket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(socketIn));
        out = new BufferedWriter(new OutputStreamWriter(socketOut));
    }
    
    /*
     * 閉じる　クライアントの処理の終了
     */
    public void close() throws IOException {
        byeFlag = true; //フラグを建てる
        in.close();
        out.close();
        socket.close();
    }
    
    /*
     * コマンドを受け取る
     */
    private String receive() throws IOException {
        String message = in.readLine(); //入力をまつ
        return message;
    }
    
    /*
     * コマンドを入力した結果をターミナルに出力する
     */
    private void send(String commandResult, String serverResult) throws IOException {
        out.write(commandResult + "\r\n");
        out.flush();
        System.out.println("クライアント" + getUserId() + "(" + getUserName() + "): " + serverResult.trim() + ": " + commandResult); //サーバーに結果を表示する。誰が実行したかと結果
    }
    
    /*
     * 指定したユーザをブラックリストに追加 引数は追加するユーザのオブジェクト
     */
    public void rejectUser(ChatClientHandler user) {
        blackList.add(user);
    }
    
    /*
     * 指定したユーザをブラックリストから削除 引数は削除するユーザのオブジェクト
     */
    public void removeBlackList(ChatClientHandler user) {
        blackList.remove(user);
    }
    
    /*
     * userIdを返す
     */
    public int getUserId() {
        return this.userId;
    }
    
    /*
     * アドレスを返す
     */
    public InetAddress getAddress() {
        return address;
    }
    
    /*
     * ユーザ番号を返す
     */
    public static int getUserNum() {
        return userNum;
    }
    
    /*
     * ユーザに名前をつける
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /*
     * ユーザの名前を返す
     */
    public String getUserName() {
        return userName;
    }
    
    /*
     * 所属しているグループを返す
     */
    public ArrayList<Group> getBelongGroup() {
        return belongGroup;
    }
    
    /*
     *　所属しているグループのリストからグループを削除する 引数は削除するグループ
     */
    public void removeBelongGroup(Group group) {
        belongGroup.remove(group);
    }
    
    /*
     * 所属しているグループのリストにグループを追加する 引数は追加するグループ
     */
    public void addBelongGroup(Group group) {
        belongGroup.add(group);
    }
    
    /*
     * メッセージを受け取って表示する 引数はメッセージ, 送信者の名前 "POST"コマンドの場合
     */
    public void getPostMessage(String message, String name) throws IOException {
        out.write(name + ": " + message + "\r\n");
        out.flush();
        out.write("> "); //次のコマンド入力
        out.flush();
    }
    
    /*
     * メッセージを受け取って表示する 引数はメッセージ, 送信者の名前 "TELL"コマンドの場合
     */
    public void getTellMessage(String message, String name) throws IOException {
        //投稿者-≥ 投稿先
        out.write(name + " -≥ " + getUserName() + ": " + message + "\r\n");
        out.flush();
        out.write("> "); //次のコマンド入力
        out.flush();
    }
    
    /*
     * 指定されたユーザがブラックリストにいるかどうか いるならtrueを返す
     */
    public boolean findBlackList(ChatClientHandler user) {
        return(blackList.contains(user));
    }
    
    /*
     * ブラックリストにユーザが登録されていないかどうか 空ならtrueを返す
     */
    public boolean emptyBlackList() {
        return(blackList.isEmpty()); //空ならtrue
    }
    
    /*
     * ブラックリストを返す
     */
    public ArrayList<ChatClientHandler> getBlackList() {
        return blackList;
    }
    
    /*
     * 指定した受信先が存在するかどうか
     * 存在しない場合エラーメッセージを返す
     */
    public String emptyUser(String name) {
        String result;
        if(ChatServer.userAssembly.containsKey(name) != true) { //ユーザが存在しない場合
            result = "指定された'" + name + "'という名前の受信先は存在しません。";
            return result;
        }
        return null; //存在するならnullを返す
    }
}
