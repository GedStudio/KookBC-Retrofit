/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 KookBC contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package snw.kookbc.impl.entity.channel;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import snw.jkook.entity.Guild;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.Category;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;
import snw.jkook.message.component.BaseComponent;
import snw.jkook.util.PageIterator;
import snw.kookbc.impl.KBCClient;
import snw.kookbc.impl.entity.builder.MessageBuilder;
import snw.kookbc.impl.network.HttpAPIRoute;
import snw.kookbc.impl.pageiter.TextChannelMessageIterator;
import snw.kookbc.util.MapBuilder;
import snw.jkook.util.Validate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class TextChannelImpl extends ChannelImpl implements TextChannel {
    private int chatLimitTime;

    public TextChannelImpl(String id, User master, Guild guild, boolean permSync, Category parent, String name, Collection<RolePermissionOverwrite> rpo, Collection<UserPermissionOverwrite> upo, int chatLimitTime) {
        super(id, master, guild, permSync, parent, name, rpo, upo);
        this.chatLimitTime = chatLimitTime;
    }

    @Override
    public String createInvite(int validSeconds, int validTimes) {
        Map<String, Object> body = new MapBuilder()
                .put("channel_id", getId())
                .put("duration", validSeconds)
                .put("setting_times", validTimes)
                .build();
        JsonObject object = KBCClient.getInstance().getConnector().getClient().post(HttpAPIRoute.INVITE_CREATE.toFullURL(), body);
        return object.get("url").getAsString();
    }

    @Override
    public PageIterator<Collection<TextChannelMessage>> getMessages(@Nullable String refer, boolean isPin, String queryMode) {
        Validate.isTrue(Objects.equals(queryMode, "before") || Objects.equals(queryMode, "around") || Objects.equals(queryMode, "after"), "Invalid queryMode");
        return new TextChannelMessageIterator(this, refer, isPin, queryMode);
    }

    @Override
    public void sendComponent(BaseComponent component, @Nullable Message quote, @Nullable User tempTarget) {
        Object[] result = MessageBuilder.serialize(component);
        MapBuilder builder = new MapBuilder()
                .put("target_id", getId())
                .put("type", result[0])
                .put("content", result[1]);
        if (quote != null) {
            builder.put("quote", quote.getId());
        }
        if (tempTarget != null) {
            builder.put("temp_target_id", tempTarget.getId());
        }
        Map<String, Object> body = builder.build();
        KBCClient.getInstance().getConnector().getClient().post(HttpAPIRoute.CHANNEL_MESSAGE_SEND.toFullURL(), body);
    }

    @Override
    public int getChatLimitTime() {
        return chatLimitTime;
    }

    // setters following:

    public void setChatLimitTime(int chatLimitTime) {
        this.chatLimitTime = chatLimitTime;
    }

}
