package com.softechurecabdriver.provider.ui.activity.notification_manager;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
