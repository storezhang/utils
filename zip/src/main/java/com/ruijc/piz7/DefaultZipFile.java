package com.ruijc.piz7;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

import com.ruijc.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 要被压缩的文件
 *
 * @author Storezhang
 * @create 2017-09-25 21:15
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public class DefaultZipFile implements IZipFile {

    private File file;
    private File root;
    private String parent;

    public DefaultZipFile(File root, File file) {
        this(root, file, "");
    }

    public DefaultZipFile(File root, File file, String parent) {
        this.root = root;
        this.file = file;
        this.parent = parent;
    }

    @Override
    public String getPath() {
        String path = file.getAbsolutePath().replace(root.getAbsolutePath(), "");

        if (!StringUtils.isBlank(path)) {
            path = parent + File.separator + path;
        }

        return path;
    }

    @Override
    public String getFilePath() {
        return file.getPath();
    }

    @Override
    public long getSize() {
        return file.getTotalSpace();
    }

    @Override
    public boolean isDir() {
        return file.isDirectory();
    }
}
