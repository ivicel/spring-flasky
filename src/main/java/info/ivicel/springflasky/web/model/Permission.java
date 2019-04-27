package info.ivicel.springflasky.web.model;

public enum Permission {
    /**
     * 关注权限
     */
    FOLLOW(0x01, "FOLLOW"),

    /**
     * 评论权限
     */
    COMMENT(0x02, "COMMENT"),

    /**
     * 写 post 权限
     */
    WRITE(0x04, "WRITE"),

    /**
     * 修改他人信息的权限,
     */
    MODERATE(0x08, "MODERATE"),

    /**
     * 管理员权限
     */
    ADMIN(0xf0, "ADMIN");

    private int value;
    private String desc;

    Permission(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public boolean hasPermission(Permission permission) {
        return (this.value & permission.getValue()) == permission.getValue();
    }
}
