/*-----------------------------------------------------------

-- PURPOSE

--    缓存非持久存留的信息。用于便捷地在不同Activity间传递数据。多用于具有前后流程联系的Activity传递数据

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import java.util.Calendar;
import java.util.HashMap;

import android.util.Log;
import cn.kol.pes.model.NetworkManager;
import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.item.KoWeekItem;
import cn.kol.pes.model.parser.adapter.MeLoginAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetOrgInfoAdapter;


public class CacheUtils {
	
	private static Calendar mSelectedDate;
	
    
    private static MeLoginAdapter mKoLoginAdapter;
    private static KoJobItem mSelectedJob;
    private static KoOpItem mSelectedOp;
    private static KoAssetCheckItem mAssetCheckItem;
    private static HashMap<String, KoWeekItem> mWeekMap;



    private static MmGetOrgInfoAdapter mmGetOrgInfoAdapter;
    
    private static String mShift = "DAY";

    private static String selData;
    
    //清空登陆信息等
    public static void clearAllCache() {
    	mSelectedDate = null;
    	mKoLoginAdapter = null;
    	mSelectedJob = null;
    	mSelectedOp = null;
    	mAssetCheckItem = null;
        selData = null;
    	NetworkManager.clearNetInstance();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    public static void setSelectedDate(Calendar cal) {
    	mSelectedDate = cal;
    }
    
    public static Calendar getSelectedDate() {
    	return mSelectedDate;
    }
    
    
    
    
    public static HashMap<String, KoWeekItem> getWeekMap() {
    	return mWeekMap;
    }
    
    public static void setWeekMap(HashMap<String, KoWeekItem> week) {
    	mWeekMap = week;
    }
    
    //点击点检设备列表时，保存那个item的数据，供打开的维修界面使用
    public static void setSelectedAssetCheckItem(KoAssetCheckItem item) {
    	mAssetCheckItem = item;
    }
    
    //保存选中的点检列表项数据s
    public static KoAssetCheckItem getSelectedAssetCheckItem() {
    	return mAssetCheckItem;
    }
    
    //保存登陆信息
    public static void setLoginUserInfo(MeLoginAdapter user) {
    	mKoLoginAdapter = user;
    }
    
    public static MeLoginAdapter getLoginUserInfo() {
    	return mKoLoginAdapter;
    }
    
    //点击开始工序后，保存工单信息
    public static void setSelectedJob(KoJobItem dan) {
    	mSelectedJob = dan;
    }
    
    public static KoJobItem getSelectedJob() {
    	return mSelectedJob;
    }
    
    //点击开始工序后，保存工序数据
    public static void setSelectedOp(KoOpItem op) {
    	mSelectedOp = op;
    }
    
    public static KoOpItem getSelectedOp() {
    	return mSelectedOp;
    }
    
    public static String getShift() {
    	return mShift;
    }
    public static MmGetOrgInfoAdapter getMmGetOrgInfoAdapter() {
        return mmGetOrgInfoAdapter;
    }

    public static void setMmGetOrgInfoAdapter(MmGetOrgInfoAdapter mmGetOrgInfoAdapter) {
        CacheUtils.mmGetOrgInfoAdapter = mmGetOrgInfoAdapter;
    }
    public static void setShift(String shift) {
    	mShift = shift;
    	Log.e("CacheUtils", "setShift():shift="+shift);
    }

    public static String getSelData() {
        return selData;
    }

    public static void setSelData(String selData) {
        CacheUtils.selData = selData;
    }
}
