import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.*;
import okio.BufferedSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sleaf on 2016/12/22 0022.
 * 发送到服务器的数据仅接受第一行
 * 每次发送数据必须带head信息，如果是获取类的必须带用户名和密码。
 * 发回的数据包含的类别详见userdata类。
 * ……
 */

// TODO: 2016/12/24 0024 编写使用说明
// TODO: 2016/12/24 0024 添加邮件的方法
public class Server
{
    static String userdatapath = "./UserData";
    static File UserList = new File(userdatapath, "UserList.txt");
    static File UserTotalNumber = new File(userdatapath, "UserTotalNumber.txt");
    public static void main(String[] args)
    {
        ServerSocket SSocket;
        Socket Client;
        ExecutorService UserServerPool = Executors.newCachedThreadPool();
        try
        {
            SSocket = new ServerSocket(6666);
            while (true)
            {
                Client = SSocket.accept();
                UserServerPool.execute(new UserServer(Client));
            }
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }

    static boolean isFileExist(File file) throws IOException
    {
        if (file.exists() && file.isFile())
        {
            file.createNewFile();
            BufferedWriter bw = Files.newBufferedWriter(file.toPath());
            bw.write('0');
            bw.close();
        }
        return true;
    }
}

class UserServer implements Runnable
{
    Socket Client;
    BufferedReader in;
    OutputStream out;
    UserData data;
    UserData data2back = new UserData();

    UserServer(Socket Client)
    {
        this.Client = Client;
        System.out.println(LocalDate.now().toString() + " ---> " + LocalTime.now() + "\t用户接入：" + Client.getInetAddress());
    }

    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(Client.getInputStream()));
            out = Client.getOutputStream();
            while (!in.readLine().isEmpty())
            {
                ;
            }
            data = json.getData(in.readLine());
            switch (data.getHead())
            {
                case 1:
                    new SignUp();
                    break;
                case 2:
                    new SignIn();
                    break;
                default:
                    break;
            }
            in.close();
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    class SignUp
    {
        int TotalNumber;

        SignUp() throws Exception
        {
            data2back = data;
            BufferedWriter bw;
            try
            {
                if (isExist(data.getUsermail()))
                {
                    return;
                } else
                {
                    if (Server.isFileExist(Server.UserTotalNumber))
                    {
                        Scanner sin = new Scanner(Server.UserTotalNumber);
                        TotalNumber = sin.nextInt();
                    }
                    data2back.setId(TotalNumber);
                    bw = Files.newBufferedWriter(Server.UserTotalNumber.toPath());
                    bw.write(String.valueOf(TotalNumber++));
                    if (Server.isFileExist(Server.UserTotalNumber))
                    {
                        bw = Files.newBufferedWriter(Paths.get(Server.userdatapath + String.format("\\User-%08d.txt", data2back.getId())));
                        bw.write(json.formJson(data2back));
                    }
                    bw = Files.newBufferedWriter(Server.UserList.toPath());
                    bw.write(data2back.getUsermail() + "---id=<" + data2back.getId() + ">\n\r");
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            data2back.setSuccessful(true);
            Gson gson = new GsonBuilder().create();
            System.out.println(data2back);
            out.write(gson.toJson(data2back, UserData.class).getBytes());
//            out.write(Byte.valueOf(json.formJson(data2back)));//boolean
            out.flush();
        }


    }

    class SignIn
    {
        SignIn() throws Exception
        {
            if (isExist(data.getUsermail()))
            {
                data2back = json.getData(readfile());
                if (data.getPassword().equals(data2back.getPassword()))
                {
                    data2back.setSuccessful(true);
                } else data2back = data;
            }
            System.out.println(data2back);
            out.write(Byte.valueOf(json.formJson(data2back)));
            out.flush();
        }

        String readfile() throws IOException
        {
            String tmp;
            int id = -1;
            Scanner find = new Scanner(Server.UserList);
            outer:
            while ((tmp = find.nextLine()) != null)
            {
                Matcher matcher = Pattern.compile(data.getUsermail() + "---id=<(.+?)>").matcher(tmp);
                while (matcher.find())
                {
                    id = Integer.valueOf(matcher.group(1));
                    break outer;
                }
            }
            return Files.newBufferedReader(Paths.get(Server.userdatapath + String.format("\\User-%08d.txt", id))).readLine();
        }
    }

    boolean isExist(String usermail) throws IOException
    {
        String tmp;
        if (!Server.UserList.exists() && Server.UserList.isFile())
        {
            Path path = Server.UserList.toPath();
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return false;
        }
        Scanner find = new Scanner(Server.UserList);
        while (find.hasNext() && (tmp = find.nextLine()) != null)
        {
            if (tmp.equals(usermail))
            {
                return true;
            }
        }
        return false;
    }
}


class json
{
    static UserData getData(String jsonString)
    {
        UserData Data = new UserData();
        System.out.println(jsonString);
        Gson gson = new GsonBuilder().create();
        try
        {
            Data = gson.fromJson(jsonString, UserData.class);

//            JSONObject personObject = new JSONObject(jsonString);
//            Data.setHead(personObject.getInt("head"));//用来识别请求类型
//            Data.setId(personObject.getInt("id"));//用户id
//            Data.setUsername(personObject.getString("username"));//用户昵称
//            Data.setUsermail(personObject.getString("usermail"));//用户昵称
//            Data.setPassword(personObject.getString("password"));//用户密码
//            Data.setSuccessful(personObject.getBoolean("issuccessful"));//备注
//            Data.setRemarks(personObject.getString("remarks"));//备注
//            Data.addPackage(personObject.getString("package_id"), personObject.getString("package_info"));//快递单号及快递信息（json）
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Data;
    }

    static String formJson(UserData data) throws Exception
    {
        JSONObject json = new JSONObject();
        json.put("head", data.getHead());
        json.put("id", data.getId());
        json.put("username", data.getUsername());
        json.put("usermail", data.getUsermail());
        json.put("password", data.getPassword());
        json.put("issuccessful", data.issuccessful());
        json.put("remarks", data.getRemarks());
        if (data.getPackageList() != null)
        {
            JSONArray packageList = new JSONArray();
            for (int i = 0; i < data.getPackageList().size(); i++)
            {
                JSONObject tmpPackage = new JSONObject();
                tmpPackage.put("package_id", data.getPackageList().get(i).getId());
                tmpPackage.put("package_info", data.getPackageList().get(i).getInfo());
                packageList.put(tmpPackage);
            }
            json.put("packageList", packageList);
        }
        return json.toString();
    }
}

class UserData
{
    @SerializedName("head")
    private int head;
    @SerializedName("id")
    private int id;
    @SerializedName("user_name")
    private String username;
    @SerializedName("email")
    private String usermail;
    @SerializedName("password")
    private String password;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("is_successful")
    @Expose(deserialize = false)
    private boolean issuccessful;
    private ArrayList<KUAIDI> packageList;

    public boolean issuccessful()
    {
        return issuccessful;
    }

    public void setSuccessful(boolean successful)
    {
        issuccessful = successful;
    }

    public int getHead()
    {
        return head;
    }

    public void setHead(int head)
    {
        this.head = head;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsermail()
    {
        return usermail;
    }

    public void setUsermail(String usermail)
    {
        this.usermail = usermail;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public ArrayList<KUAIDI> getPackageList()
    {
        return packageList;
    }

    public void addPackage(String id, String info)
    {
        this.packageList.add(new KUAIDI(id, info));
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s %s %s %s", usermail, username, password, id, head);
    }
}

class KUAIDI
{
    String id;
    String info;

    public KUAIDI(String id, String info)
    {
        this.id = id;
        this.info = info;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }
}