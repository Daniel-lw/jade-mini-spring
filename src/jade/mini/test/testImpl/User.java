package jade.mini.test.testImpl;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: liwei46
 * Date: 14-9-15
 * Time: 上午8:38
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private long id;
    private String userName;
    private String password;
    private int source;//0默认值,表示本站注册用户，1表示qq用户，2表示人人用户
    private Date createTime;
    private long regIp;
    private long lastIp;
    private long schoolId;
    private int type; //0默认值表示普通用户
    private long rrUid;
    private String qqOpenId;
    public static int SOURCE_LIFE = 0;
    public static int SOURCE_QQ = 1;
    public static int SOURCE_RENREN = 2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRrUid() {
        return rrUid;
    }

    public void setRrUid(long rrUid) {
        this.rrUid = rrUid;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getRegIp() {
        return regIp;
    }

    public void setRegIp(long regIp) {
        this.regIp = regIp;
    }

    public long getLastIp() {
        return lastIp;
    }

    public void setLastIp(long lastIp) {
        this.lastIp = lastIp;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }
}
