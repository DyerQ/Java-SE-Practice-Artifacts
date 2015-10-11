package com.netcracker.edu;

import com.jcraft.jsch.UserInfo;

class LocalUserInfo implements UserInfo {
    // TODO Describe
    public String getPassword() {
        return null;
    }

    public boolean promptYesNo(String str) {
        return true;
    }

    public String getPassphrase() {
        return null;
    }

    public boolean promptPassphrase(String message) {
        return true;
    }

    public boolean promptPassword(String message) {
        return true;
    }

    public void showMessage(String message) {

    }
}
