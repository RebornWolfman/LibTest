package httploglib.lib.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wanjian.sak.LayoutManager;

import java.util.List;

import httploglib.lib.been.HttpBeen;
import httploglib.lib.been.IpConfigBeen;
import httploglib.lib.crash.CrashHandler;
import httploglib.lib.service.WindowService;
import lib.DemoHoverMenuService;

/**
 * @author liuml
 * @explain 测试库工具 关于工具库所有的操作都通过这个类
 * @time 2016/12/7 10:04
 */

public class TestLibUtil {

    private Application context;
    IpLibConfig libConfig ;

    //使用静态单例模式
    private static class InnerInstance {
        public static TestLibUtil instance = new TestLibUtil();
    }

    public static TestLibUtil getInstance() {
        return InnerInstance.instance;
    }


    public void startUtil(Context context){
        DemoHoverMenuService.showFloatingMenu(context);
    }
    public void initWindows(Application context) {
        this.context = context;
        if (!isServiceWork(context, WindowService.class.getName())) {
            Intent intent = new Intent(context, WindowService.class);
            context.startService(intent);
            //崩溃工具初始化
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(context);
            //UI
            LayoutManager.init(context);

        }

    }


    /**
     * 发送网路哦请求
     * @param header
     * @param url
     * @param json
     */
    public void sendmessage(String header, String url, String json) {
        Intent intent1 = new Intent();
//        不能发送大量数据
        //BroadcastUtil.send(context, intent1, BroadcastUtil.windows);
        //直接操作静态变量
        Logger.d("打印数据 url=     \n" + url);
        HttpBeen been = new HttpBeen(url, json, header);
        //最大条数  0条避免数量过多溢出
        if (WindowService.httpMoudleList != null) {

            if (WindowService.httpMoudleList.size() > 30) {
                WindowService.httpMoudleList.remove(30);
                WindowService.httpMoudleList.add(been);
            } else {
                WindowService.httpMoudleList.add(been);
            }
        }
    }


    /**
     * 这里逻辑判断  除非sp内 没有IP地址的数据才会去更新
     *
     * @param context
     * @param list
     */
    public void setSwitchs(Context context, List<IpConfigBeen> list) {
        libConfig = IpLibConfig.getInstance(context);
        //存入之前先判断是否已经有了
        String str = getSwitchs((context));
        if (TextUtils.isEmpty(str)) {
            //存入
            libConfig.initIpConfig(list);
        }
    }




    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**********************************************ip 操作******************************************************/

    /**
     * 获取当前ip
     *
     * @param context
     * @return
     */
    public String getSwitchs(Context context) {
        String url = null;
        //切换服务器ip 地址 测试
        ListDataSave instance = ListDataSave.getInstance(context, ListDataSave.ListDataSave);
        List<IpConfigBeen> list = instance.getDataList(ListDataSave.listTag);
        for (IpConfigBeen ipConfigBeen : list) {
            if (ipConfigBeen.isSelect()) {
                url = ipConfigBeen.getUrl();
            }
        }
        return url;
    }
    /**
     * 添加ip
     *
     * @param ipConfigBeen
     */
    public void AddIp(IpConfigBeen ipConfigBeen) {

        IpLibConfig.getInstance(context).AddIp(ipConfigBeen);
    }

    /**
     * 删除ip
     *
     * @param position
     */
    public void DelIp(int position) {
        IpLibConfig.getInstance(context).DelIp(position);
    }

    /**
     * 删除全部ip
     */
    public void DelIpAll() {
        IpLibConfig.getInstance(context).DelIpAll();
    }

    /**
     * 设置ip 的数据
     *
     * @param dataList
     */
    public void setDataList(List<IpConfigBeen> dataList) {
        IpLibConfig.getInstance(context).setListSelect(dataList);
    }

    /**
     * 获取所有ip地址
     * @return
     */
    public List<IpConfigBeen> getIpList(){
        List<IpConfigBeen> ipList = IpLibConfig.getInstance(context).getIpList();
        return  ipList;
    }


}
