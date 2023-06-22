package top.liaoxin.StarDB.backend.TransactionManager;

import top.liaoxin.StarDB.backend.common.Error;
import top.liaoxin.StarDB.backend.utils.Panic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
* 维护xid文件，记录各个事务的状态
* */
public interface TransactionManager {
    long begin();// 开启事务
    void commit(long xid);// 提交事务
    void abort(long xid);// 回滚事务
    boolean isActive(long xid);// 查询事务的状态是否正在进行
    boolean isCommitted(long xid);// 查询事务是否提交
    boolean isAbort(long xid);// 查询事务是否回滚
    void close();// 关闭事务


    public static TransactionManagerImpl create(String path) {
        File f = new File(path + TransactionManagerImpl.XID_SUFFIX);
        try {
            if(!f.createNewFile()) {
                Panic.panic(Error.FileExistsException);
            }
        }catch (Exception e) {
            Panic.panic(e);
        }
        if(!f.canRead() || !f.canWrite()) {
            Panic.panic(Error.FileCannotRWException);
        }
        FileChannel fc = null;
        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        }catch (FileNotFoundException e) {
            Panic.panic(e);
        }
        //  写 空XID文件头
        //从零创建 XID 文件时需要写一个空的 XID 文件头，即设置 xidCounter 为 0，否则后续在校验时会不合法
        ByteBuffer buf = ByteBuffer.wrap(new byte[TransactionManagerImpl.LEN_XID_HEADER_LENGTH]);
        try {
            fc.position(0);
            fc.write(buf);
        }catch (IOException e) {
            Panic.panic(e);
        }
        return new TransactionManagerImpl(raf, fc);
    }

    public static TransactionManagerImpl open(String path) {
        File f = new File(path + TransactionManagerImpl.XID_SUFFIX);
        if(!f.exists()) {
            Panic.panic(Error.FileNotExistsException);
        }
        if(!f.canWrite() || !f.canRead()) {
            Panic.panic(Error.FileCannotRWException);
        }
        FileChannel fc = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f,"rw");
            fc = raf.getChannel();
        }catch (FileNotFoundException e) {
            Panic.panic(e);
        }
        return new TransactionManagerImpl(raf,fc);
    }
}
