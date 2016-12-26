import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.APPEND;

/**
 * Created by Sleaf on 2016/12/22 0022.
 * *******************************************
 * 发送到服务器的数据仅接受第一行
 * 每次发送数据必须带head信息，如果是获取类的必须带用户名和密码。
 * 发回的数据包含的类别详见userdata类。
 * ……
 * *******************************************
 */

// TODO: 2016/12/24 0024 添加快递的方法

public class Server {
    static String datapath = "./Data";
    static String userdatapath = "./Data/Userdata";
    static File UserList = new File(datapath, "UserList.txt");
    static File UserTotalNumber = new File(datapath, "UserTotalNumber.txt");

    public static void main(String[] args) {
        ServerSocket SSocket;
        Socket Client;
        ExecutorService UserServerPool = Executors.newCachedThreadPool();
        System.out.println(
            LocalDate.now().toString() + " -> " + LocalTime.now() + " -> " + "服务器已启动！");
        try {
            SSocket = new ServerSocket(6666);
            while (true) {
                Client = SSocket.accept();
                UserServerPool.execute(new UserServer(Client));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    static void letFileExist(File file) throws IOException {
        if (!(file.exists() && file.isFile())) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            if (!file.equals(UserList)) {
                BufferedWriter bw = Files.newBufferedWriter(file.toPath());
                bw.write('0');
                bw.close();
            }

        }
    }
}

class UserServer implements Runnable {
    int TotalNumber;
    Socket Client;
    BufferedReader in;
    OutputStream out;
    UserData data;
    UserData data2back = new UserData();

    UserServer(Socket Client) {
        this.Client = Client;
        System.out.println(
            LocalDate.now().toString() + " -> " + LocalTime.now() + " -> " + "\t用户接入："
                + Client.getInetAddress());
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(Client.getInputStream()));
            out = Client.getOutputStream();
            data = json.getData(in.readLine());
            if (data.getUsermail() != null && data.getPassword() != null && !data.isSuccessful()) {
                switch (data.getHead()) {
                    case 0:
                        SignUp();
                        break;
                    case 1:
                        SignIn();
                        break;
                    case 2:
                        Addpackage();
                    default:
                        break;
                }
            }
            Gson gson = new GsonBuilder().create();
            out.write(gson.toJson(data2back, UserData.class).getBytes());
            out.flush();
            in.close();
            out.close();
            System.out.println(
                LocalDate.now().toString() + " -> " + LocalTime.now() + " -> " + "From client("
                    + Client.getInetAddress() + "):"
                    + String.format("HEAD:%s\tUSERMAIL:%s\tRESULT:%s\t\tLIST:%s", data.getHead(), data.getUsermail(), data2back.isSuccessful(), data2back.getPackageList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void SignUp() throws Exception {
        data2back = data;
        BufferedWriter bw;
        if (isExist(data.getUsermail()) != -1) {
            return;
        } else {
            try {
                Server.letFileExist(Server.UserTotalNumber);
                Scanner sin = new Scanner(Server.UserTotalNumber);
                TotalNumber = sin.nextInt();
                data2back.setId(TotalNumber);
                Server.letFileExist(Paths.get(Server.userdatapath
                    + String.format("/User-%08d.txt", data2back.getId())).toFile());
                bw = Files.newBufferedWriter(Paths.get(Server.userdatapath
                    + String.format("/User-%08d.txt", data2back.getId())));
                bw.write(json.formJson(data2back));
                bw.close();
                bw = Files.newBufferedWriter(Server.UserList.toPath(), APPEND);
                bw.write(data2back.getUsermail() + "---id=<" + data2back.getId() + ">\r\n");
                bw.close();
                bw = Files.newBufferedWriter(Server.UserTotalNumber.toPath());
                bw.write(String.valueOf(++TotalNumber));
                bw.close();
                data2back.setSuccessful(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    boolean SignIn() throws Exception {
        int id;
        if ((id = isExist(data.getUsermail())) != -1) {
            Server.letFileExist(new File(
                Server.userdatapath + String.format("/User-%08d.txt", id)));
            data2back = json.getData(Files.newBufferedReader(Paths.get(
                Server.userdatapath + String.format("/User-%08d.txt", id))).readLine());
            if (data.getPassword().equals(data2back.getPassword())) {
                data2back.setSuccessful(true);
            } else { data2back = data; }
            return true;
        }
        return false;
    }

    void Addpackage() throws Exception {
        if (SignIn() && !data.getPackageList().isEmpty()) {
            for (KUAIDI kuaidiform : data.getPackageList()) {
                data2back.addPackage(kuaidiform);
            }
            BufferedWriter bw;
            bw = Files.newBufferedWriter(Paths.get(Server.userdatapath
                + String.format("/User-%08d.txt", data2back.getId())));
            bw.write(json.formJson(data2back));
            bw.close();
            data2back.setSuccessful(true);
        } else { data2back.setSuccessful(false); }
    }

    int isExist(String usermail) throws IOException {//检测用户是否存在，存在则返回id,不存在则返回-1
        String[] tmp;
        String mail;
        Server.letFileExist(Server.UserList);
        Scanner find = new Scanner(Server.UserList);
        while (find.hasNext() && (tmp = find.nextLine().split("---")) != null) {
            mail = tmp[0];
            if (mail.equals(usermail)) {
                Matcher matcher =
                    Pattern.compile("<(.+?)>").matcher(tmp[1]);
                if (matcher.find()) { return Integer.valueOf(matcher.group(1)); } else return -1;
            }
        }
        return -1;
    }
}


class json {
    static UserData getData(String jsonString) {
        UserData data = new UserData();
        Gson gson = new GsonBuilder().create();
        try {
            data = gson.fromJson(jsonString, UserData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    static String formJson(UserData data) throws Exception {
        JSONObject json = new JSONObject();
        json.put("head", data.getHead());
        json.put("id", data.getId());
        json.put("username", data.getUsername());
        json.put("usermail", data.getUsermail());
        json.put("password", data.getPassword());
        json.put("issuccessful", data.isSuccessful());
        json.put("remarks", data.getRemarks());
        if (data.getPackageList() != null) {
            JSONArray packageList = new JSONArray();
            for (int i = 0; i < data.getPackageList().size(); i++) {
                JSONObject tmpPackage = new JSONObject();
                tmpPackage.put("package_id", data.getPackageList().get(i).getPackage_id());
                tmpPackage.put("package_info", data.getPackageList().get(i).getPackage_info());
                packageList.put(tmpPackage);
            }
            json.put("packageList", packageList);
        }
        return json.toString();
    }
}

class UserData {
    @SerializedName("head")
    private int head = -1;
    @SerializedName("id")
    private int id = -1;
    @SerializedName("username")
    private String username;
    @SerializedName("usermail")
    private String usermail;
    @SerializedName("password")
    private String password;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("isSuccessful")
    @Expose(deserialize = false)
    private boolean isSuccessful = false;
    @SerializedName("packageList")
    private ArrayList<KUAIDI> packageList = new ArrayList<>();

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<KUAIDI> getPackageList() {
        return packageList;
    }

    public void addPackage(KUAIDI kuaidi) {
        if (this.getPackageList().isEmpty()) {
            packageList.add(kuaidi);
        } else {
            for (KUAIDI kuaidiform : packageList) {
                if (kuaidiform.getPackage_id().equals(kuaidi.getPackage_id())) {
                    packageList.remove(kuaidiform);
                    break;
                }
            }
            packageList.add(kuaidi);
        }
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}

class KUAIDI {
    String package_id;
    String package_info;

    public KUAIDI(String id, String info) {
        this.package_id = id;
        this.package_info = info;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_info() {
        return package_info;
    }

    public void setPackage_info(String package_info) {
        this.package_info = package_info;
    }
}