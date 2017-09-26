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

import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.OutItemFactory;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * 基础压缩类
 *
 * @author Storezhang
 * @create 2017-09-25 21:17
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public abstract class AbstractCreateCallback<T extends IOutItemBase> implements IOutCreateCallback<T> {

    protected List<IZipFile> files;

    public AbstractCreateCallback(List<IZipFile> files) {
        this.files = files;
    }

    @Override
    public void setOperationResult(boolean operationResultOk) throws SevenZipException {
    }

    @Override
    public void setTotal(long total) throws SevenZipException {
    }

    @Override
    public void setCompleted(long complete) throws SevenZipException {
    }

    @Override
    public ISequentialInStream getStream(int index) throws SevenZipException {
        ISequentialInStream stream = null;

        if (files.get(index).isDir()) {
            return stream;
        }

        try {
            stream = new RandomAccessFileInStream(new RandomAccessFile(files.get(index).getFilePath(), "r"));
        } catch (FileNotFoundException e) {
            throw new SevenZipException(e);
        }

        return stream;
    }
}
