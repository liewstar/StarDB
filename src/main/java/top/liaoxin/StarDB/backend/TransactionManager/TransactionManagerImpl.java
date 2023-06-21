package top.liaoxin.StarDB.backend.TransactionManager;

import top.liaoxin.StarDB.backend.common.Error;
import top.liaoxin.StarDB.backend.utils.Panic;
import top.liaoxin.StarDB.backend.utils.Parser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionManagerImpl implements TransactionManager{

    static final int LEN_XID_HEADER_LENGTH = 8;// xid 文件头长度
    static final int XID_FIELD_LENGTH = 1;// 事务占用长度

    //事务的三种状态
    private static final byte FIELD_TRANS_ACTIVE  = 0;// 正在进行
    private static final byte FIELD_TRANS_COMMITTED  = 1;// 已经提交
    private static final byte FIELD_TRANS_ABORTED  = 2;// 回滚

    public static final byte SUPPER_XID = 0;// 超级事务，永远为committed状态

    static final String XID_SUFFIX = ".xid";

    private RandomAccessFile file;
    private FileChannel fc;
    private long xidCounter;
    private Lock counterLock;

    TransactionManagerImpl(RandomAccessFile raf, FileChannel fc) {
        this.file = raf;
        this.fc = fc;
        counterLock = new ReentrantLock();
        checkXIDCounter();
    }

    /*
    * 检查XID文件是否合法
    * 读取xidHeader中的
    * */
    private void checkXIDCounter() {
        long fileLen = 0;
        try {
            fileLen = file.length();
        }catch (IOException e1) {
            Panic.panic(Error.BadXidFileException);
        }
        if(fileLen < LEN_XID_HEADER_LENGTH) {
            Panic.panic(Error.BadXidFileException);
        }

        ByteBuffer bf = ByteBuffer.allocate(LEN_XID_HEADER_LENGTH);
        try {
            fc.position(0);
            fc.read(bf);
        } catch (IOException e) {
            Panic.panic(e);
        }
        this.xidCounter = Parser.parseLong(bf.array());
        // 返回下一个xid文件的起始位置
        long end = getXidPosition(this.xidCounter + 1);
        // 文件的长度应该等于所有已记录xid所占用的空间总和
        if(end != fileLen) {
            Panic.panic(Error.BadXidFileException);
        }

    }

    private long getXidPosition(long xid) {
        // xid状态储存的位置
        return LEN_XID_HEADER_LENGTH + (xid-1) * XID_FIELD_LENGTH;
    }
    public long begin() {
        return 0;
    }

    public void commit(long xid) {

    }

    public void abort(long xid) {

    }

    public boolean isActive(long xid) {
        return false;
    }

    public boolean isCommitted(long xid) {
        return false;
    }

    public boolean isAbort(long xid) {
        return false;
    }

    public void close() {

    }
}