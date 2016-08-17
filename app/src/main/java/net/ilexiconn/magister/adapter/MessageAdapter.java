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

package net.ilexiconn.magister.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.ilexiconn.magister.container.Message;
import net.ilexiconn.magister.container.type.MessageType;
import net.ilexiconn.magister.util.GsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends TypeAdapter<Message[]> {
    public Gson gson = GsonUtil.getGson();

    @Override
    public void write(JsonWriter out, Message[] value) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Message[] read(JsonReader in) throws IOException {
        JsonObject object = gson.getAdapter(JsonElement.class).read(in).getAsJsonObject();
        JsonArray array = object.get("Items").getAsJsonArray();
        List<Message> messageList = new ArrayList<Message>();
        for (JsonElement element : array) {
            Message message = gson.fromJson(element, Message.class);
            message.type = MessageType.getTypeById(element.getAsJsonObject().get("Soort").getAsInt());
            messageList.add(message);
        }
        return messageList.toArray(new Message[messageList.size()]);
    }
}
