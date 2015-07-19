import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
    private ServerSocket server;
    public static TreeMap<String, ChatClientHandler> userAssembly = new TreeMap<String, ChatClientHandler>(); //ユーザの集合のマップ
    public static TreeMap<String, Group> groupAssembly = new TreeMap<String, Group>(); //グループの集合のマップ
    public static HashMap<String, Command> commandMap = new HashMap<String, Command>(); //コマンドのマップ
    
    public void listen() {
        try {
            server = new ServerSocket(18080);
            System.out.println("server start on port 18080");
            setCommandMap(); //コマンドオブジェクトの格納
            while(true) {
                Socket socket = server.accept(); //ユーザの接続を待つ
                int n = ChatClientHandler.getUserNum(); //何人目のユーザかの情報
                System.out.println("undefined" + n + " connected. クライアント" + n + "が接続");
                ChatClientHandler handler = new ChatClientHandler(socket); //ユーザのオブジェクトを作成
                userAssembly.put(handler.getUserName(), handler); //ユーザの集合に格納
                handler.start(); //そのユーザの処理を開始する
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
        ChatServer echo = new ChatServer();
        echo.listen();
    }
    
    /*
     * コマンドオブジェクトをマップに格納
     */
    void setCommandMap() {
        commandMap.put("help", new HelpCommand()); //"HELP"コマンドのオブジェクトをマップに格納
        commandMap.put("name", new NameCommand()); //"NAME"コマンドのオブジェクトをマップに格納
        commandMap.put("whoami", new WhoamiCommand()); //"WHOAMI"コマンドのオブジェクトをマップに格納
        commandMap.put("bye", new ByeCommand()); //"BYE"コマンドのオブジェクトをマップに格納
        commandMap.put("post", new PostCommand()); //"POST"コマンドのオブジェクトをマップに格納
    }
}
