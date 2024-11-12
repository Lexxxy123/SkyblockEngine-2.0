/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.gridfs;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.Util;
import java.io.File;
import java.security.DigestInputStream;
import java.security.MessageDigest;

@Deprecated
public class CLI {
    private static String host = "127.0.0.1";
    private static String db = "test";
    private static Mongo mongo = null;
    private static GridFS gridFS;

    private static void printUsage() {
        System.out.println("Usage : [--bucket bucketname] action");
        System.out.println("  where  action is one of:");
        System.out.println("      list                      : lists all files in the store");
        System.out.println("      put filename              : puts the file filename into the store");
        System.out.println("      get filename1 filename2   : gets filename1 from store and sends to filename2");
        System.out.println("      md5 filename              : does an md5 hash on a file in the db (for testing)");
    }

    private static Mongo getMongo() throws Exception {
        if (mongo == null) {
            mongo = new MongoClient(host);
        }
        return mongo;
    }

    private static GridFS getGridFS() throws Exception {
        if (gridFS == null) {
            gridFS = new GridFS(CLI.getMongo().getDB(db));
        }
        return gridFS;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            CLI.printUsage();
            return;
        }
        for (int i2 = 0; i2 < args.length; ++i2) {
            String s2 = args[i2];
            if (s2.equals("--db")) {
                db = args[i2 + 1];
                ++i2;
                continue;
            }
            if (s2.equals("--host")) {
                host = args[i2 + 1];
                ++i2;
                continue;
            }
            if (s2.equals("help")) {
                CLI.printUsage();
                return;
            }
            if (s2.equals("list")) {
                GridFS fs = CLI.getGridFS();
                System.out.printf("%-60s %-10s%n", "Filename", "Length");
                DBCursor fileListCursor = fs.getFileList();
                try {
                    while (fileListCursor.hasNext()) {
                        DBObject o2 = fileListCursor.next();
                        System.out.printf("%-60s %-10d%n", o2.get("filename"), ((Number)o2.get("length")).longValue());
                    }
                } finally {
                    fileListCursor.close();
                }
                return;
            }
            if (s2.equals("get")) {
                String fn;
                GridFS fs = CLI.getGridFS();
                GridFSDBFile f2 = fs.findOne(fn = args[i2 + 1]);
                if (f2 == null) {
                    System.err.println("can't find file: " + fn);
                    return;
                }
                f2.writeTo(f2.getFilename());
                return;
            }
            if (s2.equals("put")) {
                GridFS fs = CLI.getGridFS();
                String fn = args[i2 + 1];
                GridFSInputFile f3 = fs.createFile(new File(fn));
                f3.save();
                f3.validate();
                return;
            }
            if (s2.equals("md5")) {
                String fn;
                GridFS fs = CLI.getGridFS();
                GridFSDBFile f4 = fs.findOne(fn = args[i2 + 1]);
                if (f4 == null) {
                    System.err.println("can't find file: " + fn);
                    return;
                }
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                int read = 0;
                DigestInputStream is = new DigestInputStream(f4.getInputStream(), md5);
                try {
                    while (is.read() >= 0) {
                        ++read;
                        int r2 = is.read(new byte[17]);
                        if (r2 < 0) {
                            break;
                        }
                        read += r2;
                    }
                } finally {
                    is.close();
                }
                byte[] digest = md5.digest();
                System.out.println("length: " + read + " md5: " + Util.toHex(digest));
                return;
            }
            System.err.println("unknown option: " + s2);
            return;
        }
    }
}

