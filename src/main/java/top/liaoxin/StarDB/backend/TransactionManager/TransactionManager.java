package top.liaoxin.StarDB.backend.TransactionManager;

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
}
