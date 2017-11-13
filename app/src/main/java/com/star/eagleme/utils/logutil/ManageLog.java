package com.star.eagleme.utils.logutil;

import android.content.Context;
import android.net.ParseException;
import android.os.Environment;
import android.util.Log;

import com.star.eagleme.BaseApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

public class ManageLog
{

    // Log关闭变量
    public static boolean bOpenLog = true;
    private static boolean bOpenSaveLogToFile = false;

    // Log等级,默认为VERBOSE，即全部输出
    private static int logLevel = Log.DEBUG;

    private static final int FILENUM = 10;

    private static final String DEFAULT_TAG = "Eagle Live";
    private static String mPackageName = DEFAULT_TAG;
    public static final String DateFormat = "yyyy.MM.dd.HH-mm-ss";
    public static final String LogDir = Environment
            .getExternalStorageDirectory().getPath() + "/Log/";

    // 存储文件名队列
    private static LinkedBlockingQueue<String> fileQueue = new LinkedBlockingQueue<String>(
            FILENUM);

    public static String strFileName = null;
    private static Thread th = null;

    static
    {
        if (mPackageName.equals(DEFAULT_TAG))
        {
            Context context = BaseApplication.getAppContext();
            if (null != context)
            {
                mPackageName = context.getPackageName();
            }
        }
    }
    
    public static boolean switchLog(boolean openLog, int level)
    {

        bOpenLog = openLog;
        bOpenSaveLogToFile = openLog;
        logLevel = level;

        if (!openLog)
        {
            th = null;
        }

        return true;
    }

    public static void E(String tag, String msg)
    {
        LOG_E(tag, msg);
    }

    public static void I(String tag, String msg)
    {
        LOG_I(tag, msg);
    }

    public static void D(String tag, String msg)
    {
        LOG_D(tag, msg);
    }

    public static void W(String tag, String msg)
    {
        LOG_W(tag, msg);
    }

    private static int LOG_D(String tag, String msg)
    {
        if (bOpenLog && logLevel <= Log.DEBUG)
        {
            // saveLogToFile(tag + ":" + msg);
            startLogThread();

            return Log.d(tag, msg);
        } else
        {
            return 0;
        }
    }

    private static int LOG_I(String tag, String msg)
    {
        if (bOpenLog && logLevel <= Log.INFO)
        {
            // saveLogToFile(tag + ":" + msg);
            startLogThread();

            return Log.i(tag, msg);
        } else
        {
            return 0;
        }
    }

    private static int LOG_W(String tag, String msg)
    {
        if (bOpenLog && logLevel <= Log.WARN)
        {
            // saveLogToFile(tag + ":" + msg);
            startLogThread();

            return Log.w(tag, msg);
        } else
        {
            return 0;
        }
    }

    private static int LOG_E(String tag, String msg)
    {
        if (bOpenLog && logLevel <= Log.ERROR)
        {
            // saveLogToFile(tag + ":" + msg);
            startLogThread();

            return Log.e(tag, msg);
        } else
        {
            return 0;
        }
    }

    private static void startLogThread()
    {
        if (null != th)
        {
            return;
        }
        th = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                initFileQueue();

                while (true)
                {
                    // 永真循环，一直去抓log。
                    System.out.println("--------func start--------");
                    try
                    {
                        // 定义抓log的参数，-v就是抓所有的log，-e就是抓error的log。
                        ArrayList<String> cmdLine = new ArrayList<String>();
                        cmdLine.add("logcat");
                        cmdLine.add("-v");
                        cmdLine.add("time");

                        BufferedReader bufferedReader;
                        // Runtime用于抓log的类
                        Process process = Runtime.getRuntime().exec(
                                "logcat -v time");

                        bufferedReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));

                        String str = null;
                        while ((str = bufferedReader.readLine()) != null)
                        {
                            // 将log写进文件。
                            if (!bOpenSaveLogToFile)
                            {
                                if (null != process)
                                {
                                    process.destroy();
                                    process = null;
                                }
                                th = null;

                                return;
                            }
                            writeToFile(str);
                        }
                        if (str == null)
                        {
                            bufferedReader = new BufferedReader(
                                    new InputStreamReader(process
                                            .getInputStream()));
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("--------func end--------");
                }
            }

        });
        // 开启线程
        th.start();
    }

    private static void saveLogToFile()
    {
        if (bOpenSaveLogToFile)
        {
            startLogThread();
        }
    }

    public static void writeToFile(String content)
    {
        BufferedWriter out = null;
        try
        {
            File dir = new File("mnt/sdcard/Log/" + mPackageName);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            if (null == strFileName
                    || (getFileSizes("mnt/sdcard/Log/" + mPackageName + "/"
                    + strFileName) >= 1024 * 10240))
            {
                strFileName = getLogFileName();
                System.out.println("strFileName=" + strFileName);
                isFileRollback(strFileName);
            }

            File file = new File("mnt/sdcard/Log/" + mPackageName + "/"
                    + strFileName);

            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write("\n");
            out.write(content);
        } catch (Exception e)
        {
            // e.printStackTrace();
        } catch (OutOfMemoryError e)
        {

        } finally
        {
            try
            {
                if (null != out)
                {
                    out.close();
                    out = null;
                }
            } catch (IOException e)
            {
                // e.printStackTrace();
            }
        }
    }

    private static void isFileRollback(String fileName)
            throws InterruptedException
    {
        if (fileQueue.size() < FILENUM)
        {
            fileQueue.put(fileName);
        } else
        {
            String queueString = fileQueue.poll();
            File delFile = new File("mnt/sdcard/Log/" + mPackageName + "/"
                    + queueString);
            delFile.delete();
            fileQueue.put(fileName);
        }
    }

    private static String getLogFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss",
                Locale.SIMPLIFIED_CHINESE);
        String datetime = sdf.format(date);
        String name = "N8." + datetime + ".txt";
        return name;

    }

    /**
     * 取得文件大小
     *
     * @param path
     * @return
     */
    public static long getFileSizes(String path)
    {
        long s = 0;
        try
        {
            File f = new File(path);
            if (f.exists())
            {
                FileInputStream fis = null;
                fis = new FileInputStream(f);
                s = fis.available();
                fis.close();
            }
        } catch (Exception e)
        {
        }

        return s;
    }

    public static void initFileQueue()
    {
        File logdir = new File(LogDir + mPackageName);
        if (!logdir.exists())
        {
            System.out.println("initFileQueue() --!logdir.exists()");
            return;
        }
        File logfile[] = logdir.listFiles();
        if(logfile == null) return;
        int lens = logfile.length;
        if (lens > 0)
        {
            Arrays.sort(logfile, new ComparatorByDate_());
            if (lens > FILENUM)
            {
                int delFileCount = lens - FILENUM;
                for (int i = 0; i < delFileCount; i++)
                {// 删掉最新的FILENUM个日志文件之前的日志文件
                    File deleteFile = new File(logfile[i].getAbsolutePath()
                            .toString());
                    deleteFile.delete();
                }
                for (int j = delFileCount; j < lens; j++)
                {// 将最新的FILENUM个日志文件的名字放入fileQueue
                    try
                    {
                        fileQueue.put(logfile[j].getName().toString());
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            } else
            {
                for (int j = 0; j < lens; j++)
                {// 若所有日志文件<FILENUM，则将所有日志文的名字均放入fileQueue
                    try
                    {
                        fileQueue.put(logfile[j].getName().toString());
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}

class ComparatorByDate_ implements Comparator<File>
{

    public int compare(File lhs, File rhs)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(ManageLog.DateFormat);
        try
        {
            java.util.Date sDate = null;
            try
            {
                sDate = sdf.parse(getFileLastModifiedTime(lhs.getAbsolutePath()
                        .toString()));
            } catch (java.text.ParseException e)
            {
                e.printStackTrace();
            }
            java.util.Date rDate = null;
            try
            {
                rDate = sdf.parse(getFileLastModifiedTime(rhs.getAbsolutePath()
                        .toString()));
            } catch (java.text.ParseException e)
            {
                e.printStackTrace();
            }

            long diff = sDate.getTime() - rDate.getTime();
            if (diff > 0)
            {
                return 1;
            } else if (diff < 0)
            {
                return -1;
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getFileLastModifiedTime(String filePath)
    {
        String path = filePath.toString();
        File f = new File(path);
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat(
                ManageLog.DateFormat);
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }
};
