/**チャットプログラムを作る上で工夫した点や苦労した点**
 まずは、コマンド毎にクラスを作ったことである。
 コマンド毎にそれぞれクラスを作成して、HashMapにkeyとしてコマンド名を、valueとしてそのクラスのオブジェクトを入れておく。そして、入力されたコマンド名と同じkeyをもつMapの中のコマンドオブジェクトを取り出すことで、コマンド機能を実装した。
 以前の課題で多態性をもつプログラムというものがあったが実装しきれなかった。そのときに実装したいと思っていたので、今回の課題で実装しようと思った。HashMapの中にいれることのできるオブジェクトの型を自分で決められることは調べてわかっていた。http://www.javadrive.jp/start/hashmap/index1.html 参考
 しかし、コマンドクラスをたくさんつくっても、同じHashMapに入れることのできるオブジェクトの型は一つであるので、どうすればいいのかわからなかった。
 そこで、コマンドクラスという親クラスを作って、そのコマンドクラスを継承して、各コマンドのクラスを作った。そして、親クラスの参照変数にその親クラスを継承したクラスのオブジェクトをHashMapに入れた。そうすると、HashMapのvalueの型は親クラスの型になるのである。ここまでを考えるのに時間がかかった。
 
 また、"REJECT"コマンドや"MEMBERS"コマンド、"GROUP"コマンドは表示する名前は昇順でなければならないが、TreeMapを用いることで解決した。keyに名前を代入することで、自動的に昇順でソートされるので便利である。
 
 また、"BYE"コマンドであるが、in.close()やout.close()、socket.close()をするだけだと、ChatClientHandlerのrunメソッドのwhile文から抜けることができないのである。これに対処するためにbyeFlagを用意した。ユーザが"BYE"コマンドを実行すると、byeFlagをtrueするのである。そして、ChatClientHandlerのrunメソッドのwhile文で、byeFlagがtrueの場合breakするようにした。これで、run()メソッドから抜けることができたのである。
 
 また、全体のユーザやグループ、コマンドリストのマップをスタティックで宣言して、クラス外でスタティック参照して、利用している。これは書き方を全て統一できることや、インスタンスを作る必要もないので、どこからも簡単に呼び出せるので、便利である。しかし、セキュリティ面では危険であると考えられる。今回は時間がなく、直すことができないのが残念であるが、次からはプライベートで宣言するようにして、セキュリティ面に気をつけるようにしたい。
 
 また、//http://detail.chiebukuro.yahoo.co.jp/qa/question_detail/q1435267304 参考
 上のサイトを参考にしたのだが、例えば、ストリング型の文末に","が入っていた場合に削除するときに用いた。また、ストリング型が最初にnullを持っていた場合に後からそのオブジェクトに文字列を追加すると、最初に"null"という文字列が入ってしまうということがわかった。そこで、上の方法を用いて解決した。上の方法なら、ストリング型の文字列を好きな範囲に指定出来るのである。
 
 また、//http://d.hatena.ne.jp/yamama_lanchester/20080530/1212122258 参考
 上のサイトを参考にして、コレクション型のリストやマップの要素をfor文ですべて回すときに用いた。
 **/

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
        commandMap.put("users", new UsersCommand()); //"USERS"コマンドのオブジェクトをマップに格納
        commandMap.put("bye", new ByeCommand()); //"BYR"コマンドのオブジェクトをマップに格納
        commandMap.put("post", new PostCommand()); //"POST"コマンドのオブジェクトをマップに格納
        commandMap.put("tell", new TellCommand()); //"TELL"コマンドのオブジェクトをマップに格納
        commandMap.put("reject", new RejectCommand()); //"REJECT"コマンドのオブジェクトをマップに格納
        commandMap.put("create", new CreateCommand()); //"CREATE"コマンドのオブジェクトをマップに格納
        commandMap.put("leave", new LeaveCommand()); //"LEAVE"コマンドのオブジェクトをマップに格納
        commandMap.put("join", new JoinCommand()); //"JOIN"コマンドのオブジェクトをマップに格納
        commandMap.put("group", new GroupCommand()); //"GROUP"コマンドのオブジェクトをマップに格納
        commandMap.put("members", new MembersCommand()); //"MEMBERS"コマンドのオブジェクトをマップに格納
        commandMap.put("ban", new BanCommand()); //"BAN"コマンドのオブジェクトをマップに格納
    }
}
