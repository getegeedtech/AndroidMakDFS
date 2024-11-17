package makdfs.com;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    Context context;
    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }

    public String api(){
        return  "https://api.makdfs.com/";
    }

    public int randoum(int min,int max){
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        return random_int;
    }
    public String getUserid() {
        userid = sharedPreferences.getString("userid","");
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
        sharedPreferences.edit().putString("userid",userid).commit();
    }
    public String getUsername() {
        username = sharedPreferences.getString("username","");
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
        sharedPreferences.edit().putString("username",username).commit();
    }
    public String getContact() {
        contact = sharedPreferences.getString("contact","");
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
        sharedPreferences.edit().putString("contact",contact).commit();
    }
    public String getEmail() {
        email = sharedPreferences.getString("email","");
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email",email).commit();
    }
    public String getAddress() {
        address = sharedPreferences.getString("address","");
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address",address).commit();
    }
    public String getCity() {
        city = sharedPreferences.getString("city","");
        return city;
    }
    public void setCity(String city) {
        this.city = city;
        sharedPreferences.edit().putString("city",city).commit();
    }
    public String getPin() {
        pin = sharedPreferences.getString("pin","");
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
        sharedPreferences.edit().putString("pin",pin).commit();
    }
    public String getPick() {
        pick = sharedPreferences.getString("pick","");
        return pick;
    }
    public void setPick(String pick) {
        this.pick = pick;
        sharedPreferences.edit().putString("pick",pick).commit();
    }
    public String getUser() {
        user = sharedPreferences.getString("user","");
        return user;
    }
    public void setUser(String user) {
        this.user = user;
        sharedPreferences.edit().putString("user",user).commit();
    }
    public String getLogout() {
        logout = sharedPreferences.getString("logout","");
        return logout;
    }
    public void setLogout(String logout) {
        this.logout = logout;
        sharedPreferences.edit().putString("logout",logout).commit();
    }
    public String getReferid() {
        referid = sharedPreferences.getString("referid","");
        return referid;
    }
    public void setrReferid(String referid) {
        this.referid = referid;
        sharedPreferences.edit().putString("referid",referid).commit();
    }
    public String getDeviceid() {
        deviceid = sharedPreferences.getString("deviceid","");
        return deviceid;
    }
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
        sharedPreferences.edit().putString("deviceid",deviceid).commit();
    }
    public String getPassword() {
        password = sharedPreferences.getString("password","");
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
        sharedPreferences.edit().putString("password",password).commit();
    }
    public String getDob() {
        dob = sharedPreferences.getString("dob","");
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
        sharedPreferences.edit().putString("dob",dob).commit();
    }

    private String userid;
    private String username;
    private String contact;
    private String email;
    private String address;
    private String city;
    private String pin;
    private String pick;
    private String user;
    private String logout;
    private String referid;
    private String deviceid;
    private String password;
    private String dob;

    SharedPreferences sharedPreferences;
    public User(Context context){
        this.context=context;
        sharedPreferences= context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }
}
