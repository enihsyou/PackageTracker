import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest
{
    public static void main(String[] args) throws IOException
    {
        Socket server = new Socket("127.0.0.1", 6666);
        String inputString;
        System.out.printf("Please input : ");
        BufferedReader Sin = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter os = new PrintWriter(server.getOutputStream());
        BufferedReader is = new BufferedReader(new InputStreamReader(server.getInputStream()));
        inputString = Sin.readLine();
        while (inputString != null && !inputString.trim().equals("quit"))
        {
            os.println(inputString);
            os.flush();
            System.out.println("Client:" + inputString);
            System.out.println("Server:" + is.readLine());
            inputString = Sin.readLine();
        }
        os.close();
        is.close();
        server.close();
    }
}
