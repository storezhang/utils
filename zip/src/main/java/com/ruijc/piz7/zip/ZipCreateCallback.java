package com.ruijc.piz7.zip;

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

import com.ruijc.piz7.AbstractCreateCallback;
import com.ruijc.piz7.IZipFile;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.OutItemFactory;

import java.util.List;

/**
 * ZIP压缩回调
 *
 * @author Storezhang
 * @create 2017-09-25 21:09
 * @email storezhang@gmail.com
 * @qq 160290688
 */
public class ZipCreateCallback extends AbstractCreateCallback<IOutItemZip> {

    public ZipCreateCallback(List<IZipFile> files) {
        super(files);
    }

    @Override
    public IOutItemZip getItemInformation(int index, OutItemFactory<IOutItemZip> outItemFactory) throws SevenZipException {
        int attr = PropID.AttributesBitMask.FILE_ATTRIBUTE_UNIX_EXTENSION;

        IOutItemZip item = outItemFactory.createOutItem();

        if (files.get(index).isDir()) {
            item.setPropertyIsDir(true);
            attr |= PropID.AttributesBitMask.FILE_ATTRIBUTE_DIRECTORY;
            attr |= 0x81ED << 16; // permissions: drwxr-xr-x
        } else {
            item.setDataSize(files.get(index).getSize());
            attr |= 0x81A4 << 16; // permissions: -rw-r--r--
        }
        item.setPropertyPath(files.get(index).getPath());

        item.setPropertyAttributes(attr);

        return item;
    }
}
