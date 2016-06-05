/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ilexiconn.magister.handler;

import com.google.gson.Gson;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.exeption.PrivilegeException;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * @since 0.1.2
 */
public class PasswordHandler implements IHandler {
    private Gson gson = GsonUtil.getGson();
    private Magister magister;

    public PasswordHandler(Magister magister) {
        this.magister = magister;
    }

    @Override
    public String getPrivilege() {
        return "WachtwoordWijzigen";
    }

    /**
     * Change the password of the current profile.
     *
     * @param oldPassword  the current password.
     * @param newPassword  the new password.
     * @param newPassword2 the new password.
     * @return a String with the response. 'Successful' if the password changed successfully.
     * @throws IOException               if there is no active internet connection.
     * @throws InvalidParameterException if one of the parameters is null or empty, or when the two new passwords aren't
     *                                   the same.
     * @throws PrivilegeException        if the profile doesn't have the privilege to perform this action.
     */
    public String changePassword(String oldPassword, String newPassword, String newPassword2) throws IOException, InvalidParameterException, PrivilegeException {
        /*if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty() || newPassword2 == null || newPassword2.isEmpty()) {
            throw new InvalidParameterException("Parameters can't be null or empty!");
        } else if (!newPassword.equals(newPassword2)) {
            throw new InvalidParameterException("New passwords don't match!");
        }
        Map<String, String> nameValuePairMap = new HashMap<String, String>();
        nameValuePairMap.put("HuidigWachtwoord", oldPassword);
        nameValuePairMap.put("NieuwWachtwoord", newPassword);
        nameValuePairMap.put("PersoonId", magister.profile.id + "");
        nameValuePairMap.put("WachtwoordBevestigen", newPassword2);
        Response response = gson.fromJson(HttpUtil.httpPost(magister.school.url + "/api/personen/account/wachtwoordwijzigen?persoonId=" + magister.profile.id, nameValuePairMap), Response.class);
        if (response == null) {
            magister.user.password = newPassword;
            return "Successful";
        } else {
            LogUtil.printError(response.message, new InvalidParameterException());
            return response.message;
        }*/
        return null;
    }
}
