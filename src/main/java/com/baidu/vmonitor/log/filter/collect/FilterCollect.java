package com.baidu.vmonitor.log.filter.collect;

import com.baidu.vmonitor.collect.CollectInterface;
import com.baidu.vmonitor.common.config.CustomCollectorConfig;
import com.baidu.vmonitor.common.config.CustomCollectorConfigKey;
import com.baidu.vmonitor.common.config.VmonitorCollectorConfig;
import com.baidu.vmonitor.common.config.VmonitorCollectorConfigKey;
import com.baidu.vmonitor.log.filter.ConfigLogFilter;
import com.baidu.vmonitor.logger.DefaultVMonitorLogger;
import com.baidu.vmonitor.model.LogInfo;
import com.baidu.vmonitor.utils.GsonUtils;

public class FilterCollect implements CollectInterface<LogInfo> {

    public static final String LOGLEVEL = CustomCollectorConfig.getInstance().getString(CustomCollectorConfigKey.LOG_FILTER_LEVEL);
    public static final Class<?> LOGCATEGORY = ConfigLogFilter.class;
    public static final String FORMAT = VmonitorCollectorConfig.getInstance().getString(VmonitorCollectorConfigKey.LOG_FILTER_FORMAT);

    private static FilterCollect instance = null;

    private DefaultVMonitorLogger vMonitorLogger;

    private FilterCollect(DefaultVMonitorLogger vMonitorLogger) {
        this.vMonitorLogger = vMonitorLogger;
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new FilterCollect(new DefaultVMonitorLogger(LOGCATEGORY, LOGLEVEL));
        }
    }

    public static FilterCollect getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }
    
    /**
     * @Title: readResolve
     * @Description: TODO(如果该对象被用于序列化，可以保证对象在序列化前后保持一致)
     * @returnType: Object
     * @return
     */
    public Object readResolve() {
        return getInstance();
    }

    public void doCollect(LogInfo... info) {
        try {
            vMonitorLogger.doLog(FORMAT, GsonUtils.getInstance().toJson(info));
        } catch (Exception e) {
            vMonitorLogger.doLog(FORMAT, info.toString());
        }
    }

}
