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

import com.ruijc.piz7.zip.ZipCreateCallback;
import com.ruijc.util.CollectionUtils;
import com.ruijc.util.FileUtils;
import com.ruijc.util.IFileWalk;
import net.sf.sevenzipjbinding.IOutCreateArchiveZip;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

/**
 * 压缩工具类
 *
 * @author Storezhang
 * @create 2017-09-25 20:53
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public class ZipUtils {

    public static void zip(String filename, String... paths) throws Exception {
        zip(paths, filename);
    }

    public static void zip(final String[] path, String filename) throws Exception {
        zip(path, filename, 5);
    }

    public static void zip(final String[] paths, String filename, int lvl) throws Exception {
        if (null == paths || 0 == paths.length) {
            return;
        }

        final List<IZipFile> files = new LinkedList<IZipFile>();
        for (final String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                final File parentFile = new File(path);
                FileUtils.walk(path, new IFileWalk() {
                    @Override
                    public void onWalk(File file) {
                        files.add(new DefaultZipFile(parentFile, file));
                    }
                });
            } else {
                files.add(new DefaultZipFile(file.getParentFile(), file));
            }
        }


        RandomAccessFile raf = null;
        IOutCreateArchiveZip outArchive = null;
        try {
            raf = new RandomAccessFile(filename, "rw");
            outArchive = SevenZip.openOutArchiveZip();
            outArchive.setLevel(lvl);
            outArchive.createArchive(new RandomAccessFileOutStream(raf), files.size(), new ZipCreateCallback(files));
        } finally {
            if (null != outArchive) {
                outArchive.close();
            }
            if (null != raf) {
                raf.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String destZipFile = args[0];
        String[] files = new String[args.length - 1];
        System.arraycopy(args, 1, files, 0, args.length - 1);
        zip(destZipFile, files);
    }
}
