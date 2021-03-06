package com.android.server.notification;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.provider.Settings.Secure;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.ZenModeConfig;
import android.service.notification.ZenModeConfig.ScheduleInfo;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.notification.NotificationManagerService.DumpFilter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.TimeZone;

/*  JADX ERROR: NullPointerException in pass: ReSugarCode
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ReSugarCode.initClsEnumMap(ReSugarCode.java:159)
    	at jadx.core.dex.visitors.ReSugarCode.visit(ReSugarCode.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ExtractFieldInit.checkStaticFieldsInit(ExtractFieldInit.java:58)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
public class ScheduleConditionProvider extends SystemConditionProviderService {
    private static final String ACTION_EVALUATE = null;
    public static final ComponentName COMPONENT = null;
    static final boolean DEBUG = false;
    private static final String EXTRA_TIME = "time";
    private static final String NOT_SHOWN = "...";
    private static final int REQUEST_CODE_EVALUATE = 1;
    private static final String SCP_SETTING = "snoozed_schedule_condition_provider";
    private static final String SEPARATOR = ";";
    private static final String SIMPLE_NAME = null;
    static final String TAG = "ConditionProviders.SCP";
    private AlarmManager mAlarmManager;
    private boolean mConnected;
    private final Context mContext;
    private long mNextAlarmTime;
    private BroadcastReceiver mReceiver;
    private boolean mRegistered;
    private ArraySet<Uri> mSnoozed;
    private final ArrayMap<Uri, ScheduleCalendar> mSubscriptions;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e9 in method: com.android.server.notification.ScheduleConditionProvider.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    static {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 00e9 in method: com.android.server.notification.ScheduleConditionProvider.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.ScheduleConditionProvider.<clinit>():void");
    }

    public ScheduleConditionProvider() {
        this.mContext = this;
        this.mSubscriptions = new ArrayMap();
        this.mSnoozed = new ArraySet();
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (ScheduleConditionProvider.DEBUG) {
                    Slog.d(ScheduleConditionProvider.TAG, "onReceive " + intent.getAction());
                }
                if ("android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction())) {
                    for (Uri conditionId : ScheduleConditionProvider.this.mSubscriptions.keySet()) {
                        ScheduleCalendar cal = (ScheduleCalendar) ScheduleConditionProvider.this.mSubscriptions.get(conditionId);
                        if (cal != null) {
                            cal.setTimeZone(Calendar.getInstance().getTimeZone());
                        }
                    }
                }
                ScheduleConditionProvider.this.evaluateSubscriptions();
            }
        };
        if (DEBUG) {
            Slog.d(TAG, "new " + SIMPLE_NAME + "()");
        }
    }

    public ComponentName getComponent() {
        return COMPONENT;
    }

    public boolean isValidConditionId(Uri id) {
        return ZenModeConfig.isValidScheduleConditionId(id);
    }

    public void dump(PrintWriter pw, DumpFilter filter) {
        pw.print("    ");
        pw.print(SIMPLE_NAME);
        pw.println(":");
        pw.print("      mConnected=");
        pw.println(this.mConnected);
        pw.print("      mRegistered=");
        pw.println(this.mRegistered);
        pw.println("      mSubscriptions=");
        long now = System.currentTimeMillis();
        for (Uri conditionId : this.mSubscriptions.keySet()) {
            pw.print("        ");
            pw.print(meetsSchedule((ScheduleCalendar) this.mSubscriptions.get(conditionId), now) ? "* " : "  ");
            pw.println(conditionId);
            pw.print("            ");
            pw.println(((ScheduleCalendar) this.mSubscriptions.get(conditionId)).toString());
        }
        pw.println("      snoozed due to alarm: " + TextUtils.join(SEPARATOR, this.mSnoozed));
        SystemConditionProviderService.dumpUpcomingTime(pw, "mNextAlarmTime", this.mNextAlarmTime, now);
    }

    public void onConnected() {
        if (DEBUG) {
            Slog.d(TAG, "onConnected");
        }
        this.mConnected = true;
        readSnoozed();
    }

    public void onBootComplete() {
    }

    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            Slog.d(TAG, "onDestroy");
        }
        this.mConnected = false;
    }

    public void onSubscribe(Uri conditionId) {
        if (DEBUG) {
            Slog.d(TAG, "onSubscribe " + conditionId);
        }
        if (ZenModeConfig.isValidScheduleConditionId(conditionId)) {
            this.mSubscriptions.put(conditionId, toScheduleCalendar(conditionId));
            evaluateSubscriptions();
            return;
        }
        notifyCondition(conditionId, 0, "badCondition");
    }

    public void onUnsubscribe(Uri conditionId) {
        if (DEBUG) {
            Slog.d(TAG, "onUnsubscribe " + conditionId);
        }
        this.mSubscriptions.remove(conditionId);
        removeSnoozed(conditionId);
        evaluateSubscriptions();
    }

    public void attachBase(Context base) {
        attachBaseContext(base);
    }

    public IConditionProvider asInterface() {
        return (IConditionProvider) onBind(null);
    }

    private void evaluateSubscriptions() {
        if (this.mAlarmManager == null) {
            this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        }
        setRegistered(!this.mSubscriptions.isEmpty());
        long now = System.currentTimeMillis();
        this.mNextAlarmTime = 0;
        long nextUserAlarmTime = getNextAlarm();
        for (Uri conditionId : this.mSubscriptions.keySet()) {
            ScheduleCalendar cal = (ScheduleCalendar) this.mSubscriptions.get(conditionId);
            if (cal == null || !cal.isInSchedule(now)) {
                notifyCondition(conditionId, 0, "!meetsSchedule");
                removeSnoozed(conditionId);
                if (nextUserAlarmTime == 0 && cal != null) {
                    cal.maybeSetNextAlarm(now, nextUserAlarmTime);
                }
            } else {
                if (conditionSnoozed(conditionId) || cal.shouldExitForAlarm(now)) {
                    notifyCondition(conditionId, 0, "alarmCanceled");
                    addSnoozed(conditionId);
                } else {
                    notifyCondition(conditionId, 1, "meetsSchedule");
                }
                cal.maybeSetNextAlarm(now, nextUserAlarmTime);
            }
            if (cal != null) {
                long nextChangeTime = cal.getNextChangeTime(now);
                if (nextChangeTime > 0 && nextChangeTime > now) {
                    if (this.mNextAlarmTime == 0 || nextChangeTime < this.mNextAlarmTime) {
                        this.mNextAlarmTime = nextChangeTime;
                    }
                }
            }
        }
        updateAlarm(now, this.mNextAlarmTime);
    }

    private void updateAlarm(long now, long time) {
        AlarmManager alarms = (AlarmManager) this.mContext.getSystemService("alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mContext, 1, new Intent(ACTION_EVALUATE).addFlags(268435456).putExtra(EXTRA_TIME, time), 134217728);
        alarms.cancel(pendingIntent);
        if (time > now) {
            if (DEBUG) {
                String str = TAG;
                Object[] objArr = new Object[3];
                objArr[0] = SystemConditionProviderService.ts(time);
                objArr[1] = SystemConditionProviderService.formatDuration(time - now);
                objArr[2] = SystemConditionProviderService.ts(now);
                Slog.d(str, String.format("Scheduling evaluate for %s, in %s, now=%s", objArr));
            }
            alarms.setExact(0, time, pendingIntent);
        } else if (DEBUG) {
            Slog.d(TAG, "Not scheduling evaluate");
        }
    }

    public long getNextAlarm() {
        AlarmClockInfo info = this.mAlarmManager.getNextAlarmClock(ActivityManager.getCurrentUser());
        return info != null ? info.getTriggerTime() : 0;
    }

    private boolean meetsSchedule(ScheduleCalendar cal, long time) {
        return cal != null ? cal.isInSchedule(time) : false;
    }

    private static ScheduleCalendar toScheduleCalendar(Uri conditionId) {
        ScheduleInfo schedule = ZenModeConfig.tryParseScheduleConditionId(conditionId);
        if (schedule == null || schedule.days == null || schedule.days.length == 0) {
            return null;
        }
        ScheduleCalendar sc = new ScheduleCalendar();
        sc.setSchedule(schedule);
        sc.setTimeZone(TimeZone.getDefault());
        return sc;
    }

    private void setRegistered(boolean registered) {
        if (this.mRegistered != registered) {
            if (DEBUG) {
                Slog.d(TAG, "setRegistered " + registered);
            }
            this.mRegistered = registered;
            if (this.mRegistered) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.TIME_SET");
                filter.addAction("android.intent.action.TIMEZONE_CHANGED");
                filter.addAction(ACTION_EVALUATE);
                filter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
                registerReceiver(this.mReceiver, filter);
            } else {
                unregisterReceiver(this.mReceiver);
            }
        }
    }

    private void notifyCondition(Uri conditionId, int state, String reason) {
        if (DEBUG) {
            Slog.d(TAG, "notifyCondition " + conditionId + " " + Condition.stateToString(state) + " reason=" + reason);
        }
        notifyCondition(createCondition(conditionId, state));
    }

    private Condition createCondition(Uri id, int state) {
        String summary = NOT_SHOWN;
        String line1 = NOT_SHOWN;
        String line2 = NOT_SHOWN;
        return new Condition(id, NOT_SHOWN, NOT_SHOWN, NOT_SHOWN, 0, state, 2);
    }

    private boolean conditionSnoozed(Uri conditionId) {
        boolean contains;
        synchronized (this.mSnoozed) {
            contains = this.mSnoozed.contains(conditionId);
        }
        return contains;
    }

    private void addSnoozed(Uri conditionId) {
        synchronized (this.mSnoozed) {
            this.mSnoozed.add(conditionId);
            saveSnoozedLocked();
        }
    }

    private void removeSnoozed(Uri conditionId) {
        synchronized (this.mSnoozed) {
            this.mSnoozed.remove(conditionId);
            saveSnoozedLocked();
        }
    }

    public void saveSnoozedLocked() {
        Secure.putStringForUser(this.mContext.getContentResolver(), SCP_SETTING, TextUtils.join(SEPARATOR, this.mSnoozed), ActivityManager.getCurrentUser());
    }

    public void readSnoozed() {
        synchronized (this.mSnoozed) {
            long identity = Binder.clearCallingIdentity();
            try {
                String setting = Secure.getStringForUser(this.mContext.getContentResolver(), SCP_SETTING, ActivityManager.getCurrentUser());
                if (setting != null) {
                    String[] tokens = setting.split(SEPARATOR);
                    for (String token : tokens) {
                        String token2;
                        if (token2 != null) {
                            token2 = token2.trim();
                        }
                        if (!TextUtils.isEmpty(token2)) {
                            this.mSnoozed.add(Uri.parse(token2));
                        }
                    }
                }
                Binder.restoreCallingIdentity(identity);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(identity);
            }
        }
    }
}
