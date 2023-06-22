package top.liaoxin.StarDB.backend.common;

public class Error {
    public static final Exception BadXidFileException = new RuntimeException("the xid file is bad");
    public static final Exception FileExistsException = new RuntimeException("the file is exists");
    public static final Exception FileNotExistsException = new RuntimeException("the file is not exists");
    public static final Exception FileCannotRWException = new RuntimeException("cannot read or write the file");
}
