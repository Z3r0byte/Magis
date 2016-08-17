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
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.adapter.ArrayAdapter;
import net.ilexiconn.magister.adapter.MessageAdapter;
import net.ilexiconn.magister.adapter.SingleMessageAdapter;
import net.ilexiconn.magister.container.Message;
import net.ilexiconn.magister.container.MessageFolder;
import net.ilexiconn.magister.container.SingleMessage;
import net.ilexiconn.magister.container.type.MessageType;
import net.ilexiconn.magister.exeption.PrivilegeException;
import net.ilexiconn.magister.util.GsonUtil;
import net.ilexiconn.magister.util.HttpUtil;
import net.ilexiconn.magister.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHandler implements IHandler {
    private Gson gson;
    private Magister magister;

    public MessageHandler(Magister magister) {
        this.magister = magister;
        Map<Class<?>, TypeAdapter<?>> map = new HashMap<Class<?>, TypeAdapter<?>>();
        map.put(MessageFolder[].class, new ArrayAdapter<MessageFolder>(MessageFolder.class, MessageFolder[].class));
        map.put(Message[].class, new MessageAdapter());
        map.put(SingleMessage[].class, new SingleMessageAdapter());
        gson = GsonUtil.getGsonWithAdapters(map);
    }

    /**
     * Get an array with all the {@link MessageFolder}s of this profile.
     *
     * @return an array with all the {@link MessageFolder}s of this profile.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public MessageFolder[] getMessageFolders() throws IOException {
        return gson.fromJson(HttpUtil.httpGet(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/berichten/mappen"), MessageFolder[].class);
    }

    /**
     * Get an array of {@link Message}s of a specific {@link MessageFolder}.
     *
     * @param folder the {@link MessageFolder} instance.
     * @return an array of {@link Message}s.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Message[] getMessagesPerFolder(MessageFolder folder) throws IOException {
        return getMessagesPerFolder(folder.id);
    }

    /**
     * Get an array of {@link Message}s of a specific {@link MessageFolder}.
     *
     * @param folderID the {@link MessageFolder} ID.
     * @return an array of {@link Message}s.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public Message[] getMessagesPerFolder(int folderID) throws IOException {
        return gson.fromJson(HttpUtil.httpGet(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/berichten?mapId=" + folderID + "&orderby=soort+DESC&skip=0&top=25"), Message[].class);
    }

    /**
     * Get an array of {@link SingleMessage}s of this specific {@link Message}.
     *
     * @param message the {@link Message} instance.
     * @return an array of {@link SingleMessage}s.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public SingleMessage[] getSingleMessage(Message message) throws IOException {
        return getSingleMessage(message.id, message.type);
    }

    /**
     * Get an array of {@link SingleMessage}s of this specific {@link Message}.
     *
     * @param messageID   the {@link Message} ID.
     * @param messageType the type of this message.
     * @return an array of {@link SingleMessage}s.
     * @throws IOException        if there is no active internet connection.
     * @throws PrivilegeException if the profile doesn't have the privilege to perform this action.
     */
    public SingleMessage[] getSingleMessage(int messageID, MessageType messageType) throws IOException {
        return gson.fromJson(HttpUtil.httpGet(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/berichten/" + messageID + "?berichtSoort=" + messageType.getName()), SingleMessage[].class);
    }

    /**
     * Post a single message to Magister.
     *
     * @param message the message.
     * @return true if the message got sent.
     */
    public boolean postMessage(SingleMessage message) {
        try {
            String data = gson.toJson(message);
            InputStreamReader respose = HttpUtil.httpPostRaw(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/berichten", data);
            return true;
        } catch (IOException e) {
            LogUtil.printError(e.getMessage(), e.getCause());
            return false;
        }
    }

    public Message updateMessage(SingleMessage message) throws IOException {
        String data = gson.toJson(message);
        return gson.fromJson(new JsonReader(HttpUtil.httpPut(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/berichten/" + message.id, data)), SingleMessage.class);
    }

    public Message markMessageRead(SingleMessage message, boolean isRead) throws IOException {
        message.isRead = isRead;
        String data = gson.toJson(message);
        return updateMessage(message);
    }

    public Message moveMessageTo(SingleMessage message, MessageFolder messageFolder) throws IOException {
        message.mapId = messageFolder.id;
        message.mapTitle = messageFolder.title;
        return updateMessage(message);
    }

    public void emptyMessageFolder(MessageFolder messageFolder) throws IOException {
        HttpUtil.httpDelete(magister.schoolUrl.getApiUrl() + "personen/" + magister.profile.id + "/berichten/map/" + messageFolder.id);
    }

    /**
     * Get the attachments from a message and download them to a directory.
     *
     * @param message     the message.
     * @param downloadDir the directory to download the files to.
     * @return a list of files with the attachments. Will return null if there aren't any attachments bound with the
     * message.
     * @throws IOException if there is no active internet connection.
     */
    public File[] getAttachmentsOfMessage(SingleMessage message, File downloadDir) throws IOException {
        URL[] urls = message.getAttachmentsUrls(magister);
        if (urls == null || urls.length == 0) {
            return null;
        }
        List<File> files = new ArrayList<File>();
        for (URL url : urls) {
            files.add(HttpUtil.httpGetFile(url.toString(), downloadDir));
        }
        if (files.size() == 0) {
            return null;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrivilege() {
        return "Berichten";
    }
}
