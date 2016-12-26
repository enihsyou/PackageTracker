import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        try {
            Socket server = new Socket("127.0.0.1", 6666);
            PrintWriter os = new PrintWriter(server.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String inputString;

            UserData data = new UserData();
            data.setHead(2);
            data.setUsermail("6666@qq");
            data.setPassword("123");

            Gson gson = new GsonBuilder().create();
            inputString = gson.toJson(data, UserData.class);
            os.println(inputString);
            os.flush();
            System.out.println("Client:" + inputString);
            System.out.println("Server:" + is.readLine());
            os.close();
            is.close();
            server.close();
        } catch (IOException e) {
            System.out.println("错误！服务器未运行！");
        }
    }
}
