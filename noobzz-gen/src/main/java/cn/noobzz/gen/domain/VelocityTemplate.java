package cn.noobzz.gen.domain;

/**
 * @author: ZZJ
 * @date: 2022/09/07
 * @desc:
 */
public class VelocityTemplate {

    private String name;

    private String content;

    private String path;

    private String dir;

    @Override
    public String toString() {
        return "VelocityTemplate{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", path='" + path + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
