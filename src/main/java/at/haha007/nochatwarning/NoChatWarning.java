package at.haha007.nochatwarning;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoChatWarning extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                var internalAdventureComponent = event.getPacket()
                        .getStructures().read(0)
                        .getOptionalStructures().read(0)
                        .map(c -> c.getStructures().getTarget())
                        .orElse(null);
                if (internalAdventureComponent == null) {
                    event.setCancelled(true);
                    return;
                }

                PacketContainer container = new PacketContainer(PacketType.Play.Server.SYSTEM_CHAT);
                container.getBooleans().write(0, false);
                container.getStrings().write(0, WrappedChatComponent.fromHandle(internalAdventureComponent).getJson());
                event.setPacket(container);
            }
        });
    }
}
